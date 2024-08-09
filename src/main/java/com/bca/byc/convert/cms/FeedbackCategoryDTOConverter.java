package com.bca.byc.convert.cms;

import com.bca.byc.entity.FeedbackCategory;
import com.bca.byc.model.cms.FeedbackCategoryCreateRequest;
import com.bca.byc.model.cms.FeedbackCategoryDetailResponse;
import com.bca.byc.model.cms.FeedbackCategoryUpdateRequest;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.validation.Valid;
import java.time.LocalDateTime;

@Component
public class FeedbackCategoryDTOConverter {

    @Autowired
    private ModelMapper modelMapper;

    // for get data
    public FeedbackCategoryDetailResponse convertToListResponse(FeedbackCategory data) {
        FeedbackCategoryDetailResponse dto = modelMapper.map(data, FeedbackCategoryDetailResponse.class);

        return dto;
    }

    // for create data
    public FeedbackCategory convertToCreateRequest(@Valid FeedbackCategoryCreateRequest dto) {
        FeedbackCategory data = modelMapper.map(dto, FeedbackCategory.class);

        return data;
    }

    // for update data
    public void convertToUpdateRequest(FeedbackCategory entity, @Valid FeedbackCategoryUpdateRequest dto) {

        modelMapper.map(dto, entity);

        entity.setUpdatedAt(LocalDateTime.now());
    }
}
