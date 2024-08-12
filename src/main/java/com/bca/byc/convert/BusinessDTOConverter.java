package com.bca.byc.convert;

import com.bca.byc.entity.Business;
import com.bca.byc.model.api.BusinessModelDTO;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import javax.validation.Valid;
import java.time.LocalDateTime;

@Component
@AllArgsConstructor
public class BusinessDTOConverter {

    private ModelMapper modelMapper;

    // for get data
    public BusinessModelDTO.DetailResponse convertToListResponse(Business data) {
        // mapping Entity with DTO Entity
        BusinessModelDTO.DetailResponse dto = modelMapper.map(data, BusinessModelDTO.DetailResponse.class);
        // return
        return dto;
    }

    // for create data
    public Business convertToCreateRequest(@Valid BusinessModelDTO.CreateRequest dto) {
        // mapping DTO Entity with Entity
        Business data = modelMapper.map(dto, Business.class);
        // return
        return data;
    }

    // for update data
    public void convertToUpdateRequest(Business data, @Valid BusinessModelDTO.UpdateRequest dto) {
        // mapping DTO Entity with Entity
        modelMapper.map(dto, data);
        // set updated_at
        data.setUpdatedAt(LocalDateTime.now());
    }
}
