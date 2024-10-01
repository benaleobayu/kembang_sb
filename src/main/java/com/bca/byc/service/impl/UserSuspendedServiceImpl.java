package com.bca.byc.service.impl;

import com.bca.byc.converter.UserSuspendedDTOConverter;
import com.bca.byc.entity.AppUser;
import com.bca.byc.entity.AppUserAttribute;
import com.bca.byc.exception.BadRequestException;
import com.bca.byc.model.UserManagementDetailResponse;
import com.bca.byc.model.UserManagementListResponse;
import com.bca.byc.model.projection.CMSBulkDeleteProjection;
import com.bca.byc.model.projection.CMSBulkSuspendProjection;
import com.bca.byc.repository.UserSuspendedRepository;
import com.bca.byc.repository.handler.HandlerRepository;
import com.bca.byc.response.ResultPageResponseDTO;
import com.bca.byc.service.UserSuspendedService;
import com.bca.byc.util.PaginationUtil;
import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static com.bca.byc.converter.parsing.TreeUserManagementConverter.IndexResponse;


@Service
@AllArgsConstructor
public class UserSuspendedServiceImpl implements UserSuspendedService {

    private final UserSuspendedRepository repository;

    private final UserSuspendedDTOConverter converter;

    @Override
    public ResultPageResponseDTO<UserManagementListResponse> listData(Integer pages,
                                                                      Integer limit,
                                                                      String sortBy,
                                                                      String direction,
                                                                      String keyword,
                                                                      Long locationId,
                                                                      LocalDate startDate,
                                                                      LocalDate endDate) {
        keyword = StringUtils.isEmpty(keyword) ? "%" : keyword + "%";
        Sort sort = Sort.by(new Sort.Order(PaginationUtil.getSortBy(direction), sortBy));
        Pageable pageable = PageRequest.of(pages, limit, sort);

        // set date
        LocalDateTime start = (startDate == null) ? LocalDateTime.of(1970, 1, 1, 0, 0) : startDate.atStartOfDay();
        LocalDateTime end = (endDate == null) ? LocalDateTime.now() : endDate.atTime(23, 59, 59);

        Page<AppUser> pageResult = repository.findByKeywordAndStatusAndSuspendedAndCreatedAt(keyword, locationId, start, end, pageable);
        List<UserManagementListResponse> dtos = pageResult.stream().map((c) -> {
            UserManagementListResponse dto = new UserManagementListResponse();
            IndexResponse(c,dto);
            return dto;
        }).collect(Collectors.toList());

        int currentPage = pageResult.getNumber() + 1;
        int totalPages = pageResult.getTotalPages();

        return PaginationUtil.createResultPageDTO(
                pageResult.getTotalElements(), // total items
                dtos,
                currentPage, // current page
                currentPage > 1 ? currentPage - 1 : 1, // prev page
                currentPage < totalPages - 1 ? currentPage + 1 : totalPages - 1, // next page
                1, // first page
                totalPages - 1, // last page
                pageResult.getSize() // per page
        );
    }

    @Override
    public UserManagementDetailResponse findDataBySecureId(String id) throws BadRequestException {
        AppUser data = HandlerRepository.getEntityBySecureId(id, repository, "User not found");

        return converter.convertToListResponse(data);
    }

    @Override
    public void makeUserIsDeletedTrue(String id) {
        AppUser user = HandlerRepository.getEntityBySecureId(id, repository, "User not found");
        AppUserAttribute userAttribute = user.getAppUserAttribute();
        userAttribute.setIsDeleted(true);
        user.setAppUserAttribute(userAttribute);
        repository.save(user);
    }

    @Override
    public void makeUserIsSuspendedFalse(String id) {
        AppUser user = HandlerRepository.getEntityBySecureId(id, repository, "User not found");
        AppUserAttribute userAttribute = user.getAppUserAttribute();
        userAttribute.setIsSuspended(false);
        user.setAppUserAttribute(userAttribute);
        repository.save(user);
    }

    @Override
    public void makeUserBulkDeleteTrue(Set<String> ids) {
        Set<CMSBulkDeleteProjection> userProjections = repository.findToDeleteBySecureIdIn(ids);

        userProjections.forEach(projection -> {
            AppUser user = repository.findById(projection.getId())
                    .orElseThrow(() -> new EntityNotFoundException("User not found"));
            AppUserAttribute userAttribute = user.getAppUserAttribute();
            userAttribute.setIsDeleted(true);
            user.setAppUserAttribute(userAttribute);
            repository.save(user);
        });
    }

    @Override
    public void makeUserBulkSuspendedFalse(Set<String> ids) {
        Set<CMSBulkSuspendProjection> userProjections = repository.findToSuspendBySecureIdIn(ids);

        userProjections.forEach(projection -> {
            AppUser user = repository.findById(projection.getId())
                    .orElseThrow(() -> new EntityNotFoundException("User not found"));
            AppUserAttribute userAttribute = user.getAppUserAttribute();
            userAttribute.setIsSuspended(false);
            user.setAppUserAttribute(userAttribute);
            repository.save(user);
        });
    }
}
