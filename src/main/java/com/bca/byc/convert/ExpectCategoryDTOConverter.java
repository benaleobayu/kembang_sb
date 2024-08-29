package com.bca.byc.convert;

import com.bca.byc.entity.ExpectCategory;
import com.bca.byc.model.ExpectCategoryCreateRequest;
import com.bca.byc.model.ExpectCategoryDetailResponse;
import com.bca.byc.model.ExpectCategoryUpdateRequest;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import jakarta.validation.Valid;
import java.time.LocalDateTime;

@Component
@AllArgsConstructor
public class ExpectCategoryDTOConverter {

    private ModelMapper modelMapper;

    // for get data
    public ExpectCategoryDetailResponse convertToListResponse(ExpectCategory data) {
        // mapping Entity with DTO Entity
        ExpectCategoryDetailResponse dto = modelMapper.map(data, ExpectCategoryDetailResponse.class);
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
