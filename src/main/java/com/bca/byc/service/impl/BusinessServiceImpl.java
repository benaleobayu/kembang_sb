package com.bca.byc.service.impl;

import com.bca.byc.convert.BusinessCategoryDTOConverter;
import com.bca.byc.convert.BusinessDTOConverter;
import com.bca.byc.entity.Business;
import com.bca.byc.exception.BadRequestException;
import com.bca.byc.model.BusinessCategoryModelDTO;
import com.bca.byc.model.BusinessCreateRequest;
import com.bca.byc.model.BusinessDetailResponse;
import com.bca.byc.model.BusinessUpdateRequest;
import com.bca.byc.repository.BusinessRepository;
import com.bca.byc.service.BusinessService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class BusinessServiceImpl implements BusinessService {

    private BusinessRepository repository;
    private BusinessDTOConverter converter;
    private BusinessCategoryDTOConverter categoryConverter;


    @Override
    public BusinessDetailResponse findDataById(Long id) throws BadRequestException {
        Business data = repository.findById(id)
                .orElseThrow(() -> new BadRequestException("Business not found"));
        BusinessDetailResponse dto = converter.convertToListResponse(data);


        Set<BusinessCategoryModelDTO.BusinessCategoryDetailResponse> categoryDTOs = dto.getCategories().stream()
                .map(category -> new BusinessCategoryModelDTO.BusinessCategoryDetailResponse()
                ).collect(Collectors.toSet());
        dto.setCategories((List<BusinessCategoryModelDTO.BusinessCategoryDetailResponse>) categoryDTOs);
        return dto;
    }

    @Override
    public List<BusinessDetailResponse> findAllData() {
        // Get the list
        List<Business> datas = repository.findAll();

        // stream into the list
        return datas.stream()
                .map(converter::convertToListResponse)
                .collect(Collectors.toList());
    }

    @Override
    public void saveData(@Valid BusinessCreateRequest dto) throws BadRequestException {
        // set entity to add with model mapper
        Business data = converter.convertToCreateRequest(dto);
        // save data
        repository.save(data);
    }

    @Override
    public void updateData(Long id, BusinessUpdateRequest dto) throws BadRequestException {
        // check exist and get
        Business data = repository.findById(id)
                .orElseThrow(() -> new BadRequestException("INVALID Business ID"));

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
            throw new BadRequestException("Business not found");
        } else {
            repository.deleteById(id);
        }
    }
}

