package com.bca.byc.converter;

import com.bca.byc.entity.ExpectItem;
import com.bca.byc.model.ExpectItemCreateRequest;
import com.bca.byc.model.ExpectItemDetailResponse;
import com.bca.byc.model.ExpectItemUpdateRequest;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
@AllArgsConstructor
public class ExpectItemDTOConverter {

    private ModelMapper modelMapper;

    // for get data
    public ExpectItemDetailResponse convertToListResponse(ExpectItem data) {
        // mapping Entity with DTO Entity
        ExpectItemDetailResponse dto = modelMapper.map(data, ExpectItemDetailResponse.class);
        // return
        return dto;
    }

    // for create data
    public ExpectItem convertToCreateRequest(@Valid ExpectItemCreateRequest dto) {
        // mapping DTO Entity with Entity
        ExpectItem data = modelMapper.map(dto, ExpectItem.class);
        // return
        return data;
    }

    // for update data
    public void convertToUpdateRequest(ExpectItem data, @Valid ExpectItemUpdateRequest dto) {
        // mapping DTO Entity with Entity
        modelMapper.map(dto, data);
        // set updated_at
        data.setUpdatedAt(LocalDateTime.now());
    }
}
