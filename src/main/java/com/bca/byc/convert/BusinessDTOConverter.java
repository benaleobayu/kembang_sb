package com.bca.byc.convert;

import com.bca.byc.entity.Business;
import com.bca.byc.model.BusinessCreateRequest;
import com.bca.byc.model.BusinessDetailResponse;
import com.bca.byc.model.BusinessUpdateRequest;
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
    public BusinessDetailResponse convertToListResponse(Business data) {
        // mapping Entity with DTO Entity
        BusinessDetailResponse dto = modelMapper.map(data, BusinessDetailResponse.class);
        // return
        return dto;
    }

    // for create data
    public Business convertToCreateRequest(@Valid BusinessCreateRequest dto) {
        // mapping DTO Entity with Entity
        Business data = modelMapper.map(dto, Business.class);
        // return
        return data;
    }

    // for update data
    public void convertToUpdateRequest(Business data, @Valid BusinessUpdateRequest dto) {
        // mapping DTO Entity with Entity
        modelMapper.map(dto, data);
        // set updated_at
        data.setUpdatedAt(LocalDateTime.now());
    }
}
