package com.bca.byc.service.impl;
import com.bca.byc.converter.dictionary.PageCreateReturn;
import com.bca.byc.model.*;
import com.bca.byc.util.PaginationUtil;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.jsoup.Jsoup;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import com.bca.byc.converter.BusinessCategoryDTOConverter;
import com.bca.byc.entity.BusinessCategory;
import com.bca.byc.exception.BadRequestException;
import com.bca.byc.repository.BusinessCategoryRepository;
import com.bca.byc.repository.handler.HandlerRepository;
import com.bca.byc.response.ResultPageResponseDTO;
import com.bca.byc.service.BusinessCategoryService;

@Service
@AllArgsConstructor
public class BusinessCategoryServiceImpl implements BusinessCategoryService {

    private BusinessCategoryRepository repository;
    private BusinessCategoryDTOConverter converter;


    @Override
    public List<BusinessCategoryListResponse> findByParentIdIsNotNull() {
        // Get the list
        List<BusinessCategory> datas = repository.findByParentIdIsNotNull();
        for (BusinessCategory item : datas) {
            if (item.getDescription() != null) {
                String cleanDescription = Jsoup.parse(item.getDescription()).text();
                item.setDescription(cleanDescription);
            }
        }

        // stream into the list
        return datas.stream()
                .map(converter::convertToListResponse)
                .collect(Collectors.toList());
    }

    @Override
    public List<BusinessCategoryListResponse> findByParentIdIsNull() {
        // Get the list
        List<BusinessCategory> datas = repository.findByParentIdIsNull();

        // stream into the list
        return datas.stream()
                .map(converter::convertToListResponse)
                .collect(Collectors.toList());
    }

    @Override
    public ResultPageResponseDTO<BusinessCategoryListResponse> listDataBusinessCategory(Integer pages, Integer limit, String sortBy, String direction, String keyword) {
        keyword = StringUtils.isEmpty(keyword) ? "%" : keyword + "%";
        Sort sort = Sort.by(new Sort.Order(PaginationUtil.getSortBy(direction), sortBy));
        Pageable pageable = PageRequest.of(pages, limit, sort);
        Page<BusinessCategory> pageResult = repository.findDataByKeyword(keyword, pageable);
        List<BusinessCategoryListResponse> dtos = pageResult.stream().map((c) -> {
            BusinessCategoryListResponse dto = converter.convertToListResponse(c);
            return dto;
        }).collect(Collectors.toList());

        return PageCreateReturn.create(pageResult, dtos);
    }

    @Override
    public BusinessCategoryListResponse findDataBySecureId(String id) {
        BusinessCategory data = HandlerRepository.getEntityBySecureId(
                id, repository, "Business category not found"
        );
        return converter.convertToListResponse(data);
    }

    @Override
    public void saveData(@Valid BusinessCategoryParentCreateRequest dto) throws BadRequestException {
        // set entity to add with model mapper
        BusinessCategory data = converter.convertToCreateParentRequest(dto);
        // save data
        repository.save(data);
    }

    @Override
    public void updateData(String id, @Valid BusinessCategoryUpdateRequest dto) throws BadRequestException {
        // check exist and get
        BusinessCategory data = HandlerRepository.getEntityBySecureId(
                id, repository, "Business category not found"
        );

        // update
        converter.convertToUpdateRequest(data, dto);

        // update the updated_at
        data.setUpdatedAt(LocalDateTime.now());

        // save
        repository.save(data);
    }

    @Override
    public void deleteData(String id) throws BadRequestException {
        BusinessCategory data = HandlerRepository.getEntityBySecureId(
                id, repository, "Business category not found"
        );
        // delete data
        if (!repository.existsById(data.getId())) {
            throw new BadRequestException("Business category not found");
        } else {
            repository.deleteById(data.getId());
        }
    }


}
