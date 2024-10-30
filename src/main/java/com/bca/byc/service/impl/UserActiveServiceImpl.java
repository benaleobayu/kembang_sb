package com.bca.byc.service.impl;

import com.bca.byc.converter.UserActiveDTOConverter;
import com.bca.byc.converter.dictionary.PageCreateReturn;
import com.bca.byc.converter.parsing.GlobalConverter;
import com.bca.byc.converter.parsing.TreeLogUserManagement;
import com.bca.byc.converter.parsing.TreeUserManagementConverter;
import com.bca.byc.entity.*;
import com.bca.byc.enums.LogStatus;
import com.bca.byc.exception.BadRequestException;
import com.bca.byc.model.Elastic.UserActiveElastic;
import com.bca.byc.model.LogUserManagementRequest;
import com.bca.byc.model.UserManagementDetailResponse;
import com.bca.byc.model.UserManagementFilterList;
import com.bca.byc.model.UserManagementListResponse;
import com.bca.byc.model.data.ListTagUserResponse;
import com.bca.byc.model.projection.CmsGetIdFromSecureIdProjection;
import com.bca.byc.model.search.ListOfFilterPagination;
import com.bca.byc.model.search.SavedKeywordAndPageable;
import com.bca.byc.repository.BranchRepository;
import com.bca.byc.repository.Elastic.UserActiveElasticRepository;
import com.bca.byc.repository.LogUserManagementRepository;
import com.bca.byc.repository.UserActiveRepository;
import com.bca.byc.repository.auth.AppAdminRepository;
import com.bca.byc.repository.AppUserRepository;
import com.bca.byc.repository.handler.HandlerRepository;
import com.bca.byc.response.ResultPageResponseDTO;
import com.bca.byc.security.util.ContextPrincipal;
import com.bca.byc.service.UserActiveService;
import com.bca.byc.service.UserActiveUpdateRequest;
import com.bca.byc.util.PaginationUtil;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static com.bca.byc.converter.parsing.TreeLogUserManagement.logUserManagement;
import static com.bca.byc.converter.parsing.TreeUserManagementConverter.IndexResponse;
import static com.bca.byc.repository.handler.HandlerRepository.getEntityBySecureId;

@Service
@AllArgsConstructor
public class UserActiveServiceImpl implements UserActiveService {
    private final AppUserRepository userRepository;

    private final AppAdminRepository adminRepository;
    private final UserActiveRepository repository;
    private final UserActiveElasticRepository elasticRepository;
    private final UserActiveDTOConverter converter;
    private final BranchRepository branchRepository;
    private final LogUserManagementRepository logUserManagementRepository;
    @Value("${app.base.url}")
    private String baseUrl;

    @Override
    public UserManagementDetailResponse findBySecureId(String id) throws BadRequestException {
        AppUser data = getEntityData(id);

        UserManagementDetailResponse dto = new UserManagementDetailResponse();
        TreeUserManagementConverter converter = new TreeUserManagementConverter();
        converter.DetailResponse(data, dto);
        return dto;
    }

    @Override
    public ResultPageResponseDTO<UserManagementListResponse> listDataActiveIndex(Integer pages, Integer limit, String sortBy, String direction, String keyword, UserManagementFilterList filter) {
        ListOfFilterPagination filtering = new ListOfFilterPagination(
                keyword,
                filter.getStartDate(),
                filter.getEndDate()
        );
        SavedKeywordAndPageable set = GlobalConverter.createPageable(pages, limit, sortBy, direction, keyword, filtering);

        // set date
        LocalDateTime start = (filter.getStartDate() == null) ? LocalDateTime.of(1970, 1, 1, 0, 0) : filter.getStartDate().atStartOfDay();
        LocalDateTime end = (filter.getEndDate() == null) ? LocalDateTime.now() : filter.getEndDate().atTime(23, 59, 59);

        Page<AppUser> pageResult = repository.getDataActiveIndex(set.keyword(), set.pageable(), start , end, filter.getLocationId(), filter.getSegmentation(), filter.getIsSenior());
        List<UserManagementListResponse> dtos = pageResult.stream().map((c) -> {
            UserManagementListResponse dto = new UserManagementListResponse();
            IndexResponse(c, dto);
            return dto;
        }).collect(Collectors.toList());

        return PageCreateReturn.create(pageResult, dtos);
    }

    @Override
    @Transactional
    public void updateData(String id, UserActiveUpdateRequest dto) throws BadRequestException {
        AppAdmin admin = GlobalConverter.getAdminEntity(adminRepository);
        AppUser data = getEntityData(id);
        Branch branch = getEntityBranch(dto.getBranchId());
        converter.convertToUpdateRequest(data, dto, branch);

        LogUserManagementRequest logDto = new LogUserManagementRequest(
                "UPDATED",
                "Updated user active"
        );

        TreeLogUserManagement.logUserManagement(
                null,
                data,
                admin,
                LogStatus.UPDATED,
                logDto,
                logUserManagementRepository
        );

        GlobalConverter.CmsAdminUpdateAtBy(data, admin);
        repository.save(data);
    }

    @Override
    @Transactional
    public void deleteData(String id) throws BadRequestException {
        AppAdmin admin = GlobalConverter.getAdminEntity(adminRepository);
        AppUser data = getEntityBySecureId(id, repository, "user not found");
        // delete data
        if (!repository.existsBySecureId(id)) {
            throw new BadRequestException("user not found");
        } else {
            data.getAppUserAttribute().setIsSuspended(true);
            data.getAppUserAttribute().setIsDeleted(true);
            GlobalConverter.CmsAdminUpdateAtBy(data, admin);

            repository.save(data);
        }
    }

    @Override
    @Transactional
    public void suspendData(String id, @Valid LogUserManagementRequest dto) throws BadRequestException {
        AppUser data = getEntityBySecureId(id, repository, "user not found");
        String AdminEmail = ContextPrincipal.getPrincipal();
        AppAdmin admin = adminRepository.findByEmail(AdminEmail).orElseThrow(() -> new BadRequestException("admin not found"));
        AppUserAttribute attribute = data.getAppUserAttribute();
        // toggle
        attribute.setIsSuspended(!attribute.getIsSuspended().equals(true));
        // save
        data.setAppUserAttribute(attribute);
        logUserManagement(
                null,
                data,
                admin,
                LogStatus.SUSPENDED,
                dto,
                logUserManagementRepository
        );

        GlobalConverter.CmsAdminUpdateAtBy(data, admin);
        repository.save(data);
    }

    @Override
    public void makeUserBulkSuspendedTrue(Set<String> ids) {
        AppAdmin admin = GlobalConverter.getAdminEntity(adminRepository);
        Set<CmsGetIdFromSecureIdProjection> userProjections = repository.findToSuspendBySecureIdIn(ids);

        userProjections.forEach(projection -> {
            AppUser data = repository.findById(projection.getId())
                    .orElseThrow(() -> new EntityNotFoundException("User not found"));
            AppUserAttribute userAttribute = data.getAppUserAttribute();
            userAttribute.setIsSuspended(true);
            data.setAppUserAttribute(userAttribute);
            GlobalConverter.CmsAdminUpdateAtBy(data, admin);
            repository.save(data);
        });
    }

    @Override
    public ResultPageResponseDTO<ListTagUserResponse> listDataTagUser(Integer pages, Integer limit, String sortBy, String direction, String keyword) {
        AppUser userLogin = GlobalConverter.getUserEntity(userRepository);
        keyword = StringUtils.isEmpty(keyword) ? "%" : keyword + "%";
        Sort sort = Sort.by(new Sort.Order(PaginationUtil.getSortBy(direction), sortBy));
        Pageable pageable = PageRequest.of(pages, limit, sort);

        Page<ListTagUserResponse> pageResult = repository.findListTagUser(keyword, pageable, userLogin.getId());
        List<ListTagUserResponse> dtos = pageResult.stream().map((data) -> {
            ListTagUserResponse dto = new ListTagUserResponse();

            dto.setAvatar(GlobalConverter.getParseImage(data.getAvatar(), baseUrl));
            dto.setId(data.getId());
            dto.setIndex(data.getIndex());
            dto.setName(data.getName());
            dto.setBusinessName(data.getBusinessName());
            dto.setLineOfBusiness(data.getLineOfBusiness());
            dto.setIsPrimary(data.getIsPrimary());

            return dto;
        }).collect(Collectors.toList());


        return PageCreateReturn.create(pageResult, dtos);
    }

    @Override
    public com.bca.byc.response.Page<UserActiveElastic> getAllActiveUser(Integer page, Integer size) {
        Pageable pageable = PageRequest.of(page, size);
        var userList = elasticRepository.findAllBy(pageable);

        return new com.bca.byc.response.Page<>(
                userList.stream().collect(Collectors.toList()),
                pageable,
                userList.getTotalElements()
        );
    }

    // --- Helper ---
    private AppUser getEntityData(String id) {
        return HandlerRepository.getIdBySecureId(
                id,
                repository::findBySecureId,
                projection -> repository.findById(projection.getId()),
                "user not found"
        );
    }
    private Branch getEntityBranch(String id) {
        return HandlerRepository.getIdBySecureId(
                id,
                branchRepository::findBySecureId,
                projection -> branchRepository.findById(projection.getId()),
                "Branch not found"
        );
    }


}
