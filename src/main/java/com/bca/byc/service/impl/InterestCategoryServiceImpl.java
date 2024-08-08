package com.bca.byc.service.impl;

import com.bca.byc.convert.cms.InterestCategoryDTOConverter;
import com.bca.byc.entity.InterestCategory;
import com.bca.byc.exception.BadRequestException;
import com.bca.byc.model.cms.InterestCategoryCreateRequest;
import com.bca.byc.model.cms.InterestCategoryDetailResponse;
import com.bca.byc.model.cms.InterestCategoryUpdateRequest;
import com.bca.byc.repository.InterestCategoryRepository;
import com.bca.byc.service.InterestCategoryService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import javax.validation.Valid;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class InterestCategoryServiceImpl implements InterestCategoryService {

    private InterestCategoryRepository repository;

    private InterestCategoryDTOConverter converter;

    @Override
    public InterestCategoryDetailResponse findDataById(Long id) {
        InterestCategory data = repository.findById(id)
                .orElseThrow(() -> new BadRequestException("Interest category not found"));

        return converter.convertToListResponse(data);
    }

    @Override
    public List<InterestCategoryDetailResponse> findAllData() {

        List<InterestCategory> dtos = repository.findAll();

        return dtos.stream()
                .map((businessCategory -> converter.convertToListResponse(businessCategory)))
                .collect(Collectors.toList());
    }

    @Override
    public void saveData(@Valid InterestCategoryCreateRequest dto) throws Exception {

        InterestCategory data = converter.convertToCreateRequest(dto);

        repository.save(data);

    }

    @Override
    public void updateData(Long id, @Valid InterestCategoryUpdateRequest dto) {

        InterestCategory data = repository.findById(id)
                .orElseThrow(() -> new BadRequestException("Interest category not found"));

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
