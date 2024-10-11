package com.bca.byc.service.impl;

import com.bca.byc.converter.TagDTOConverter;
import com.bca.byc.converter.parsing.GlobalConverter;
import com.bca.byc.entity.Tag;
import com.bca.byc.exception.BadRequestException;
import com.bca.byc.model.data.TagCreateUpdateRequest;
import com.bca.byc.model.data.TagDetailResponse;
import com.bca.byc.model.search.ListOfFilterPagination;
import com.bca.byc.model.search.SavedKeywordAndPageable;
import com.bca.byc.repository.TagRepository;
import com.bca.byc.response.ResultPageResponseDTO;
import com.bca.byc.service.TagService;
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
public class TagServiceImpl implements TagService {

    private TagRepository repository;
    private TagDTOConverter converter;

    @Override
    public ResultPageResponseDTO<TagDetailResponse> listDataTag(Integer pages, Integer limit, String sortBy, String direction, String keyword) {
        ListOfFilterPagination filter = new ListOfFilterPagination(
                keyword
        );
        SavedKeywordAndPageable set = GlobalConverter.createPageable(pages, limit, sortBy, direction, keyword, filter);

        Page<Tag> pageResult = repository.findByNameLikeIgnoreCase(set.keyword(), set.pageable());
        List<TagDetailResponse> dtos = pageResult.stream().map((c) -> {
            TagDetailResponse dto = converter.convertToListResponse(c);
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
    public ResultPageResponseDTO<TagDetailResponse> listDataTagApps(Integer pages, Integer limit, String sortBy, String direction, String keyword) {
        keyword = StringUtils.isEmpty(keyword) ? "%" : keyword + "%";
        Sort sort = Sort.by(new Sort.Order(PaginationUtil.getSortBy(direction), sortBy));
        Pageable pageable = PageRequest.of(pages, limit, sort);
        Page<Tag> pageResult = repository.findByNameLikeIgnoreCase(keyword, pageable);
        List<TagDetailResponse> dtos = pageResult.stream().map((c) -> {
            TagDetailResponse dto = converter.convertToListResponse(c);
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
    public TagDetailResponse findDataById(Long id) throws BadRequestException {
        Tag data = repository.findById(id)
                .orElseThrow(() -> new BadRequestException("Tag not found"));

        return converter.convertToListResponse(data);
    }


    @Override
    public void saveData(@Valid TagCreateUpdateRequest dto) throws BadRequestException {
        // set entity to add with model mapper
        Tag data = converter.convertToCreateRequest(dto);
        // save data
        repository.save(data);
    }

    @Override
    public void updateData(Long id, TagCreateUpdateRequest dto) throws BadRequestException {
        // check exist and get
        Tag data = repository.findById(id)
                .orElseThrow(() -> new BadRequestException("INVALID Tag ID"));

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
            throw new BadRequestException("Tag not found");
        } else {
            repository.deleteById(id);
        }
    }
}
