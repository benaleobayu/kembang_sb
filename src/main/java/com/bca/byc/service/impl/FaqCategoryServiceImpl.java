package com.bca.byc.service.impl;

import com.bca.byc.convert.FaqCategoryDTOConverter;
import com.bca.byc.entity.FaqCategory;
import com.bca.byc.exception.BadRequestException;
import com.bca.byc.model.cms.FaqCategoryModelDTO;
import com.bca.byc.repository.FaqCategoryRepository;
import com.bca.byc.service.FaqCategoryService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class FaqCategoryServiceImpl implements FaqCategoryService {

    private FaqCategoryRepository repository;
    private FaqCategoryDTOConverter converter;

    @Override
    public FaqCategoryModelDTO.FaqCategoryDetailResponse findDataById(Long id) throws BadRequestException {
        FaqCategory data = repository.findById(id)
                .orElseThrow(() -> new BadRequestException("FaqCategory not found"));

        return converter.convertToListResponse(data);
    }

    @Override
    public List<FaqCategoryModelDTO.FaqCategoryDetailResponse> findAllData() {
        // Get the list
        List<FaqCategory> datas = repository.findAll();

        // stream into the list
        return datas.stream()
                .map(converter::convertToListResponse)
                .collect(Collectors.toList());
    }

    @Override
    public void saveData(@Valid FaqCategoryModelDTO.FaqCategoryCreateRequest dto) throws BadRequestException {
        // set entity to add with model mapper
        FaqCategory data = converter.convertToCreateRequest(dto);
        // save data
        repository.save(data);
    }

    @Override
    public void updateData(Long id, FaqCategoryModelDTO.FaqCategoryUpdateRequest dto) throws BadRequestException {
        // check exist and get
        FaqCategory data = repository.findById(id)
                .orElseThrow(() -> new BadRequestException("INVALID FaqCategory ID"));

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
            throw new BadRequestException("FaqCategory not found");
        } else {
            repository.deleteById(id);
        }
    }
}
