package com.bca.byc.converter;

import com.bca.byc.entity.Permission;
import com.bca.byc.entity.Role;
import com.bca.byc.entity.RoleHasPermission;
import com.bca.byc.exception.BadRequestException;
import com.bca.byc.model.RoleCreateRequest;
import com.bca.byc.model.RoleDetailResponse;
import com.bca.byc.model.RoleListResponse;
import com.bca.byc.model.RoleUpdateRequest;
import com.bca.byc.repository.PermissionRepository;
import com.bca.byc.repository.RoleHasPermissionRepository;
import com.bca.byc.repository.RoleRepository;
import com.bca.byc.response.PermissionResponse;
import com.bca.byc.util.Formatter;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Component
@AllArgsConstructor
public class RoleDTOConverter {

    private ModelMapper modelMapper;

    private final RoleRepository roleRepository;
    private final PermissionRepository permissionRepository;
    private final RoleHasPermissionRepository roleHasPermissionRepository;

    // for get data
    public RoleListResponse convertToListResponse(Role data) {
        // mapping Entity with DTO Entity
        RoleListResponse dto = modelMapper.map(data, RoleListResponse.class);
        // return the DTO
        return dto;
    }

    public RoleDetailResponse convertToDetailResponse(Role data) {
        // mapping Entity with DTO Entity
        RoleDetailResponse dto = modelMapper.map(data, RoleDetailResponse.class);

        // Define the default permissions (view, create, read, update, delete)
        List<String> defaultPermissions = Arrays.asList("view", "create", "read", "update", "delete");

        // Define a map to hold grouped permissions
        Map<String, List<PermissionResponse>> permissionGroups = new HashMap<>();

        // Get role's permissions
        List<RoleHasPermission> roleHasPermissionList = data.getRolePermission();

        // Group permissions by the prefix (category) before the dot (e.g., role, admin, user)
        Map<String, List<RoleHasPermission>> permissionsByCategory = roleHasPermissionList.stream()
                .collect(Collectors.groupingBy(roleHasPermission -> {
                    // Extract category by splitting the permission name at the dot
                    String[] parts = roleHasPermission.getPermission().getName().split("\\.");
                    return parts.length > 1 ? parts[0] : "other"; // default to "other" if no category
                }));

        // Iterate over each category (e.g., role, admin, user)
        for (Map.Entry<String, List<RoleHasPermission>> entry : permissionsByCategory.entrySet()) {
            String category = entry.getKey();
            List<RoleHasPermission> permissions = entry.getValue();

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
            permissionGroups.put(category, permissionDetails);
        }

        // Set the grouped permissions to the DTO
        dto.setPermissions(permissionGroups);

        // return the DTO
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

        // Manually add new permissions to the role
        if (dto.getAddPermissionIds() != null) {
            for (Long permissionId : dto.getAddPermissionIds()) {
                Permission permission = permissionRepository.findById(permissionId)
                        .orElseThrow(() -> new RuntimeException("Permission not found"));
                RoleHasPermission roleHasPermission = new RoleHasPermission(data, permission);
                if (!roleHasPermissionRepository.existsByRoleAndPermission(data, permission)) {
                    roleHasPermissionRepository.save(roleHasPermission);
                }
            }
        }

        // Manually remove permissions from the role
        if (dto.getRemovePermissionIds() != null) {
            for (Long permissionId : dto.getRemovePermissionIds()) {
                RoleHasPermission roleHasPermission = roleHasPermissionRepository.findByRoleIdAndPermissionId(data.getId(), permissionId);
                if (roleHasPermission != null) {
                    roleHasPermissionRepository.delete(roleHasPermission);
                }
            }
        }

        // Update other fields like the role name if necessary
        if (dto.getName() != null) {
            data.setName(dto.getName());
        }

        roleRepository.save(data);

        // set updated_at
        data.setUpdatedAt(LocalDateTime.now());
    }
}
