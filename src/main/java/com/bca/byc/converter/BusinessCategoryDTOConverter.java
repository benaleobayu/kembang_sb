package com.bca.byc.converter;

import com.bca.byc.converter.parsing.GlobalConverter;
import com.bca.byc.entity.BusinessCategory;
import com.bca.byc.model.*;
import com.bca.byc.util.helper.Formatter;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

import static com.bca.byc.converter.parsing.GlobalConverter.CmsIDTimeStampResponseAndId;

@Component
@AllArgsConstructor
public class BusinessCategoryDTOConverter {

    private ModelMapper modelMapper;

    // for get data
    public BusinessCategoryIndexResponse convertToIndexResponse(BusinessCategory data) {
        BusinessCategoryIndexResponse dto = new BusinessCategoryIndexResponse();
        dto.setName(data.getName());
        dto.setStatus(data.getIsActive());
        dto.setOrders(data.getOrders());
        List<String> subCategories = new ArrayList<>();
        for (BusinessCategory businessCategory : data.getChildren()) {
            if (businessCategory.getIsDeleted().equals(false)) {
                subCategories.add(businessCategory.getName());
            }
        }
        dto.setSubCategories(subCategories);
        CmsIDTimeStampResponseAndId(dto, data);
        return dto;
    }

    public BusinessCategoryListResponse convertToListResponse(BusinessCategory data) {
        GlobalConverter converter = new GlobalConverter();
        BusinessCategoryListResponse dto = new BusinessCategoryListResponse();
        dto.setName(data.getName());
        dto.setDescription(data.getDescription() != null ? Formatter.formatDescription(data.getDescription()) : null);
        dto.setOrders(data.getOrders());
        dto.setStatus(data.getIsActive());
        CmsIDTimeStampResponseAndId(dto, data); // timestamp and id

        List<BusinessCategoryItemListResponse> listBusiness = new ArrayList<>();
        for (BusinessCategory businessCategory : data.getChildren()) {
            BusinessCategoryItemListResponse child = new BusinessCategoryItemListResponse();
            child.setName(businessCategory.getName());
            child.setDescription(businessCategory.getDescription() != null ? Formatter.formatDescription(businessCategory.getDescription()) : null);
            child.setOrders(businessCategory.getOrders());
            child.setStatus(businessCategory.getIsActive());
            CmsIDTimeStampResponseAndId(child, businessCategory); // timestamp and id
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
        CmsIDTimeStampResponseAndId(dto, data); // timestamp and id
        return dto;
    }
    // for create data parent

    public BusinessCategory convertToCreateParentRequest(@Valid BusinessCategoryParentCreateRequest dto) {
        // mapping DTO Entity with Entity
        BusinessCategory data = new BusinessCategory();
        data.setName(dto.getName());
        data.setIsActive(dto.getStatus());
        data.setOrders(dto.getOrders());
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
        data.setName(dto.getName());
        data.setOrders(dto.getOrders());
        data.setIsActive(dto.getStatus());
    }

    public PostCategoryDetailResponse convertToListCategoryPostCreateResponse(BusinessCategory data) {
        // mapping Entity with DTO Entity
        PostCategoryDetailResponse dto = new PostCategoryDetailResponse();
        dto.setId(data.getSecureId());
        dto.setName(data.getName());
        // return
        return dto;
    }
}
