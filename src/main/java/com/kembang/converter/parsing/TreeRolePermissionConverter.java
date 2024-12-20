package com.kembang.converter.parsing;

import com.kembang.entity.Permission;
import com.kembang.entity.RoleHasPermission;
import com.kembang.model.PermissionListResponse;
import com.kembang.repository.PermissionRepository;
import com.kembang.response.PermissionResponse;

import java.util.*;
import java.util.stream.Collectors;

public class TreeRolePermissionConverter {

    private static final List<String> DEFAULT_PERMISSIONS = Arrays.asList("view", "create", "read", "update", "delete", "export", "import", "approval");

    public List<PermissionListResponse> convertRolePermissions(List<RoleHasPermission> roleHasPermissionList, PermissionRepository permissionRepository, String type) {
        // Group permissions by the prefix (category) before the dot (e.g., role, admin, user)
        Map<String, List<RoleHasPermission>> permissionsByCategory = roleHasPermissionList.stream()
                .collect(Collectors.groupingBy(roleHasPermission -> {
                    String[] parts = roleHasPermission.getPermission().getName().split("\\.");
                    return parts.length > 1 ? parts[0] : "other"; // default to "other" if no category
                }));

        List<PermissionListResponse> menuNames = new ArrayList<>();
        // Iterate over each category
        for (Map.Entry<String, List<RoleHasPermission>> entry : permissionsByCategory.entrySet()) {
            String category = entry.getKey();
            List<RoleHasPermission> permissions = entry.getValue();

            PermissionListResponse menuName = new PermissionListResponse();
            menuName.setMenuName(category);

            // Create a list of PermissionResponse for each category
            List<PermissionResponse> permissionDetails = new ArrayList<>();

            // Iterate over default permissions
            for (String defaultPermission : DEFAULT_PERMISSIONS) {
                // Check if the role has the current permission
                Optional<RoleHasPermission> matchingPermission = permissions.stream()
                        .filter(roleHasPermission -> roleHasPermission.getPermission().getName().equals(category + "." + defaultPermission))
                        .findFirst();

                // Create a new PermissionResponse
                PermissionResponse permissionDetail = new PermissionResponse();
                boolean isActive = matchingPermission.isPresent();
                Permission permissionId = matchingPermission.map(RoleHasPermission::getPermission).orElse(null);
                if (matchingPermission.isPresent()) {
                    permissionDetail.setPermissionId(permissionId.getId());
                    permissionDetail.setPermissionName(defaultPermission);
                    permissionDetail.setDisabled(!permissionRepository.existsByName(permissionId.getName())); // permission exists, not disabled
                    permissionDetail.setActive(isActive);
                } else {
                    Permission permissionOnNull = permissionRepository.findByName(category + "." + defaultPermission).orElse(null);
                    permissionDetail.setPermissionId(type.equals("info") ? null : permissionRepository.findByName(category + "." + defaultPermission).map(Permission::getId).orElse(null)); // no ID because it doesn't exist
                    permissionDetail.setPermissionName(defaultPermission);
                    permissionDetail.setDisabled(!permissionRepository.existsByName(category + "." + defaultPermission)); // permission doesn't exist, disabled
                    permissionDetail.setActive(false);
                }

                permissionDetails.add(permissionDetail);
            }

            // Add the permissionDetails to the menu name
            menuName.setPermissions(permissionDetails);
            menuNames.add(menuName);
        }

        return menuNames;
    }
}
