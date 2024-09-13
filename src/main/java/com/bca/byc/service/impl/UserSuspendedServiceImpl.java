package com.bca.byc.service.impl;

import com.bca.byc.converter.AppUserDTOConverter;
import com.bca.byc.entity.AppUser;
import com.bca.byc.entity.AppUserAttribute;
import com.bca.byc.enums.StatusType;
import com.bca.byc.exception.BadRequestException;
import com.bca.byc.model.UserManagementDetailResponse;
import com.bca.byc.repository.UserSuspendedRepository;
import com.bca.byc.response.ResultPageResponseDTO;
import com.bca.byc.service.UserSuspendedService;
import com.bca.byc.util.PaginationUtil;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.stream.Collectors;


@Service
@AllArgsConstructor
public class UserSuspendedServiceImpl implements UserSuspendedService {

    private final UserSuspendedRepository repository;

    private final AppUserDTOConverter converter;

    @Override
    public ResultPageResponseDTO<UserManagementDetailResponse> listData(Integer pages, Integer limit, String sortBy, String direction, String userName) {
        userName = StringUtils.isEmpty(userName) ? "%" : userName + "%";
        Sort sort = Sort.by(new Sort.Order(PaginationUtil.getSortBy(direction), sortBy));
        Pageable pageable = PageRequest.of(pages, limit, sort);
        Page<AppUser> pageResult = repository.findByNameLikeIgnoreCaseAndAppUserDetailStatusAndAppUserAttributeIsSuspendedTrue(userName, StatusType.ACTIVATED, pageable);
        List<UserManagementDetailResponse> dtos = pageResult.stream().map((c) -> {
            UserManagementDetailResponse dto = converter.convertToDetailResponse(c);
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
    public UserManagementDetailResponse findDataById(Long id) throws BadRequestException {
        AppUser data = repository.findById(id)
                        .orElseThrow(() -> new BadRequestException("user not found"));

                return converter.convertToDetailResponse(data);
    }

    @Override
    public void makeUserIsDeletedTrue(Long id) {
        AppUser user = repository.findById(id)
                .orElseThrow(() -> new BadRequestException("User not found"));
        AppUserAttribute userAttribute = user.getAppUserAttribute();
        userAttribute.setIsDeleted(true);
        user.setAppUserAttribute(userAttribute);
        repository.save(user);
    }

    @Override
    public void makeUserIsSuspendedFalse(Long id) {
        AppUser user = repository.findById(id)
                .orElseThrow(() -> new BadRequestException("User not found"));
        AppUserAttribute userAttribute = user.getAppUserAttribute();
        userAttribute.setIsSuspended(false);
        user.setAppUserAttribute(userAttribute);
        repository.save(user);
    }
}
