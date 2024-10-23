package com.bca.byc.converter;

import com.bca.byc.converter.parsing.GlobalConverter;
import com.bca.byc.converter.parsing.TreeRolePermissionConverter;
import com.bca.byc.entity.AppAdmin;
import com.bca.byc.entity.Role;
import com.bca.byc.entity.RoleHasPermission;
import com.bca.byc.model.*;
import com.bca.byc.repository.PermissionRepository;
import com.bca.byc.response.AdminPermissionResponse;
import com.bca.byc.util.helper.Formatter;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;


@Component
@AllArgsConstructor
public class AdminDTOConverter {

    @Value("${app.base.url}")
    private String baseUrl;

    private ModelMapper modelMapper;
    private final PermissionRepository permissionRepository;

    // for get data
    public AdminDetailResponse convertToListResponse(AppAdmin data) {
        // mapping Entity with DTO Entity
        AdminDetailResponse dto = modelMapper.map(data, AdminDetailResponse.class);
        dto.setAvatar(GlobalConverter.getAvatarImage(data.getAvatar(), baseUrl));
        dto.setCover(GlobalConverter.getAvatarImage(data.getCover(), baseUrl));
        // get role name
        dto.setId(data.getSecureId());
        dto.setIndex(data.getId());
        dto.setStatus(data.getIsActive());
        dto.setIsVisible(data.getIsVisible());
        dto.setRoleId(data.getRole().getSecureId());
        dto.setRoleName(data.getRole().getName());
        dto.setCreatedAt(Formatter.formatLocalDateTime(data.getCreatedAt()));
        dto.setUpdatedAt(Formatter.formatLocalDateTime(data.getUpdatedAt()));
        // return
        return dto;
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
        List<RoleHasPermission> roleHasPermissionList = role.getRolePermission();
        TreeRolePermissionConverter converter = new TreeRolePermissionConverter();
        List<PermissionListResponse> menuNames = converter.convertRolePermissions(roleHasPermissionList, permissionRepository, "info");
        // Set the grouped permissions to the DTO
        dto.setPermissions(menuNames);
        // return
        return dto;
    }

    public AdminPermissionResponse convertToPermissionResponse(AppAdmin data) {

        AdminPermissionResponse dto = modelMapper.map(data, AdminPermissionResponse.class);

        List<String> permissions = new ArrayList<>();

        for (RoleHasPermission roleHasPermission : data.getRole().getRolePermission()) {
            String[] parts = roleHasPermission.getPermission().getName().split("\\.");
            if (parts.length > 1) {
                String category = parts[0];
                String permission = parts[1];
                permissions.add(category + "." + permission);
            }
        }

        dto.setPermissions(permissions);

        return dto;
    }
}
