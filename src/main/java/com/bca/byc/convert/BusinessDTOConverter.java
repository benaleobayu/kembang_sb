package com.bca.byc.convert;

import com.bca.byc.entity.Business;
import com.bca.byc.model.BusinessModelDTO;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
@AllArgsConstructor
public class BusinessDTOConverter {

    private ModelMapper modelMapper;

    // for get data
    public BusinessModelDTO.BusinessDetailResponse convertToListResponse(Business data) {
        // mapping Entity with DTO Entity
        BusinessModelDTO.BusinessDetailResponse dto = modelMapper.map(data, BusinessModelDTO.BusinessDetailResponse.class);
        // return
        return dto;
    }

    // for create data
    public Business convertToCreateRequest(@Valid BusinessModelDTO.BusinessCreateRequest dto) {
        // mapping DTO Entity with Entity
        Business data = modelMapper.map(dto, Business.class);
        // return
        return data;
    }

    // for update data
    public void convertToUpdateRequest(Business data, @Valid BusinessModelDTO.BusinessUpdateRequest dto) {
        // mapping DTO Entity with Entity
        modelMapper.map(dto, data);
        // set updated_at
        data.setUpdatedAt(LocalDateTime.now());
    }
}
