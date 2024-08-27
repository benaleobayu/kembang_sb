package com.bca.byc.convert;

import com.bca.byc.entity.FaqCategory;
import com.bca.byc.model.FaqCategoryModelDTO;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import jakarta.validation.Valid;
import java.time.LocalDateTime;

@Component
@AllArgsConstructor
public class FaqCategoryDTOConverter {

    private ModelMapper modelMapper;

    // for get data
    public FaqCategoryModelDTO.FaqCategoryDetailResponse convertToListResponse(FaqCategory data) {
        // mapping Entity with DTO Entity
        FaqCategoryModelDTO.FaqCategoryDetailResponse dto = modelMapper.map(data, FaqCategoryModelDTO.FaqCategoryDetailResponse.class);
        // return
        return dto;
    }

    // for create data
    public FaqCategory convertToCreateRequest(@Valid FaqCategoryModelDTO.FaqCategoryCreateRequest dto) {
        // mapping DTO Entity with Entity
        FaqCategory data = modelMapper.map(dto, FaqCategory.class);
        // return
        return data;
    }

    // for update data
    public void convertToUpdateRequest(FaqCategory data, @Valid FaqCategoryModelDTO.FaqCategoryUpdateRequest dto) {
        // mapping DTO Entity with Entity
        modelMapper.map(dto, data);
        // set updated_at
        data.setUpdatedAt(LocalDateTime.now());
    }
}
