package com.bca.byc.converter;

import com.bca.byc.entity.ExpectCategory;
import com.bca.byc.entity.ExpectItem;
import com.bca.byc.model.ExpectCategoryCreateRequest;
import com.bca.byc.model.ExpectCategoryDetailResponse;
import com.bca.byc.model.ExpectCategoryUpdateRequest;
import com.bca.byc.model.ExpectItemDetailResponse;
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

    // for get data
    public ExpectCategoryDetailResponse convertToListResponse(ExpectCategory data) {
        GlobalConverter converter = new GlobalConverter();
        // mapping Entity with DTO Entity
        ExpectCategoryDetailResponse dto = new ExpectCategoryDetailResponse();
        dto.setName(data.getName());
        dto.setDescription(data.getDescription());
        dto.setOrders(data.getOrders());
        dto.setIsOther(data.getIsOther());
        dto.setStatus(data.getIsActive());

        List<ExpectItemDetailResponse> listExpectItem = new ArrayList<>();
        for (ExpectItem expectItem : data.getExpectItems()) {
            ExpectItemDetailResponse expectItemDetailResponse = new ExpectItemDetailResponse();
            expectItemDetailResponse.setName(expectItem.getName());
            expectItemDetailResponse.setDescription(expectItem.getDescription());
            expectItemDetailResponse.setOrders(expectItem.getOrders());
            expectItemDetailResponse.setStatus(expectItem.getIsActive());
            converter.CmsIDTimeStampResponse(dto, data); // timestamp and id
            listExpectItem.add(expectItemDetailResponse);
        }
        dto.setExpectItems(listExpectItem);
        // return
        return dto;
    }

    // for create data
    public ExpectCategory convertToCreateRequest(@Valid ExpectCategoryCreateRequest dto) {
        // mapping DTO Entity with Entity
        ExpectCategory data = modelMapper.map(dto, ExpectCategory.class);
        // return
        return data;
    }

    // for update data
    public void convertToUpdateRequest(ExpectCategory data, @Valid ExpectCategoryUpdateRequest dto) {
        // mapping DTO Entity with Entity
        modelMapper.map(dto, data);
        // set updated_at
        data.setUpdatedAt(LocalDateTime.now());
    }
}
