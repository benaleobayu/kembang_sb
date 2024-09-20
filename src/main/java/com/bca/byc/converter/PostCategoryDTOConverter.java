package com.bca.byc.converter;

import com.bca.byc.entity.PostCategory;
import com.bca.byc.model.PostCategoryCreateUpdateRequest;
import com.bca.byc.model.PostCategoryDetailResponse;

import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import jakarta.validation.Valid;

import java.time.LocalDateTime;

@Component
@AllArgsConstructor
public class PostCategoryDTOConverter {

    private ModelMapper modelMapper;

    // for get data
    public PostCategoryDetailResponse convertToListResponse(PostCategory data) {
        // mapping Entity with DTO Entity
        PostCategoryDetailResponse dto = modelMapper.map(data, PostCategoryDetailResponse.class);
        // return
        return dto;
    }

    // for create data
    public PostCategory convertToCreateRequest(@Valid PostCategoryCreateUpdateRequest dto) {
        // mapping DTO Entity with Entity
        PostCategory data = modelMapper.map(dto, PostCategory.class);
        // return
        return data;
    }

    // for update data
    public void convertToUpdateRequest(PostCategory data, @Valid PostCategoryCreateUpdateRequest dto) {
        // mapping DTO Entity with Entity
        modelMapper.map(dto, data);
        // set updated_at
        data.setUpdatedAt(LocalDateTime.now());
    }
}
