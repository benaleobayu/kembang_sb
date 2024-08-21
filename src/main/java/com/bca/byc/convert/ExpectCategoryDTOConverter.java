package com.bca.byc.convert;

import com.bca.byc.entity.ExpectCategory;
import com.bca.byc.model.ExpectCategoryModelDTO;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import javax.validation.Valid;
import java.time.LocalDateTime;

@Component
@AllArgsConstructor
public class ExpectCategoryDTOConverter {

    private ModelMapper modelMapper;

    // for get data
    public ExpectCategoryModelDTO.ExpectCategoryDetailResponse convertToListResponse(ExpectCategory data) {
        // mapping Entity with DTO Entity
        ExpectCategoryModelDTO.ExpectCategoryDetailResponse dto = modelMapper.map(data, ExpectCategoryModelDTO.ExpectCategoryDetailResponse.class);
        // return
        return dto;
    }

    // for create data
    public ExpectCategory convertToCreateRequest(@Valid ExpectCategoryModelDTO.ExpectCategoryCreateRequest dto) {
        // mapping DTO Entity with Entity
        ExpectCategory data = modelMapper.map(dto, ExpectCategory.class);
        // return
        return data;
    }

    // for update data
    public void convertToUpdateRequest(ExpectCategory data, @Valid ExpectCategoryModelDTO.ExpectCategoryUpdateRequest dto) {
        // mapping DTO Entity with Entity
        modelMapper.map(dto, data);
        // set updated_at
        data.setUpdatedAt(LocalDateTime.now());
    }
}
