package com.bca.byc.converter;

import com.bca.byc.converter.parsing.GlobalConverter;
import com.bca.byc.entity.BusinessCategory;
import com.bca.byc.model.BusinessCategoryCreateRequest;
import com.bca.byc.model.BusinessCategoryDetailResponse;
import com.bca.byc.model.BusinessCategoryUpdateRequest;
import com.bca.byc.util.helper.Formatter;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Component
@AllArgsConstructor
public class BusinessCategoryDTOConverter {

    private ModelMapper modelMapper;

    // for get data
    public BusinessCategoryDetailResponse convertToListResponse(BusinessCategory data) {
        GlobalConverter converter = new GlobalConverter();
        BusinessCategoryDetailResponse dto = new BusinessCategoryDetailResponse();
        dto.setName(data.getName());
        dto.setDescription(Formatter.formatDescription(data.getDescription()));
        dto.setOrders(data.getOrders());
        dto.setStatus(data.getIsActive());
        dto.setParentId(data.getParentId() != null ? data.getParentId().getId() : null);
        converter.CmsIDTimeStampResponse(dto, data); // timestamp and id

        List<BusinessCategoryDetailResponse> listBusiness = new ArrayList<>();
        for (BusinessCategory businessCategory : data.getChildren()) {
            BusinessCategoryDetailResponse child = new BusinessCategoryDetailResponse();
            child.setName(businessCategory.getName());
            child.setDescription(Formatter.formatDescription(businessCategory.getDescription()));
            child.setOrders(businessCategory.getOrders());
            child.setStatus(businessCategory.getIsActive());
            child.setParentId(businessCategory.getParentId() != null ? businessCategory.getParentId().getId() : null);
            converter.CmsIDTimeStampResponse(child, businessCategory); // timestamp and id
            listBusiness.add(child);
        }
        dto.setChildren(listBusiness);
        // return
        return dto;
    }

    // for create data
    public BusinessCategory convertToCreateRequest(@Valid BusinessCategoryCreateRequest dto) {
        // mapping DTO Entity with Entity
        BusinessCategory data = modelMapper.map(dto, BusinessCategory.class);
        // return
        return data;
    }

    // for update data
    public void convertToUpdateRequest(BusinessCategory data, @Valid BusinessCategoryUpdateRequest dto) {
        // mapping DTO Entity with Entity
        modelMapper.map(dto, data);
        // set updated_at
        data.setUpdatedAt(LocalDateTime.now());
    }
}
