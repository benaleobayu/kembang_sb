package com.bca.byc.service.impl;

import com.bca.byc.convert.cms.BusinessCategoryDTOConverter;
import com.bca.byc.entity.BusinessCategory;
import com.bca.byc.exception.BadRequestException;
import com.bca.byc.model.cms.BusinessCategoryCreateRequest;
import com.bca.byc.model.cms.BusinessCategoryDetailResponse;
import com.bca.byc.model.cms.BusinessCategoryUpdateRequest;
import com.bca.byc.repository.BusinessCategoryRepository;
import com.bca.byc.service.BusinessCategoryService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import javax.validation.Valid;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class BusinessCategoryServiceImpl implements BusinessCategoryService {

    private BusinessCategoryRepository repository;

    private BusinessCategoryDTOConverter converter;

    @Override
    public BusinessCategoryDetailResponse findDataById(Long id) {
        BusinessCategory data = repository.findById(id)
                .orElseThrow(() -> new BadRequestException("Business category not found"));

        return converter.convertToListResponse(data);
    }

    @Override
    public List<BusinessCategoryDetailResponse> findAllData() {

        List<BusinessCategory> dtos = repository.findAll();

        return dtos.stream()
                .map((businessCategory -> converter.convertToListResponse(businessCategory)))
                .collect(Collectors.toList());
    }

    @Override
    public void saveData(@Valid BusinessCategoryCreateRequest dto) throws Exception {

        BusinessCategory data = converter.convertToCreateRequest(dto);

        repository.save(data);

    }

    @Override
    public void updateData(Long id, @Valid BusinessCategoryUpdateRequest dto) {

        BusinessCategory data = repository.findById(id)
                .orElseThrow(() -> new BadRequestException("Business category not found"));

        // update
        converter.convertToUpdateRequest(data, dto);

        // set updatedAt
        data.setUpdatedAt(LocalDateTime.now());

        // save
        repository.save(data);
    }

    @Override
    public void deleteData(Long id) {

        repository.deleteById(id);
    }
}
