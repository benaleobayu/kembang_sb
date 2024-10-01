package com.bca.byc.converter;

import com.bca.byc.entity.BusinessCategory;

import com.bca.byc.model.attribute.AttributeResponse;
import com.bca.byc.model.data.InputAttributeResponse;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class InputAttributeDTOConverter {

    private ModelMapper modelMapper;

    // for get data
    public AttributeResponse convertToListSubBusinessCategoryResponse(BusinessCategory data) {
        // mapping Entity with DTO Entity
        AttributeResponse<Long> dto = new AttributeResponse<>();
        dto.setId(data.getId());
        dto.setName(data.getName());
        // return
        return dto;
    }


}

