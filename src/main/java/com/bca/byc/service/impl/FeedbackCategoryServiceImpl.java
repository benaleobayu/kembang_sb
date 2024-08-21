package com.bca.byc.service.impl;

import com.bca.byc.convert.FeedbackCategoryDTOConverter;
import com.bca.byc.entity.FeedbackCategory;
import com.bca.byc.exception.BadRequestException;
import com.bca.byc.model.FeedbackCategoryModelDTO;
import com.bca.byc.repository.FeedbackCategoryRepository;
import com.bca.byc.service.MsFeedbackCategoryService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class FeedbackCategoryServiceImpl implements MsFeedbackCategoryService {

    private FeedbackCategoryRepository repository;
    private FeedbackCategoryDTOConverter converter;

    @Override
    public FeedbackCategoryModelDTO.FeedbackCategoryDetailResponse findDataById(Long id) throws BadRequestException {
        FeedbackCategory data = repository.findById(id)
                .orElseThrow(() -> new BadRequestException("FeedbackCategory not found"));

        return converter.convertToListResponse(data);
    }

    @Override
    public List<FeedbackCategoryModelDTO.FeedbackCategoryDetailResponse> findAllData() {
        // Get the list
        List<FeedbackCategory> datas = repository.findAll();

        // stream into the list
        return datas.stream()
                .map(converter::convertToListResponse)
                .collect(Collectors.toList());
    }

    @Override
    public void saveData(@Valid FeedbackCategoryModelDTO.FeedbackCategoryCreateRequest dto) throws BadRequestException {
        // set entity to add with model mapper
        FeedbackCategory data = converter.convertToCreateRequest(dto);
        // save data
        repository.save(data);
    }

    @Override
    public void updateData(Long id, FeedbackCategoryModelDTO.FeedbackCategoryUpdateRequest dto) throws BadRequestException {
        // check exist and get
        FeedbackCategory data = repository.findById(id)
                .orElseThrow(() -> new BadRequestException("INVALID FeedbackCategory ID"));

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
            throw new BadRequestException("FeedbackCategory not found");
        } else {
            repository.deleteById(id);
        }
    }
}
