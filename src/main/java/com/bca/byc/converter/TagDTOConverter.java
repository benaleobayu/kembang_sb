package com.bca.byc.converter;

import com.bca.byc.entity.Tag;
import com.bca.byc.model.data.TagDetailResponse;
import com.bca.byc.model.data.TagCreateUpdateRequest;

import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import jakarta.validation.Valid;

import java.time.LocalDateTime;

@Component
@AllArgsConstructor
public class TagDTOConverter {

    private ModelMapper modelMapper;

    // for get data
    public TagDetailResponse convertToListResponse(Tag data) {
        // mapping Entity with DTO Entity
        TagDetailResponse dto = new TagDetailResponse();
        dto.setId(data.getSecureId());
        dto.setIndex(data.getId());
        dto.setName(data.getName());
        // return
        return dto;
    }

    // for create data
    public Tag convertToCreateRequest(@Valid TagCreateUpdateRequest dto) {
        // mapping DTO Entity with Entity
        Tag data = modelMapper.map(dto, Tag.class);
        // return
        return data;
    }

    // for update data
    public void convertToUpdateRequest(Tag data, @Valid TagCreateUpdateRequest dto) {
        // mapping DTO Entity with Entity
        modelMapper.map(dto, data);
        // set updated_at
        data.setUpdatedAt(LocalDateTime.now());
    }
}
