package com.bca.byc.service.impl;

import com.bca.byc.convert.cms.FeedbackCategoryDTOConverter;
import com.bca.byc.entity.FeedbackCategory;
import com.bca.byc.exception.BadRequestException;
import com.bca.byc.model.cms.FeedbackCategoryCreateRequest;
import com.bca.byc.model.cms.FeedbackCategoryDetailResponse;
import com.bca.byc.model.cms.FeedbackCategoryUpdateRequest;
import com.bca.byc.repository.FeedbackCategoryRepository;
import com.bca.byc.service.FeedbackCategoryService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import javax.validation.Valid;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class FeedbackCategoryServiceImpl implements FeedbackCategoryService {

    private FeedbackCategoryRepository repository;

    private FeedbackCategoryDTOConverter converter;

    @Override
    public FeedbackCategoryDetailResponse findDataById(Long id) {
        FeedbackCategory data = repository.findById(id)
                .orElseThrow(() -> new BadRequestException("Feedback category not found"));

        return converter.convertToListResponse(data);
    }

    @Override
    public List<FeedbackCategoryDetailResponse> findAllData() {

        List<FeedbackCategory> dtos = repository.findAll();

        return dtos.stream()
                .map((businessCategory -> converter.convertToListResponse(businessCategory)))
                .collect(Collectors.toList());
    }

    @Override
    public void saveData(@Valid FeedbackCategoryCreateRequest dto) throws Exception {

        FeedbackCategory data = converter.convertToCreateRequest(dto);

        repository.save(data);

    }

    @Override
    public void updateData(Long id, @Valid FeedbackCategoryUpdateRequest dto) {

        FeedbackCategory data = repository.findById(id)
                .orElseThrow(() -> new BadRequestException("Feedback category not found"));

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
