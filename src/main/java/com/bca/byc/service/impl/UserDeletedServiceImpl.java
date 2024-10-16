package com.bca.byc.service.impl;

import com.bca.byc.converter.UserDeletedDTOConverter;
import com.bca.byc.converter.dictionary.PageCreateReturn;
import com.bca.byc.converter.parsing.GlobalConverter;
import com.bca.byc.converter.parsing.TreeUserManagementConverter;
import com.bca.byc.entity.AppAdmin;
import com.bca.byc.entity.AppUser;
import com.bca.byc.entity.AppUserAttribute;
import com.bca.byc.exception.BadRequestException;
import com.bca.byc.model.UserManagementDetailResponse;
import com.bca.byc.model.UserManagementListResponse;
import com.bca.byc.model.projection.CMSBulkDeleteProjection;
import com.bca.byc.model.search.ListOfFilterPagination;
import com.bca.byc.model.search.SavedKeywordAndPageable;
import com.bca.byc.repository.UserDeletedRepository;
import com.bca.byc.repository.auth.AppAdminRepository;
import com.bca.byc.repository.handler.HandlerRepository;
import com.bca.byc.response.ResultPageResponseDTO;
import com.bca.byc.service.UserDeletedService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static com.bca.byc.converter.parsing.TreeUserManagementConverter.IndexResponse;

@Service
@RequiredArgsConstructor
public class UserDeletedServiceImpl implements UserDeletedService {

    private final AppAdminRepository adminRepository;
    private final UserDeletedRepository repository;

    private final UserDeletedDTOConverter converter;

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

        Page<AppUser> pageResult = repository.findByKeywordAndStatusAndDeletedAndCreatedAt(set.keyword(), locationId, start, end, set.pageable());
        List<UserManagementListResponse> dtos = pageResult.stream().map((c) -> {
            UserManagementListResponse dto = new UserManagementListResponse();
            IndexResponse(c, dto);
            return dto;
        }).collect(Collectors.toList());

        return PageCreateReturn.create(pageResult, dtos);
    }

    @Override
    public UserManagementDetailResponse findDataById(String id) throws BadRequestException {
        AppUser data = HandlerRepository.getIdBySecureId(
                id,
                repository::findBySecureId,
                projection -> repository.findById(projection.getId()),
                "User not found"
        );
        UserManagementDetailResponse dto = new UserManagementDetailResponse();
        TreeUserManagementConverter converter =
                new TreeUserManagementConverter();
        converter.DetailResponse(data, dto);

        return dto;
    }

    @Override
    public void makeUserIsDeletedFalse(String id) {
        AppAdmin admin = GlobalConverter.getAdminEntity(adminRepository);
        AppUser data = HandlerRepository.getEntityBySecureId(id, repository, "User not found");
        AppUserAttribute userAttribute = data.getAppUserAttribute();
        userAttribute.setIsDeleted(false);
        userAttribute.setIsSuspended(false);
        data.setAppUserAttribute(userAttribute);

        GlobalConverter.CmsAdminUpdateAtBy(data, admin);
        repository.save(data);
    }

    @Override
    public void makeUserBulkRestoreTrue(Set<String> ids) {
        AppAdmin admin = GlobalConverter.getAdminEntity(adminRepository);
        Set<CMSBulkDeleteProjection> userProjections = repository.findBySecureIdIn(ids);

        userProjections.forEach(projection -> {
            AppUser data = repository.findById(projection.getId())
                    .orElseThrow(() -> new EntityNotFoundException("User not found"));
            AppUserAttribute userAttribute = data.getAppUserAttribute();
            userAttribute.setIsDeleted(false);
            userAttribute.setIsSuspended(false);
            data.setAppUserAttribute(userAttribute);

            GlobalConverter.CmsAdminUpdateAtBy(data, admin);
            repository.save(data);
        });
    }

}
