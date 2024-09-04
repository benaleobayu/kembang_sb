package com.bca.byc.service.impl;

import com.bca.byc.converter.BusinessCategoryDTOConverter;
import com.bca.byc.entity.BusinessCategory;
import com.bca.byc.exception.BadRequestException;
import com.bca.byc.model.BusinessCategoryCreateRequest;
import com.bca.byc.model.BusinessCategoryDetailResponse;
import com.bca.byc.model.BusinessCategoryUpdateRequest;
import com.bca.byc.repository.BusinessCategoryRepository;
import com.bca.byc.service.MsBusinessCategoryService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.jsoup.Jsoup;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class BusinessCategoryServiceImpl implements MsBusinessCategoryService {

    private BusinessCategoryRepository repository;
    private BusinessCategoryDTOConverter converter;

    @Override
    public BusinessCategoryDetailResponse findDataById(Long id) throws BadRequestException {
        BusinessCategory data = repository.findById(id)
                .orElseThrow(() -> new BadRequestException("BusinessCategory not found"));

        return converter.convertToListResponse(data);
    }

    @Override
    public List<BusinessCategoryDetailResponse> findByParentIdIsNull() {
        // Get the list
        List<BusinessCategory> datas = repository.findByParentIdIsNull();

        // stream into the list
        return datas.stream()
                .map(converter::convertToListResponse)
                .collect(Collectors.toList());
    }

    @Override
    public List<BusinessCategoryDetailResponse> findByParentIdIsNotNull() {
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
    public List<BusinessCategoryDetailResponse> findAllData() {
        // Get the list
        List<BusinessCategory> datas = repository.findAll();
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
    public List<BusinessCategoryDetailResponse> findByParent(BusinessCategory parentId) {
        List<BusinessCategory> datas = repository.findByParentId(parentId);

        return datas.stream()
                .map(converter::convertToListResponse)
                .collect(Collectors.toList());
    }

    @Override
    public void saveData(@Valid BusinessCategoryCreateRequest dto) throws BadRequestException {
        // set entity to add with model mapper
        BusinessCategory data = converter.convertToCreateRequest(dto);
        // ensure parent_id is null
        if (dto.getCheckParentId() != null) {
            throw new BadRequestException("Parent ID must be null for a parent category.");
        }
        // save data
        repository.save(data);
    }

    @Override
    public void saveDataChild(Long id, @Valid BusinessCategoryCreateRequest dto) throws BadRequestException {

        // ensure parent_id is not null
        if (dto.getCheckParentId() == null) {
            throw new BadRequestException("Parent ID must not be null for a child category.");
        }

        // set entity to add with model mapper
        BusinessCategory data = converter.convertToCreateRequest(dto);

        data.setId(null);
        // find parent
        BusinessCategory parent = repository.findById(id)
                .orElseThrow(() -> new BadRequestException("Parent category not found with ID: " + id));
        // Set parent category
        data.setParentId(parent);
        // save data
        repository.save(data);
    }

    @Override
    public void updateData(Long id, BusinessCategoryUpdateRequest dto) throws BadRequestException {
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
