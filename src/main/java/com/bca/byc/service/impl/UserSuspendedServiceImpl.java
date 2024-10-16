package com.bca.byc.service.impl;

import com.bca.byc.converter.UserSuspendedDTOConverter;
import com.bca.byc.converter.dictionary.PageCreateReturn;
import com.bca.byc.converter.parsing.GlobalConverter;
import com.bca.byc.converter.parsing.TreeUserManagementConverter;
import com.bca.byc.entity.AppAdmin;
import com.bca.byc.entity.AppUser;
import com.bca.byc.entity.AppUserAttribute;
import com.bca.byc.enums.LogStatus;
import com.bca.byc.exception.BadRequestException;
import com.bca.byc.model.ActionMessageRequest;
import com.bca.byc.model.LogUserManagementRequest;
import com.bca.byc.model.UserManagementDetailResponse;
import com.bca.byc.model.UserManagementListResponse;
import com.bca.byc.model.projection.CmsGetIdFromSecureIdProjection;
import com.bca.byc.model.search.ListOfFilterPagination;
import com.bca.byc.model.search.SavedKeywordAndPageable;
import com.bca.byc.repository.LogUserManagementRepository;
import com.bca.byc.repository.UserSuspendedRepository;
import com.bca.byc.repository.auth.AppAdminRepository;
import com.bca.byc.repository.handler.HandlerRepository;
import com.bca.byc.response.ResultPageResponseDTO;
import com.bca.byc.service.UserSuspendedService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static com.bca.byc.converter.parsing.TreeLogUserManagement.logUserManagement;
import static com.bca.byc.converter.parsing.TreeUserManagementConverter.IndexResponse;


@Service
@AllArgsConstructor
public class UserSuspendedServiceImpl implements UserSuspendedService {

    private final AppAdminRepository adminRepository;
    private final LogUserManagementRepository logUserManagementRepository;

    private final UserSuspendedRepository repository;

    private final UserSuspendedDTOConverter converter;

    @Override
    public ResultPageResponseDTO<UserManagementListResponse> listData(Integer pages, Integer limit, String sortBy, String direction, String keyword, Long locationId, LocalDate startDate, LocalDate endDate) {
        ListOfFilterPagination filter = new ListOfFilterPagination(
                keyword,
                startDate,
                endDate
        );
        SavedKeywordAndPageable set = GlobalConverter.createPageable(pages, limit, sortBy, direction, keyword, filter);
        // set date
        LocalDateTime start = (startDate == null) ? LocalDateTime.of(1970, 1, 1, 0, 0) : startDate.atStartOfDay();
        LocalDateTime end = (endDate == null) ? LocalDateTime.now() : endDate.atTime(23, 59, 59);

        Page<AppUser> pageResult = repository.findByKeywordAndStatusAndSuspendedAndCreatedAt(set.keyword(), locationId, start, end, set.pageable());
        List<UserManagementListResponse> dtos = pageResult.stream().map((c) -> {
            UserManagementListResponse dto = new UserManagementListResponse();
            IndexResponse(c,dto);
            return dto;
        }).collect(Collectors.toList());

        return PageCreateReturn.create(pageResult, dtos);
    }

    @Override
    public UserManagementDetailResponse findDataBySecureId(String id) throws BadRequestException {
        AppUser user = repository.findBySecureId(id)
                .orElseThrow(() -> new BadRequestException("User suspended not found"));

        UserManagementDetailResponse dto = new UserManagementDetailResponse();
        TreeUserManagementConverter converter = new TreeUserManagementConverter();
        converter.DetailResponse(user, dto);
        return dto;
    }

    @Override
    public void makeUserIsDeletedTrue(String id, @Valid ActionMessageRequest dto) {
        AppAdmin admin = GlobalConverter.getAdminEntity(adminRepository);
        AppUser data = HandlerRepository.getEntityBySecureId(id, repository, "User not found");
        AppUserAttribute userAttribute = data.getAppUserAttribute();
        userAttribute.setIsDeleted(true);
        data.setAppUserAttribute(userAttribute);
        LogUserManagementRequest newLog = new LogUserManagementRequest(
                "DELETED",
                dto.getMessage()
        );

        logUserManagement(
                null,
                data,
                admin,
                LogStatus.DELETED,
                newLog,
                logUserManagementRepository
        );

        GlobalConverter.CmsAdminUpdateAtBy(data, admin);
        repository.save(data);
    }

    @Override
    public void makeUserIsSuspendedFalse(String id) {
        AppAdmin admin = GlobalConverter.getAdminEntity(adminRepository);
        AppUser data = HandlerRepository.getEntityBySecureId(id, repository, "User not found");
        AppUserAttribute userAttribute = data.getAppUserAttribute();
        userAttribute.setIsSuspended(false);
        data.setAppUserAttribute(userAttribute);

        GlobalConverter.CmsAdminUpdateAtBy(data, admin);
        repository.save(data);
    }

    @Override
    public void makeUserBulkSuspendedFalse(Set<String> ids) {
        AppAdmin admin = GlobalConverter.getAdminEntity(adminRepository);
        Set<CmsGetIdFromSecureIdProjection> userProjections = repository.findToSuspendBySecureIdIn(ids);

        userProjections.forEach(projection -> {
            AppUser data = repository.findById(projection.getId())
                    .orElseThrow(() -> new EntityNotFoundException("User not found"));
            AppUserAttribute userAttribute = data.getAppUserAttribute();
            userAttribute.setIsSuspended(false);
            data.setAppUserAttribute(userAttribute);

            GlobalConverter.CmsAdminUpdateAtBy(data, admin);
            repository.save(data);
        });
    }
}
