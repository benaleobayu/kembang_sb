package com.bca.byc.convert;

import com.bca.byc.entity.BusinessCategory;
import com.bca.byc.model.cms.BusinessCategoryModelDTO;
import com.bca.byc.util.Formatter;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import javax.validation.Valid;
import java.time.LocalDateTime;

@Component
@AllArgsConstructor
public class BusinessCategoryDTOConverter {

    private ModelMapper modelMapper;

    // for get data
    public BusinessCategoryModelDTO.DetailResponse convertToListResponse(BusinessCategory data) {
        // mapping Entity with DTO Entity
        BusinessCategoryModelDTO.DetailResponse dto = modelMapper.map(data, BusinessCategoryModelDTO.DetailResponse.class);
        // Use DataFormatter here
        dto.setDescription(Formatter.formatDescription(data.getDescription()));
        dto.setCreatedAt(Formatter.formatLocalDateTime(data.getCreatedAt()));
        dto.setUpdatedAt(Formatter.formatLocalDateTime(data.getUpdatedAt()));
        // return
        return dto;
    }

    // for create data
    public BusinessCategory convertToCreateRequest(@Valid BusinessCategoryModelDTO.CreateRequest dto) {
        // mapping DTO Entity with Entity
        BusinessCategory data = modelMapper.map(dto, BusinessCategory.class);
        // return
        return data;
    }

    // for update data
    public void convertToUpdateRequest(BusinessCategory data, @Valid BusinessCategoryModelDTO.UpdateRequest dto) {
        // mapping DTO Entity with Entity
        modelMapper.map(dto, data);
        // set updated_at
        data.setUpdatedAt(LocalDateTime.now());
    }
}
