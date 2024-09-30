package com.bca.byc.converter;

import com.bca.byc.converter.parsing.GlobalConverter;
import com.bca.byc.entity.BusinessCategory;
import com.bca.byc.model.*;
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
    public BusinessCategoryListResponse convertToListResponse(BusinessCategory data) {
        GlobalConverter converter = new GlobalConverter();
        BusinessCategoryListResponse dto = new BusinessCategoryListResponse();
        dto.setName(data.getName());
        dto.setDescription(Formatter.formatDescription(data.getDescription()));
        dto.setOrders(data.getOrders());
        dto.setStatus(data.getIsActive());
        converter.CmsIDTimeStampResponse(dto, data); // timestamp and id

        List<BusinessCategoryItemListResponse> listBusiness = new ArrayList<>();
        for (BusinessCategory businessCategory : data.getChildren()) {
            BusinessCategoryItemListResponse child = new BusinessCategoryItemListResponse();
            child.setName(businessCategory.getName());
            child.setDescription(Formatter.formatDescription(businessCategory.getDescription()));
            child.setOrders(businessCategory.getOrders());
            child.setStatus(businessCategory.getIsActive());
            child.setParentId(businessCategory.getParentId() != null ? businessCategory.getParentId().getSecureId() : null);
            converter.CmsIDTimeStampResponse(child, businessCategory); // timestamp and id
            listBusiness.add(child);
        }
        dto.setSubCategories(listBusiness);
        // return
        return dto;
    }

    public BusinessCategoryItemListResponse convertToListItemResponse(BusinessCategory data) {
        GlobalConverter converter = new GlobalConverter();
        BusinessCategoryItemListResponse dto = new BusinessCategoryItemListResponse();
        dto.setName(data.getName());
        dto.setDescription(Formatter.formatDescription(data.getDescription()));
        dto.setOrders(data.getOrders());
        dto.setStatus(data.getIsActive());
        converter.CmsIDTimeStampResponse(dto, data); // timestamp and id
        return dto;
    }

    // for create data parent
    public BusinessCategory convertToCreateParentRequest(@Valid BusinessCategoryParentCreateRequest dto) {
        // mapping DTO Entity with Entity
        BusinessCategory data = modelMapper.map(dto, BusinessCategory.class);

        data.setIsParent(true);
        // return
        return data;
    }

    // for create data child
    public BusinessCategory convertToCreateChildRequest(@Valid BusinessCategoryItemCreateRequest dto) {
        // mapping DTO Entity with Entity
        BusinessCategory data = new BusinessCategory();

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
