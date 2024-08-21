package com.bca.byc.convert;

import com.bca.byc.entity.ExpectItem;
import com.bca.byc.model.ExpectItemModelDTO;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import javax.validation.Valid;
import java.time.LocalDateTime;

@Component
@AllArgsConstructor
public class ExpectItemDTOConverter {

    private ModelMapper modelMapper;

    // for get data
    public ExpectItemModelDTO.ExpectItemDetailResponse convertToListResponse(ExpectItem data) {
        // mapping Entity with DTO Entity
        ExpectItemModelDTO.ExpectItemDetailResponse dto = modelMapper.map(data, ExpectItemModelDTO.ExpectItemDetailResponse.class);
        // return
        return dto;
    }

    // for create data
    public ExpectItem convertToCreateRequest(@Valid ExpectItemModelDTO.ExpectItemCreateRequest dto) {
        // mapping DTO Entity with Entity
        ExpectItem data = modelMapper.map(dto, ExpectItem.class);
        // return
        return data;
    }

    // for update data
    public void convertToUpdateRequest(ExpectItem data, @Valid ExpectItemModelDTO.ExpectItemUpdateRequest dto) {
        // mapping DTO Entity with Entity
        modelMapper.map(dto, data);
        // set updated_at
        data.setUpdatedAt(LocalDateTime.now());
    }
}
