package com.bca.byc.converter;

import com.bca.byc.entity.AppUserDetail;
import com.bca.byc.model.AppUserDetailCreateUpdateRequest;
import com.bca.byc.model.AppUserDetailDetailResponse;

import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import jakarta.validation.Valid;

import java.time.LocalDateTime;

@Component
@AllArgsConstructor
public class AppUserDetailDTOConverter {

    private ModelMapper modelMapper;

    // for get data
    public AppUserDetailDetailResponse convertToListResponse(AppUserDetail data) {
        // mapping Entity with DTO Entity
        AppUserDetailDetailResponse dto = modelMapper.map(data, AppUserDetailDetailResponse.class);
        // return
        return dto;
    }

    // for create data
    public AppUserDetail convertToCreateRequest(@Valid AppUserDetailCreateUpdateRequest dto) {
        // mapping DTO Entity with Entity
        AppUserDetail data = modelMapper.map(dto, AppUserDetail.class);
        // return
        return data;
    }

    // for update data
    public void convertToUpdateRequest(AppUserDetail data, @Valid AppUserDetailCreateUpdateRequest dto) {
        // mapping DTO Entity with Entity
        modelMapper.map(dto, data);
        // set updated_at
        data.setUpdatedAt(LocalDateTime.now());
    }
}
