package com.bca.byc.converter;

import com.bca.byc.entity.Role;
import com.bca.byc.model.RoleDetailResponse;

import com.bca.byc.service.RoleCreateRequest;
import com.bca.byc.service.RoleUpdateRequest;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import jakarta.validation.Valid;

import java.time.LocalDateTime;

@Component
@AllArgsConstructor
public class RoleDTOConverter {

    private ModelMapper modelMapper;

    // for get data
    public RoleDetailResponse convertToListResponse(Role data) {
        // mapping Entity with DTO Entity
        RoleDetailResponse dto = modelMapper.map(data, RoleDetailResponse.class);
        // return
        return dto;
    }

    // for create data
    public Role convertToCreateRequest(@Valid RoleCreateRequest dto) {
        // mapping DTO Entity with Entity
        Role data = modelMapper.map(dto, Role.class);
        // return
        return data;
    }

    // for update data
    public void convertToUpdateRequest(Role data, @Valid RoleUpdateRequest dto) {
        // mapping DTO Entity with Entity
        modelMapper.map(dto, data);
        // set updated_at
        data.setUpdatedAt(LocalDateTime.now());
    }
}
