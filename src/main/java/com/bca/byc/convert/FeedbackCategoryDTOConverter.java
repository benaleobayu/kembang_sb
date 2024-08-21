package com.bca.byc.convert;

import com.bca.byc.entity.FeedbackCategory;
import com.bca.byc.model.cms.FeedbackCategoryModelDTO;
import com.bca.byc.util.Formatter;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import javax.validation.Valid;
import java.time.LocalDateTime;

@Component
@AllArgsConstructor
public class FeedbackCategoryDTOConverter {

    private ModelMapper modelMapper;

    // for get data
    public FeedbackCategoryModelDTO.FeedbackCategoryDetailResponse convertToListResponse(FeedbackCategory data) {
        // mapping Entity with DTO Entity
        FeedbackCategoryModelDTO.FeedbackCategoryDetailResponse dto = modelMapper.map(data, FeedbackCategoryModelDTO.FeedbackCategoryDetailResponse.class);
        // Use DataFormatter here
        dto.setDescription(Formatter.formatDescription(data.getDescription()));
        dto.setCreatedAt(Formatter.formatLocalDateTime(data.getCreatedAt()));
        dto.setUpdatedAt(Formatter.formatLocalDateTime(data.getUpdatedAt()));
        // return
        return dto;
    }

    // for create data
    public FeedbackCategory convertToCreateRequest(@Valid FeedbackCategoryModelDTO.FeedbackCategoryCreateRequest dto) {
        // mapping DTO Entity with Entity
        FeedbackCategory data = modelMapper.map(dto, FeedbackCategory.class);
        // return
        return data;
    }

    // for update data
    public void convertToUpdateRequest(FeedbackCategory data, @Valid FeedbackCategoryModelDTO.FeedbackCategoryUpdateRequest dto) {
        // mapping DTO Entity with Entity
        modelMapper.map(dto, data);
        // set updated_at
        data.setUpdatedAt(LocalDateTime.now());
    }
}
