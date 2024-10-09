package com.bca.byc.converter;

import com.bca.byc.entity.Post;
import com.bca.byc.model.AdminContentCreateUpdateRequest;
import com.bca.byc.model.AdminContentDetailResponse;

import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import jakarta.validation.Valid;

import java.time.LocalDateTime;

@Component
@AllArgsConstructor
public class AdminContentDTOConverter {

    private ModelMapper modelMapper;

    // for get data
    public AdminContentDetailResponse convertToListResponse(Post data) {
        // mapping Entity with DTO Entity
        AdminContentDetailResponse dto = modelMapper.map(data, AdminContentDetailResponse.class);
        // return
        return dto;
    }

    // for create data
    public Post convertToCreateRequest(@Valid AdminContentCreateUpdateRequest dto) {
        // mapping DTO Entity with Entity
        Post data = modelMapper.map(dto, Post.class);
        // return
        return data;
    }

    // for update data
    public void convertToUpdateRequest(Post data, @Valid AdminContentCreateUpdateRequest dto) {
        // mapping DTO Entity with Entity
        modelMapper.map(dto, data);
        // set updated_at
        data.setUpdatedAt(LocalDateTime.now());
    }
}

