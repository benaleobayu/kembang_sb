package com.bca.byc.converter;

import com.bca.byc.entity.AppAdmin;
import com.bca.byc.entity.Permission;
import com.bca.byc.entity.Role;
import com.bca.byc.entity.RoleHasPermission;
import com.bca.byc.model.*;
import com.bca.byc.service.RoleService;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

import jakarta.validation.Valid;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Component
@AllArgsConstructor
public class AdminDTOConverter {

    private ModelMapper modelMapper;
    private final RoleService roleService;

    // for get data
    public AdminDetailResponse convertToListResponse(AppAdmin data) {
        // mapping Entity with DTO Entity
        AdminDetailResponse dto = modelMapper.map(data, AdminDetailResponse.class);
        // get role name
        dto.setRoleName(data.getRole().getName());
        // return
        return dto;
    }

    // for create data
    public AppAdmin convertToCreateRequest(@Valid AdminCreateRequest dto) {
        // mapping DTO Entity with Entity
        AppAdmin data = modelMapper.map(dto, AppAdmin.class);
        // return
        return data;
    }

    // for update data
    public void convertToUpdateRequest(AppAdmin data, @Valid AdminUpdateRequest dto) {
        // mapping DTO Entity with Entity
        modelMapper.map(dto, data);
        // set updated_at
        data.setUpdatedAt(LocalDateTime.now());
    }

    public AdminCmsDetailResponse convertToInfoResponse(AppAdmin data) {
        // mapping Entity with DTO Entity
        AdminCmsDetailResponse dto = modelMapper.map(data, AdminCmsDetailResponse.class);
        // get role and permission
        dto.setRoleName(data.getRole().getName());
        Role role = data.getRole();
        List<SimpleGrantedAuthority> roleHasPermissionList = role.getAuthorities();
        // get list permission
        List<String> permissions = roleHasPermissionList.stream()
                        .map(SimpleGrantedAuthority::getAuthority)
                                .collect(Collectors.toList());

        dto.setPermissions(permissions);
        // return
        return dto;
    }
}
