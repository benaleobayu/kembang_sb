package com.bca.byc.convert.cms;

import com.bca.byc.entity.InterestCategory;
import com.bca.byc.model.cms.InterestCategoryCreateRequest;
import com.bca.byc.model.cms.InterestCategoryDetailResponse;
import com.bca.byc.model.cms.InterestCategoryUpdateRequest;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.validation.Valid;
import java.time.LocalDateTime;

@Component
public class InterestCategoryDTOConverter {

    @Autowired
    private ModelMapper modelMapper;

    // for get data
    public InterestCategoryDetailResponse convertToListResponse(InterestCategory data) {
        InterestCategoryDetailResponse dto = modelMapper.map(data, InterestCategoryDetailResponse.class);

        return dto;
    }

    // for create data
    public InterestCategory convertToCreateRequest(@Valid InterestCategoryCreateRequest dto) {
        InterestCategory data = modelMapper.map(dto, InterestCategory.class);

        return data;
    }

    // for update data
    public void convertToUpdateRequest(InterestCategory entity, @Valid InterestCategoryUpdateRequest dto) {

        modelMapper.map(dto, entity);

        entity.setUpdatedAt(LocalDateTime.now());
    }
}
