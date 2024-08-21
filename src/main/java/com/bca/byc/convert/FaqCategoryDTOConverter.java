package com.bca.byc.convert;

import com.bca.byc.entity.FaqCategory;
import com.bca.byc.model.cms.FaqCategoryModelDTO;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import javax.validation.Valid;
import java.time.LocalDateTime;

@Component
@AllArgsConstructor
public class FaqCategoryDTOConverter {

    private ModelMapper modelMapper;

    // for get data
    public FaqCategoryModelDTO.DetailResponse convertToListResponse(FaqCategory data) {
        // mapping Entity with DTO Entity
        FaqCategoryModelDTO.DetailResponse dto = modelMapper.map(data, FaqCategoryModelDTO.DetailResponse.class);
        // return
        return dto;
    }

    // for create data
    public FaqCategory convertToCreateRequest(@Valid FaqCategoryModelDTO.CreateRequest dto) {
        // mapping DTO Entity with Entity
        FaqCategory data = modelMapper.map(dto, FaqCategory.class);
        // return
        return data;
    }

    // for update data
    public void convertToUpdateRequest(FaqCategory data, @Valid FaqCategoryModelDTO.UpdateRequest dto) {
        // mapping DTO Entity with Entity
        modelMapper.map(dto, data);
        // set updated_at
        data.setUpdatedAt(LocalDateTime.now());
    }
}
