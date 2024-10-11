package com.bca.byc.service.cms.impl;

import com.bca.byc.converter.AppUserDTOConverter;
import com.bca.byc.converter.parsing.GlobalConverter;
import com.bca.byc.entity.AppUser;
import com.bca.byc.model.Elastic.AppUserElastic;
import com.bca.byc.model.UserManagementDetailResponse;
import com.bca.byc.model.search.ListOfFilterPagination;
import com.bca.byc.model.search.SavedKeywordAndPageable;
import com.bca.byc.repository.Elastic.AppUserElasticRepository;
import com.bca.byc.repository.auth.AppUserRepository;
import com.bca.byc.response.ResultPageResponseDTO;
import com.bca.byc.service.cms.CmsUserService;
import com.bca.byc.util.PaginationUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CmsUserServiceImpl implements CmsUserService {

    private final AppUserRepository repository;
    private final AppUserElasticRepository elasticRepository;

    private final AppUserDTOConverter converter;

    @Override
    public com.bca.byc.response.Page<AppUserElastic> getAllUsers(Integer page, Integer size) {
        Pageable pageable = PageRequest.of(page, size);
        var userList = elasticRepository.findAllBy(pageable);
        return new com.bca.byc.response.Page<>(
                userList.stream().collect(Collectors.toList()),
                pageable,
                userList.getTotalElements()
        );
    }

    @Override
    public Long countAllUsers() {
        return repository.count();
    }

    @Override
    public Long getElasticCount() {
        return elasticRepository.count();
    }

    @Override
    public ResultPageResponseDTO<UserManagementDetailResponse> listData(Integer pages, Integer limit, String sortBy, String direction, String keyword) {
        ListOfFilterPagination filter = new ListOfFilterPagination(
                keyword
        );
        SavedKeywordAndPageable set = GlobalConverter.createPageable(pages, limit, sortBy, direction, keyword, filter);

        Page<AppUser> pageResult = repository.findByNameLikeIgnoreCase(set.keyword(), set.pageable());
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
