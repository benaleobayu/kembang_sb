package com.bca.byc.convert;

import com.bca.byc.entity.InterestCategory;
import com.bca.byc.model.InterestCategoryModelDTO;
import com.bca.byc.util.Formatter;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import jakarta.validation.Valid;
import java.time.LocalDateTime;

@Component
@AllArgsConstructor
public class InterestCategoryDTOConverter {

    private ModelMapper modelMapper;

    // for get data
    public InterestCategoryModelDTO.InterestCategoryDetailResponse convertToListResponse(InterestCategory data) {
        // mapping Entity with DTO Entity
        InterestCategoryModelDTO.InterestCategoryDetailResponse dto = modelMapper.map(data, InterestCategoryModelDTO.InterestCategoryDetailResponse.class);
        // Use DataFormatter here
        dto.setDescription(Formatter.formatDescription(data.getDescription()));
        dto.setCreatedAt(Formatter.formatLocalDateTime(data.getCreatedAt()));
        dto.setUpdatedAt(Formatter.formatLocalDateTime(data.getUpdatedAt()));
        // return
        return dto;
    }

    // for create data
    public InterestCategory convertToCreateRequest(@Valid InterestCategoryModelDTO.InterestCategoryCreateRequest dto) {
        // mapping DTO Entity with Entity
        InterestCategory data = modelMapper.map(dto, InterestCategory.class);
        // return
        return data;
    }

    // for update data
    public void convertToUpdateRequest(InterestCategory data, @Valid InterestCategoryModelDTO.InterestCategoryUpdateRequest dto) {
        // mapping DTO Entity with Entity
        modelMapper.map(dto, data);
        // set updated_at
        data.setUpdatedAt(LocalDateTime.now());
    }
}
