package com.bca.byc.convert;

import com.bca.byc.entity.Content;
import com.bca.byc.model.cms.ContentModelDTO;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import javax.validation.Valid;
import java.time.LocalDateTime;

@Component
@AllArgsConstructor
public class ContentDTOConverter {

    private ModelMapper modelMapper;

    // for get data
    public ContentModelDTO.ContentDetailResponse convertToListResponse(Content data) {
        // mapping Entity with DTO Entity
        ContentModelDTO.ContentDetailResponse dto = modelMapper.map(data, ContentModelDTO.ContentDetailResponse.class);
        // return
        return dto;
    }

    // for create data
    public Content convertToCreateRequest(@Valid ContentModelDTO.ContentCreateRequest dto) {
        // mapping DTO Entity with Entity
        Content data = modelMapper.map(dto, Content.class);
        // return
        return data;
    }

    // for update data
    public void convertToUpdateRequest(Content data, @Valid ContentModelDTO.ContentUpdateRequest dto) {
        // mapping DTO Entity with Entity
        modelMapper.map(dto, data);
        // set updated_at
        data.setUpdatedAt(LocalDateTime.now());
    }
}
