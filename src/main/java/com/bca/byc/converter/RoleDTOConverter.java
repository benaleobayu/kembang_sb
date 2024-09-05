package com.bca.byc.converter;

import com.bca.byc.entity.Role;
import com.bca.byc.entity.RoleHasPermission;
import com.bca.byc.model.RoleDetailResponse;

import com.bca.byc.model.RoleCreateRequest;
import com.bca.byc.model.RoleUpdateRequest;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

import jakarta.validation.Valid;

import java.time.LocalDateTime;
import java.util.List;

@Component
@AllArgsConstructor
public class RoleDTOConverter {

    private ModelMapper modelMapper;

    // for get data
    public RoleDetailResponse convertToListResponse(Role data) {
        // mapping Entity with DTO Entity
        RoleDetailResponse dto = modelMapper.map(data, RoleDetailResponse.class);
        // return
        List<SimpleGrantedAuthority> roleHasPermissionList = data.getAuthorities();
        // get list permission
//        List<String> permissions = roleHasPermissionList.stream()
//                .map(SimpleGrantedAuthority::getAuthority)
//                .collect(Collectors.toList());

        List<RoleHasPermission> roleHasPermissions = data.getPermissions();
        dto.setPermissions(roleHasPermissions);
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
