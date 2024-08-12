package com.bca.byc.service.impl;

import com.bca.byc.convert.BusinessCategoryDTOConverter;
import com.bca.byc.entity.BusinessCategory;
import com.bca.byc.exception.BadRequestException;
import com.bca.byc.model.cms.BusinessCategoryModelDTO;
import com.bca.byc.repository.BusinessCategoryRepository;
import com.bca.byc.service.BusinessCategoryService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class BusinessCategoryServiceImpl implements BusinessCategoryService {

    private BusinessCategoryRepository repository;
    private BusinessCategoryDTOConverter converter;

    @Override
    public BusinessCategoryModelDTO.DetailResponse findDataById(Long id) throws BadRequestException {
        BusinessCategory data = repository.findById(id)
                .orElseThrow(() -> new BadRequestException("BusinessCategory not found"));

        return converter.convertToListResponse(data);
    }

    @Override
    public List<BusinessCategoryModelDTO.DetailResponse> findAllData() {
        // Get the list
        List<BusinessCategory> datas = repository.findAll();

        // stream into the list
        return datas.stream()
                .map(converter::convertToListResponse)
                .collect(Collectors.toList());
    }

    @Override
    public void saveData(@Valid BusinessCategoryModelDTO.CreateRequest dto) throws BadRequestException {
        // set entity to add with model mapper
        BusinessCategory data = converter.convertToCreateRequest(dto);
        // save data
        repository.save(data);
    }

    @Override
    public void updateData(Long id, BusinessCategoryModelDTO.UpdateRequest dto) throws BadRequestException {
        // check exist and get
        BusinessCategory data = repository.findById(id)
                .orElseThrow(() -> new BadRequestException("INVALID BusinessCategory ID"));

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
            throw new BadRequestException("BusinessCategory not found");
        } else {
            repository.deleteById(id);
        }
    }
}
