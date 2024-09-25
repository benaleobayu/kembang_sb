package com.bca.byc.converter;

import com.bca.byc.entity.AppAdmin;
import com.bca.byc.entity.Role;
import com.bca.byc.entity.RoleHasPermission;
import com.bca.byc.model.AdminCmsDetailResponse;
import com.bca.byc.model.AdminCreateRequest;
import com.bca.byc.model.AdminDetailResponse;
import com.bca.byc.model.AdminUpdateRequest;
import com.bca.byc.response.AdminPermissionResponse;
import com.bca.byc.response.PermissionResponse;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

import static com.bca.byc.config.AppConfig.baseUrl;

@Component
@AllArgsConstructor
public class AdminDTOConverter {

    private ModelMapper modelMapper;

    // for get data
    public AdminDetailResponse convertToListResponse(AppAdmin data) {
        // mapping Entity with DTO Entity
        AdminDetailResponse dto = modelMapper.map(data, AdminDetailResponse.class);
        dto.setAvatar(data.getAvatar() != null && data.getAvatar().startsWith("uploads/") ? baseUrl + "/" + data.getAvatar() : data.getAvatar());
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
        List<String> defaultPermissions = Arrays.asList("view", "create", "read", "update", "delete");

        // Define a map to hold grouped permissions
        Map<String, List<PermissionResponse>> permissionGroups = new HashMap<>();

        // Get role's permissions
        List<RoleHasPermission> roleHasPermissionList = role.getRolePermission();

        // Group permissions by the prefix (category) before the dot (e.g., role, admin, user)
        Map<String, List<RoleHasPermission>> permissionsByCategory = roleHasPermissionList.stream()
                .collect(Collectors.groupingBy(roleHasPermission -> {
                    // Extract category by splitting the permission name at the dot
                    String[] parts = roleHasPermission.getPermission().getName().split("\\.");
                    return parts.length > 1 ? parts[0] : "other"; // default to "other" if no category
                }));

        List<AdminCmsDetailResponse.MenuName> menuNames = new ArrayList<>();

        // Iterate over each category (e.g., role, admin, user)
        for (Map.Entry<String, List<RoleHasPermission>> entry : permissionsByCategory.entrySet()) {
            String category = entry.getKey();
            List<RoleHasPermission> permissions = entry.getValue();

            AdminCmsDetailResponse.MenuName menuName = new AdminCmsDetailResponse.MenuName();
            menuName.setMenuName(category);

            // Create a list of PermissionResponse for each category
            List<PermissionResponse> permissionDetails = new ArrayList<>();

            // Iterate over default permissions (view, create, read, update, delete)
            for (String defaultPermission : defaultPermissions) {
                // Check if the role has the current permission (e.g., role.view)
                Optional<RoleHasPermission> matchingPermission = permissions.stream()
                        .filter(roleHasPermission -> roleHasPermission.getPermission().getName().equals(category + "." + defaultPermission))
                        .findFirst();

                // Create a new PermissionResponse
                PermissionResponse permissionDetail = new PermissionResponse();

                // If the permission exists in the role, set its details
                if (matchingPermission.isPresent()) {
                    permissionDetail.setPermissionId(matchingPermission.get().getPermission().getId());
                    permissionDetail.setPermissionName(defaultPermission);
                    permissionDetail.setDisabled(false); // permission exists, not disabled
                } else {
                    // If the permission does not exist, set disabled to true
                    permissionDetail.setPermissionId(null); // no ID because it doesn't exist
                    permissionDetail.setPermissionName(defaultPermission);
                    permissionDetail.setDisabled(true); // permission doesn't exist, disabled
                }

                permissionDetails.add(permissionDetail);
            }

            // Add the permissionDetails to the permissionGroups map
            menuName.setPermissions(permissionDetails);

            menuNames.add(menuName);
        }

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
