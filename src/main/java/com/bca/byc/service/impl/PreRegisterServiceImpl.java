package com.bca.byc.service.impl;

import com.bca.byc.converter.PreRegisterDTOConverter;
import com.bca.byc.converter.dictionary.PageCreateReturn;
import com.bca.byc.converter.parsing.GlobalConverter;
import com.bca.byc.entity.AppAdmin;
import com.bca.byc.entity.PreRegister;
import com.bca.byc.enums.AdminApprovalStatus;
import com.bca.byc.enums.AdminType;
import com.bca.byc.enums.LogStatus;
import com.bca.byc.exception.BadRequestException;
import com.bca.byc.model.*;
import com.bca.byc.model.projection.CmsGetIdFromSecureIdProjection;
import com.bca.byc.model.search.ListOfFilterPagination;
import com.bca.byc.model.search.SavedKeywordAndPageable;
import com.bca.byc.repository.LogUserManagementRepository;
import com.bca.byc.repository.PreRegisterRepository;
import com.bca.byc.repository.auth.AppAdminRepository;
import com.bca.byc.repository.handler.HandlerRepository;
import com.bca.byc.response.RejectRequest;
import com.bca.byc.response.ResultPageResponseDTO;
import com.bca.byc.service.AppAdminService;
import com.bca.byc.service.PreRegisterService;
import jakarta.mail.MessagingException;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityNotFoundException;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static com.bca.byc.converter.parsing.TreeLogUserManagement.logUserManagement;

@Slf4j
@Service
@AllArgsConstructor
public class PreRegisterServiceImpl implements PreRegisterService {

    private PreRegisterRepository repository;
    private LogUserManagementRepository logUserManagementRepository;
    private PreRegisterDTOConverter converter;

    private AppAdminService adminService;
    private AppAdminRepository adminRepository;

    @Override
    public PreRegisterDetailResponse findDataById(Long id) throws BadRequestException {
        PreRegister data = HandlerRepository.getEntityById(id, repository, "Data Not Found");
        return converter.convertToListResponse(data);
    }

    @Override
    public PreRegisterDetailResponse findDataBySecureId(String id) {
        PreRegister data = HandlerRepository.getEntityBySecureId(id, repository, "Data Not Found");
        return converter.convertToListResponse(data);
    }

    @Override
    public ResultPageResponseDTO<PreRegisterDetailResponse> listData(Integer pages, Integer limit, String sortBy, String direction, String keyword, AdminApprovalStatus status, LocalDate startDate, LocalDate endDate) {
        AppAdmin admin = GlobalConverter.getAdminEntity(adminRepository);
        ListOfFilterPagination filter = new ListOfFilterPagination(
                keyword,
                startDate,
                endDate,
                status
        );
        SavedKeywordAndPageable set = GlobalConverter.createPageable(pages, limit, sortBy, direction, keyword, filter);

        // set date
        LocalDateTime start = (startDate == null) ? LocalDateTime.of(1970, 1, 1, 0, 0) : startDate.atStartOfDay();
        LocalDateTime end = (endDate == null) ? LocalDateTime.now() : endDate.atTime(23, 59, 59);

        int s0 = AdminApprovalStatus.PENDING.ordinal(); // 0
        int s1 = AdminApprovalStatus.OPT_APPROVED.ordinal(); // 1
        int s4 = AdminApprovalStatus.REJECTED.ordinal(); // 4
        int s5 = AdminApprovalStatus.APPROVED.ordinal(); // 5
        int s6 = AdminApprovalStatus.DELETED.ordinal(); // 6
        List<Integer> listStatus;
        Page<PreRegister> pageResult = null;
        if (admin.getRole().getName().equals("SUPERADMIN")) {
            listStatus = List.of(s0, s1, s4, s5, s6);
            pageResult = repository.FindAllDataByKeywordAndStatus(listStatus, set.keyword(), status, start, end, set.pageable());
        }
        if (admin.getRole().getName().contains("ADMIN-OPERATOR")) {
            listStatus = List.of(s0, s1, s4, s5);
            pageResult = repository.FindAllDataByKeywordAndStatus(listStatus, set.keyword(), status, start, end, set.pageable());
        }
        if (admin.getRole().getName().contains("ADMIN-SUPERVISOR")) {
            listStatus = List.of(s1, s4, s5);
            pageResult = repository.FindAllDataByKeywordAndStatus(listStatus, set.keyword(), status, start, end, set.pageable());
        }

        assert pageResult != null;
        List<PreRegisterDetailResponse> dtos = pageResult.stream().map((c) -> {
            PreRegisterDetailResponse dto = converter.convertToListResponse(c);
            return dto;
        }).collect(Collectors.toList());

        return PageCreateReturn.create(pageResult, dtos);
    }

    @Override
    public List<PreRegisterDetailResponse> findAllData() {
        // Get the list
        List<PreRegister> datas = repository.findAll();

        // stream into the list
        return datas.stream()
                .map(converter::convertToListResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public void saveData(@Valid PreRegisterCreateRequest dto, String email) throws BadRequestException {
        if (repository.existsByEmail(dto.getEmail())) {
            throw new BadRequestException("Email already exists");
        }

        AppAdmin admin = adminService.findByEmail(email);
        if (admin == null) {
            throw new BadRequestException("Admin not found");
        }
        PreRegister data = converter.convertToCreateRequest(dto, admin);
        AdminApprovalStatus isSaved = dto.getStatus() ? AdminApprovalStatus.OPT_APPROVED : AdminApprovalStatus.PENDING;
        data.setStatusApproval(isSaved);
        repository.save(data);
    }


    @Override
    @Transactional
    public void updateData(String id, PreRegisterUpdateRequest dto) throws BadRequestException {
        PreRegister data = HandlerRepository.getEntityBySecureId(id, repository, "Data not found");
        converter.convertToUpdateRequest(data, dto);
        data.setUpdatedAt(LocalDateTime.now());
        AdminApprovalStatus isSaved = dto.getStatus() ? AdminApprovalStatus.OPT_APPROVED : AdminApprovalStatus.PENDING;
        data.setStatusApproval(isSaved);
        repository.save(data);
    }

    @Override
    @Transactional(rollbackOn = Exception.class)
    public void bulkDelete(Set<String> ids) throws BadRequestException {
        AppAdmin admin = GlobalConverter.getAdminEntity(adminRepository);
        Set<CmsGetIdFromSecureIdProjection> userProjections = repository.findIdBySecureIdIn(ids);

        userProjections.forEach(projection -> {
            PreRegister data = repository.findById(projection.getId())
                    .orElseThrow(() -> new EntityNotFoundException("User not found"));
            data.setAccountType(data.getAccountType());
            data.setIsDeleted(true);
            data.setIsActive(false);
            data.setStatusApproval(AdminApprovalStatus.DELETED);
            data.setEmail(data.getEmail() + "_deleted");
            data.setMemberCin(data.getMemberCin() + "_deleted");
            GlobalConverter.CmsAdminUpdateAtBy(data, admin);
            repository.saveAndFlush(data);
        });
    }

    @Override
    public void approveData(String id, String email) throws BadRequestException, MessagingException {
        AppAdmin admin = adminService.findByEmail(email);
        if (admin == null) {
            throw new BadRequestException("Invalid email admin");
        }

        PreRegister data = repository.findBySecureId(id)
                .orElseThrow(() -> new BadRequestException("data pre register not found"));
        // update on converter
        converter.convertToApprovalRequest(data, admin);
        // save
        repository.save(data);
    }

    @Override
    public void rejectData(String id, RejectRequest reason, String email) throws BadRequestException {
        AppAdmin admin = adminService.findByEmail(email);
        if (admin == null) {
            throw new BadRequestException("Invalid email admin");
        }

        PreRegister data = repository.findBySecureId(id)
                .orElseThrow(() -> new BadRequestException("data pre register not found"));
        // update on converter
        converter.convertToRejectRequest(data, reason, admin);

        LogUserManagementRequest dto = new LogUserManagementRequest(
                "REJECT",
                reason.message()
        );
        logUserManagement(
                data,
                null,
                admin,
                LogStatus.REJECT,
                dto,
                logUserManagementRepository
        );
        // save
        repository.save(data);
    }


}

