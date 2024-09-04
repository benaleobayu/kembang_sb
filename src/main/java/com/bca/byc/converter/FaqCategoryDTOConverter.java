package com.bca.byc.converter;

import com.bca.byc.entity.FaqCategory;
import com.bca.byc.model.FaqCategoryCreateRequest;
import com.bca.byc.model.FaqCategoryDetailResponse;
import com.bca.byc.model.FaqCategoryUpdateRequest;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
@AllArgsConstructor
public class FaqCategoryDTOConverter {

    private ModelMapper modelMapper;

    // for get data
    public FaqCategoryDetailResponse convertToListResponse(FaqCategory data) {
        // mapping Entity with DTO Entity
        FaqCategoryDetailResponse dto = modelMapper.map(data, FaqCategoryDetailResponse.class);
        // return
        return dto;
    }

    // for create data
    public FaqCategory convertToCreateRequest(@Valid FaqCategoryCreateRequest dto) {
        // mapping DTO Entity with Entity
        FaqCategory data = modelMapper.map(dto, FaqCategory.class);
        // return
        return data;
    }

    // for update data
    public void convertToUpdateRequest(FaqCategory data, @Valid FaqCategoryUpdateRequest dto) {
        // mapping DTO Entity with Entity
        modelMapper.map(dto, data);
        // set updated_at
        data.setUpdatedAt(LocalDateTime.now());
    }
}
