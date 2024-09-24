package com.bca.byc.converter;

import com.bca.byc.entity.AppUser;
import com.bca.byc.model.UserManagementDetailResponse;
import com.bca.byc.service.UserActiveUpdateRequest;
import com.bca.byc.util.helper.Formatter;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
@AllArgsConstructor
public class UserDeletedDTOConverter {

    private ModelMapper modelMapper;

    // for get data
    public UserManagementDetailResponse convertToListResponse(AppUser data) {
        // mapping Entity with DTO Entity
        UserManagementDetailResponse dto = modelMapper.map(data, UserManagementDetailResponse.class);

        UserManagementConverter converter = new UserManagementConverter();
        converter.DetailResponse(data, dto);

        // return
        return dto;
    }

    // for create data
    public AppUser convertToCreateRequest(@Valid UserActiveUpdateRequest dto) {
        // mapping DTO Entity with Entity
        AppUser data = modelMapper.map(dto, AppUser.class);
        // return
        return data;
    }

    // for update data
    public void convertToUpdateRequest(AppUser data, @Valid UserActiveUpdateRequest dto) {
        // mapping DTO Entity with Entity
        modelMapper.map(dto, data);
        // set updated_at
        data.setUpdatedAt(LocalDateTime.now());
    }
}

