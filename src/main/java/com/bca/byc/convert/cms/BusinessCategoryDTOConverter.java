package com.bca.byc.convert.cms;

import com.bca.byc.entity.BusinessCategory;
import com.bca.byc.model.cms.BusinessCategoryCreateRequest;
import com.bca.byc.model.cms.BusinessCategoryDetailResponse;
import com.bca.byc.model.cms.BusinessCategoryUpdateRequest;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.validation.Valid;
import java.time.LocalDateTime;

@Component
public class BusinessCategoryDTOConverter {

    @Autowired
    private ModelMapper modelMapper;

    // for get data
    public BusinessCategoryDetailResponse convertToListResponse(BusinessCategory data) {
        BusinessCategoryDetailResponse dto = modelMapper.map(data, BusinessCategoryDetailResponse.class);

        return dto;
    }

    // for create data
    public BusinessCategory convertToCreateRequest(@Valid BusinessCategoryCreateRequest dto) {
        BusinessCategory data = modelMapper.map(dto, BusinessCategory.class);

        return data;
    }

    // for update data
    public void convertToUpdateRequest(BusinessCategory entity, @Valid BusinessCategoryUpdateRequest dto) {

        modelMapper.map(dto, entity);

        entity.setUpdatedAt(LocalDateTime.now());
    }
}
