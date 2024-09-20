package com.bca.byc.service.impl;

import com.bca.byc.converter.PostCategoryDTOConverter;
import com.bca.byc.entity.PostCategory;
import com.bca.byc.exception.BadRequestException;
import com.bca.byc.model.PostCategoryCreateUpdateRequest;
import com.bca.byc.model.PostCategoryDetailResponse;
import com.bca.byc.repository.PostCategoryRepository;
import com.bca.byc.response.ResultPageResponseDTO;
import com.bca.byc.service.PostCategoryService;
import com.bca.byc.util.PaginationUtil;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class PostCategoryServiceImpl implements PostCategoryService {

    private PostCategoryRepository repository;
    private PostCategoryDTOConverter converter;

    @Override
    public PostCategoryDetailResponse findDataById(Long id) throws BadRequestException {
        PostCategory data = repository.findById(id)
                .orElseThrow(() -> new BadRequestException("PostCategory not found"));

        return converter.convertToListResponse(data);
    }

    @Override
    public List<PostCategoryDetailResponse> findAllData() {
        // Get the list
        List<PostCategory> datas = repository.findAll();

        // stream into the list
        return datas.stream()
                .map(converter::convertToListResponse)
                .collect(Collectors.toList());
    }

    @Override
    public void saveData(@Valid PostCategoryCreateUpdateRequest dto) throws BadRequestException {
        // set entity to add with model mapper
        PostCategory data = converter.convertToCreateRequest(dto);
        // save data
        repository.save(data);
    }

    @Override
    public void updateData(Long id, PostCategoryCreateUpdateRequest dto) throws BadRequestException {
        // check exist and get
        PostCategory data = repository.findById(id)
                .orElseThrow(() -> new BadRequestException("INVALID PostCategory ID"));

        // update
        converter.convertToUpdateRequest(data, dto);

        // update the updated_at
        data.setUpdatedAt(LocalDateTime.now());

        // save
        repository.save(data);
    }

    @Override
    public void deleteData(Long id) throws BadRequestException {
        // delete data
        if (!repository.existsById(id)) {
            throw new BadRequestException("PostCategory not found");
        } else {
            repository.deleteById(id);
        }
    }

    @Override
    public ResultPageResponseDTO<PostCategoryDetailResponse> listData(Integer pages, Integer limit, String sortBy, String direction, String keyword) {
        keyword = StringUtils.isEmpty(keyword) ? "%" : keyword + "%";
        Sort sort = Sort.by(new Sort.Order(PaginationUtil.getSortBy(direction), sortBy));
        Pageable pageable = PageRequest.of(pages, limit, sort);
        Page<PostCategory> pageResult = repository.findByNameLikeIgnoreCase(keyword, pageable);
        List<PostCategoryDetailResponse> dtos = pageResult.stream().map((c) -> {
            PostCategoryDetailResponse dto = converter.convertToListResponse(c);
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
