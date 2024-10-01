package com.bca.byc.converter;

import com.bca.byc.converter.parsing.GlobalConverter;
import com.bca.byc.entity.BusinessCategory;
import com.bca.byc.entity.ExpectCategory;
import com.bca.byc.entity.ExpectItem;
import com.bca.byc.model.*;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Component
@AllArgsConstructor
public class ExpectCategoryDTOConverter {

    private ModelMapper modelMapper;

    public ExpectCategoryIndexResponse convertToIndexResponse(ExpectCategory data) {
        GlobalConverter converter = new GlobalConverter();
        ExpectCategoryIndexResponse dto = new ExpectCategoryIndexResponse();
        dto.setName(data.getName());
        dto.setStatus(data.getIsActive());
        dto.setOrders(data.getOrders());
        List<String> subCategories = new ArrayList<>();
        for (ExpectItem expectItem : data.getExpectItems()) {
            if (expectItem.getIsDeleted().equals(false) && !expectItem.getName().equals("Other")){
                subCategories.add(expectItem.getName());
            }
        }
        dto.setSubCategories(subCategories);
        converter.CmsIDTimeStampResponse(dto, data);
        return dto;
    }
    // for get data

    public ExpectCategoryIndexResponse convertToListResponse(ExpectCategory data) {
        GlobalConverter converter = new GlobalConverter();
        // mapping Entity with DTO Entity
        ExpectCategoryIndexResponse dto = new ExpectCategoryIndexResponse();
        dto.setName(data.getName());
        dto.setOrders(data.getOrders());
        dto.setStatus(data.getIsActive());
        converter.CmsIDTimeStampResponse(dto, data);
        // return
        return dto;
    }

    public ExpectCategoryIndexResponse convertToDetailResponse(ExpectCategory data) {
        GlobalConverter converter = new GlobalConverter();
        ExpectCategoryIndexResponse dto = new ExpectCategoryIndexResponse();
        dto.setName(data.getName());
        dto.setStatus(data.getIsActive());
        dto.setOrders(data.getOrders());
        List<String> subCategories = new ArrayList<>();
        for (ExpectItem expectItem : data.getExpectItems()) {
            if (expectItem.getIsDeleted().equals(false) && !expectItem.getName().equals("Other")){
                subCategories.add(expectItem.getName());
            }
        }
        dto.setSubCategories(subCategories);
        converter.CmsIDTimeStampResponse(dto, data);
        // return
        return dto;
    }


    // for create data

    public ExpectCategory convertToCreateRequest(@Valid ExpectCategoryCreateUpdateRequest dto) {
        // mapping DTO Entity with Entity
        ExpectCategory data = modelMapper.map(dto, ExpectCategory.class);
        // return
        return data;
    }
    // for update data

    public void convertToUpdateRequest(ExpectCategory data, @Valid ExpectCategoryCreateUpdateRequest dto) {
        // mapping DTO Entity with Entity
        modelMapper.map(dto, data);
        // set updated_at
        data.setUpdatedAt(LocalDateTime.now());
    }
}
