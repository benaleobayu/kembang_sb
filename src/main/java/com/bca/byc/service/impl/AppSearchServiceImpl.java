package com.bca.byc.service.impl;

import com.bca.byc.converter.AppSearchDTOConverter;
import com.bca.byc.entity.AppUser;
import com.bca.byc.entity.Post;
import com.bca.byc.exception.BadRequestException;
import com.bca.byc.model.AppSearchDetailResponse;
import com.bca.byc.repository.AppSearchRepository;
import com.bca.byc.repository.PostRepository;
import com.bca.byc.repository.auth.AppUserRepository;
import com.bca.byc.response.ResultPageResponseDTO;
import com.bca.byc.service.AppSearchService;
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
public class AppSearchServiceImpl implements AppSearchService {

    private final AppUserRepository appUserRepository;
    private final AppSearchRepository repository;

    private final PostRepository postRepository;

    private final AppSearchDTOConverter converter;

    @Override
    public ResultPageResponseDTO<AppSearchDetailResponse> listResultPosts(String email, Integer pages, Integer limit, String sortBy, String direction, String keyword) {
        // get user id
        AppUser user = appUserRepository.findByEmail(email)
                .orElseThrow(() -> new BadRequestException("User not found"));
        Long userId = user.getId();

        keyword = StringUtils.isEmpty(keyword) ? "%" : keyword + "%";
        Sort sort = Sort.by(new Sort.Order(PaginationUtil.getSortBy(direction), sortBy));
        Pageable pageable = PageRequest.of(pages, limit, sort);
//        Page<Post> pageResult = postRepository.findByTitleLikeIgnoreCase(keyword, pageable);
        Page<Post> pageResult = postRepository.findRandomPosts(keyword, pageable);

        assert pageResult != null;
        List<AppSearchDetailResponse> dtos = pageResult.stream().map((c) -> {
            AppSearchDetailResponse dto = converter.convertToListResponse(c);
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
    public ResultPageResponseDTO<AppSearchDetailResponse> listResultTags(String email, Integer pages, Integer limit, String sortBy, String direction, String keyword) {
        // get user id
        AppUser user = appUserRepository.findByEmail(email)
                .orElseThrow(() -> new BadRequestException("User not found"));
        Long userId = user.getId();

        keyword = StringUtils.isEmpty(keyword) ? "%" : keyword + "%";
        Sort sort = Sort.by(new Sort.Order(PaginationUtil.getSortBy(direction), sortBy));
        Pageable pageable = PageRequest.of(pages, limit, sort);
//        Page<Post> pageResult = postRepository.findByTitleLikeIgnoreCase(keyword, pageable);
        Page<Post> pageResult = postRepository.findRandomPosts(keyword, pageable);

        assert pageResult != null;
        List<AppSearchDetailResponse> dtos = pageResult.stream().map((c) -> {
            AppSearchDetailResponse dto = converter.convertToListResponse(c);
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
    public ResultPageResponseDTO<AppSearchDetailResponse> listResultAccounts(String email, Integer pages, Integer limit, String sortBy, String direction, String keyword) {
        // get user id
        AppUser user = appUserRepository.findByEmail(email)
                .orElseThrow(() -> new BadRequestException("User not found"));
        Long userId = user.getId();

        keyword = StringUtils.isEmpty(keyword) ? "%" : keyword + "%";
        Sort sort = Sort.by(new Sort.Order(PaginationUtil.getSortBy(direction), sortBy));
        Pageable pageable = PageRequest.of(pages, limit, sort);
//        Page<Post> pageResult = postRepository.findByTitleLikeIgnoreCase(keyword, pageable);
        Page<Post> pageResult = postRepository.findRandomPosts(keyword, pageable);

        assert pageResult != null;
        List<AppSearchDetailResponse> dtos = pageResult.stream().map((c) -> {
            AppSearchDetailResponse dto = converter.convertToListResponse(c);
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
