package com.bca.byc.service.impl;

import com.bca.byc.converter.AppUserDTOConverter;
import com.bca.byc.entity.AppUser;
import com.bca.byc.entity.AppUserAttribute;
import com.bca.byc.enums.StatusType;
import com.bca.byc.exception.BadRequestException;
import com.bca.byc.model.UserManagementDetailResponse;
import com.bca.byc.repository.UserInquiryRepository;
import com.bca.byc.response.ResultPageResponseDTO;
import com.bca.byc.service.UserInquiryService;
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
public class UserInquiryServiceImpl implements UserInquiryService {

    private final UserInquiryRepository userInquiryRepository;
    private final AppUserDTOConverter converter;

    @Override
    public UserManagementDetailResponse findDataById(Long id) throws BadRequestException {
        AppUser user = userInquiryRepository.findById(id)
                .orElseThrow(() -> new BadRequestException("User not found"));
        return converter.convertToListInquiry(user);
    }

    @Override
    public void softDeleteData(Long id) throws BadRequestException {
        AppUser user = userInquiryRepository.findById(id)
                .orElseThrow(() -> new BadRequestException("User not found"));

        AppUserAttribute userAttribute = user.getAppUserAttribute();

        userAttribute.setIsDeleted(!userAttribute.getIsDeleted());

        user.setAppUserAttribute(userAttribute);
        userInquiryRepository.save(user);

    }

    @Override
    public ResultPageResponseDTO<UserManagementDetailResponse> listData(Integer pages, Integer limit, String sortBy, String direction, String userName) {
        userName = StringUtils.isEmpty(userName) ? "%" : userName + "%";
        Sort sort = Sort.by(new Sort.Order(PaginationUtil.getSortBy(direction), sortBy));
        Pageable pageable = PageRequest.of(pages, limit, sort);
        Page<AppUser> pageResult = userInquiryRepository.findByNameLikeIgnoreCaseAndAppUserDetailStatus(userName, StatusType.REJECTED, pageable);
        List<UserManagementDetailResponse> dtos = pageResult.stream().map((c) -> {
            UserManagementDetailResponse dto = converter.convertToListInquiry(c);
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
}
