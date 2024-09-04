package com.bca.byc.converter;

import com.bca.byc.entity.BusinessCategory;
import com.bca.byc.model.BusinessCategoryCreateRequest;
import com.bca.byc.model.BusinessCategoryDetailResponse;
import com.bca.byc.model.BusinessCategoryUpdateRequest;
import com.bca.byc.util.Formatter;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
@AllArgsConstructor
public class BusinessCategoryDTOConverter {

    private ModelMapper modelMapper;

    // for get data
    public BusinessCategoryDetailResponse convertToListResponse(BusinessCategory data) {
        if (data == null) {
            return null;
        } else {
            // mapping Entity with DTO Entity
            BusinessCategoryDetailResponse dto = modelMapper.map(data, BusinessCategoryDetailResponse.class);
            // Use DataFormatter here
            dto.setDescription(Formatter.formatDescription(data.getDescription()));
            dto.setCreatedAt(Formatter.formatLocalDateTime(data.getCreatedAt()));
            dto.setUpdatedAt(Formatter.formatLocalDateTime(data.getUpdatedAt()));
            // return
            return dto;
        }
    }

    // for create data
    public BusinessCategory convertToCreateRequest(@Valid BusinessCategoryCreateRequest dto) {
        // mapping DTO Entity with Entity
        BusinessCategory data = modelMapper.map(dto, BusinessCategory.class);
        // return
        return data;
    }

    // for update data
    public void convertToUpdateRequest(BusinessCategory data, @Valid BusinessCategoryUpdateRequest dto) {
        // mapping DTO Entity with Entity
        modelMapper.map(dto, data);
        // set updated_at
        data.setUpdatedAt(LocalDateTime.now());
    }
}
