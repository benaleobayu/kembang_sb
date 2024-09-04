package com.bca.byc.service.impl;

import com.bca.byc.converter.ExpectCategoryDTOConverter;
import com.bca.byc.entity.ExpectCategory;
import com.bca.byc.exception.BadRequestException;
import com.bca.byc.model.ExpectCategoryCreateRequest;
import com.bca.byc.model.ExpectCategoryDetailResponse;
import com.bca.byc.model.ExpectCategoryUpdateRequest;
import com.bca.byc.repository.ExpectCategoryRepository;
import com.bca.byc.service.ExpectCategoryService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class ExpectCategoryServiceImpl implements ExpectCategoryService {

    private ExpectCategoryRepository repository;
    private ExpectCategoryDTOConverter converter;

    @Override
    public ExpectCategoryDetailResponse findDataById(Long id) throws BadRequestException {
        ExpectCategory data = repository.findById(id)
                .orElseThrow(() -> new BadRequestException("ExpectCategory not found"));

        return converter.convertToListResponse(data);
    }

    @Override
    public List<ExpectCategoryDetailResponse> findAllData() {
        // Get the list
        List<ExpectCategory> datas = repository.findAll();

        // stream into the list
        return datas.stream()
                .map(converter::convertToListResponse)
                .collect(Collectors.toList());
    }

    @Override
    public void saveData(@Valid ExpectCategoryCreateRequest dto) throws BadRequestException {
        // set entity to add with model mapper
        ExpectCategory data = converter.convertToCreateRequest(dto);
        // save data
        repository.save(data);
    }

    @Override
    public void updateData(Long id, ExpectCategoryUpdateRequest dto) throws BadRequestException {
        // check exist and get
        ExpectCategory data = repository.findById(id)
                .orElseThrow(() -> new BadRequestException("INVALID ExpectCategory ID"));

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
            throw new BadRequestException("ExpectCategory not found");
        } else {
            repository.deleteById(id);
        }
    }
}
