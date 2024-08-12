package com.bca.byc.convert;

import com.bca.byc.entity.InterestCategory;
import com.bca.byc.model.cms.InterestCategoryModelDTO;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import javax.validation.Valid;
import java.time.LocalDateTime;

@Component
@AllArgsConstructor
public class InterestCategoryDTOConverter {

    private ModelMapper modelMapper;

    // for get data
    public InterestCategoryModelDTO.DetailResponse convertToListResponse(InterestCategory data) {
        // mapping Entity with DTO Entity
        InterestCategoryModelDTO.DetailResponse dto = modelMapper.map(data, InterestCategoryModelDTO.DetailResponse.class);
        // return
        return dto;
    }

    // for create data
    public InterestCategory convertToCreateRequest(@Valid InterestCategoryModelDTO.CreateRequest dto) {
        // mapping DTO Entity with Entity
        InterestCategory data = modelMapper.map(dto, InterestCategory.class);
        // return
        return data;
    }

    // for update data
    public void convertToUpdateRequest(InterestCategory data, @Valid InterestCategoryModelDTO.UpdateRequest dto) {
        // mapping DTO Entity with Entity
        modelMapper.map(dto, data);
        // set updated_at
        data.setUpdatedAt(LocalDateTime.now());
    }
}
