package com.bca.byc.service.impl;

import com.bca.byc.converter.ExpectCategoryDTOConverter;
import com.bca.byc.converter.dictionary.PageCreateReturn;
import com.bca.byc.entity.ExpectCategory;
import com.bca.byc.exception.BadRequestException;
import com.bca.byc.model.ExpectCategoryCreateUpdateRequest;
import com.bca.byc.model.ExpectCategoryDetailResponse;
import com.bca.byc.model.ExpectCategoryIndexResponse;
import com.bca.byc.repository.ExpectCategoryRepository;
import com.bca.byc.repository.handler.HandlerRepository;
import com.bca.byc.response.ResultPageResponseDTO;
import com.bca.byc.service.ExpectCategoryService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import com.bca.byc.util.PaginationUtil;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.util.StringUtils;

@Service
@AllArgsConstructor
public class ExpectCategoryServiceImpl implements ExpectCategoryService {

    private ExpectCategoryRepository repository;
    private ExpectCategoryDTOConverter converter;

    @Override
        public ResultPageResponseDTO<ExpectCategoryIndexResponse> listDataExpectCategory(Integer pages, Integer limit, String sortBy, String direction, String keyword) {
            keyword = StringUtils.isEmpty(keyword) ? "%" : keyword + "%";
            Sort sort = Sort.by(new Sort.Order(PaginationUtil.getSortBy(direction), sortBy));
            Pageable pageable = PageRequest.of(pages, limit, sort);
            Page<ExpectCategory> pageResult = repository.findByNameLikeIgnoreCase(keyword, pageable);
            List<ExpectCategoryIndexResponse> dtos = pageResult.stream().map((c) -> {
                ExpectCategoryIndexResponse dto = converter.convertToListResponse(c);
                return dto;
            }).collect(Collectors.toList());

            return PageCreateReturn.create(pageResult, dtos);
        }

    @Override
    public ExpectCategoryDetailResponse findDataBySecureId(String id) throws BadRequestException {
        ExpectCategory data = HandlerRepository.getEntityBySecureId(id, repository, "User not found");

        return converter.convertToDetailResponse(data);
    }

    @Override
    public List<ExpectCategoryIndexResponse> findAllData() {
        // Get the list
        List<ExpectCategory> datas = repository.findAll();

        // stream into the list
        return datas.stream()
                .map(converter::convertToListResponse)
                .collect(Collectors.toList());
    }

    @Override
    public void saveData(@Valid ExpectCategoryCreateUpdateRequest dto) throws BadRequestException {
        // set entity to add with model mapper
        ExpectCategory data = converter.convertToCreateRequest(dto);
        // save data
        repository.save(data);
    }

    @Override
    public void updateData(String id, @Valid ExpectCategoryCreateUpdateRequest dto) throws BadRequestException {
        // check exist and get
        ExpectCategory data = HandlerRepository.getEntityBySecureId(id, repository, "User not found");

        // update
        converter.convertToUpdateRequest(data, dto);

        // update the updated_at
        data.setUpdatedAt(LocalDateTime.now());

        // save
        repository.save(data);
    }

    @Override
    public void deleteData(String id) throws BadRequestException {
        ExpectCategory data = HandlerRepository.getEntityBySecureId(id, repository, "User not found");
        Long expectId = data.getId();
        // delete data
        if (!repository.existsById(expectId)) {
            throw new BadRequestException("ExpectCategory not found");
        } else {
            repository.deleteById(expectId);
        }
    }
}
