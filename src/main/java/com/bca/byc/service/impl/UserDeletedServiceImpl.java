package com.bca.byc.service.impl;

import com.bca.byc.converter.UserDeletedDTOConverter;
import com.bca.byc.entity.AppUser;
import com.bca.byc.entity.AppUserAttribute;
import com.bca.byc.exception.BadRequestException;
import com.bca.byc.model.UserManagementDetailResponse;
import com.bca.byc.repository.UserDeletedRepository;
import com.bca.byc.response.ResultPageResponseDTO;
import com.bca.byc.service.UserDeletedService;
import com.bca.byc.util.PaginationUtil;
import lombok.RequiredArgsConstructor;
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

@Service
@RequiredArgsConstructor
public class UserDeletedServiceImpl implements UserDeletedService {

    private final UserDeletedRepository repository;

    private final UserDeletedDTOConverter converter;

    @Override
    public ResultPageResponseDTO<UserManagementDetailResponse> listData(Integer pages,
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

        Page<AppUser> pageResult = repository.findByKeywordAndStatusAndDeletedAndCreatedAt(keyword, locationId, start, end, pageable);
        List<UserManagementDetailResponse> dtos = pageResult.stream().map((c) -> {
            UserManagementDetailResponse dto = converter.convertToListResponse(c);
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

        return converter.convertToListResponse(data);
    }

    @Override
    public void makeUserIsDeletedFalse(Long id) {
        AppUser user = repository.findById(id)
                .orElseThrow(() -> new BadRequestException("User not found"));
        AppUserAttribute userAttribute = user.getAppUserAttribute();
        userAttribute.setIsDeleted(false);
        user.setAppUserAttribute(userAttribute);
        repository.save(user);
    }

    @Override
    public void makeUserBulkRestoreTrue(Set<Long> ids) {
        repository.findByIdIn(ids).forEach(user -> {
            AppUserAttribute userAttribute = user.getAppUserAttribute();
            userAttribute.setIsDeleted(false);
            user.setAppUserAttribute(userAttribute);
            repository.save(user);
        });
    }
}
