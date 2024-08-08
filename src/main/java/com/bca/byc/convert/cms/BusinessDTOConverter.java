package com.bca.byc.convert.cms;

import com.bca.byc.entity.Business;
import com.bca.byc.model.api.BusinessCreateRequest;
import com.bca.byc.model.api.BusinessDetailResponse;
import com.bca.byc.model.api.BusinessUpdateRequest;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.validation.Valid;
import java.time.LocalDateTime;

@Component
public class BusinessDTOConverter {

    @Autowired
    private ModelMapper modelMapper;

    // for get data
    public BusinessDetailResponse convertToListResponse(Business data){
        BusinessDetailResponse dto = modelMapper.map(data, BusinessDetailResponse.class);

        return dto;
    }

    // for create data
    public Business convertToCreateRequest(BusinessCreateRequest dto) {
        Business data = modelMapper.map(dto, Business.class);

        return data;
    }

    // for update data
    public Business convertToUpdateRequest(@Valid BusinessUpdateRequest dto){
        Business data = modelMapper.map(dto, Business.class);

        // set updatedAt
        data.setUpdatedAt(LocalDateTime.now());
        return data;
    }
}
