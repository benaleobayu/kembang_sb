package com.bca.byc.service.impl;

import com.bca.byc.entity.Permission;
import com.bca.byc.model.RoleDetailResponse;
import com.bca.byc.repository.PermissionRepository;
import com.bca.byc.response.PermissionResponse;
import com.bca.byc.service.PermissionService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class PermissionServiceImpl implements PermissionService {

    private final PermissionRepository permissionRepository;

    @Override
    public Map<String, List<PermissionResponse>> findAllData() {
        // Fetch all permissions
        List<Permission> allPermissions = permissionRepository.findAll();

        // Define (view, create, read, update, delete)
        List<String> defaultPermissions = Arrays.asList("view", "create", "read", "update", "delete");

        // Group all permissions by category
        Map<String, List<Permission>> permissionsByCategory = allPermissions.stream()
                .collect(Collectors.groupingBy(permission -> {
                    String[] parts = permission.getName().split("\\.");
                    return parts.length > 1 ? parts[0] : "other"; // default to "other" if no category
                }));

        // Initialize
        Map<String, List<PermissionResponse>> permissionGroups = new HashMap<>();

        // For each category
        for (Map.Entry<String, List<Permission>> entry : permissionsByCategory.entrySet()) {
            String category = entry.getKey();
            List<Permission> permissionsInCategory = entry.getValue();

            List<PermissionResponse> permissionResponses = new ArrayList<>();

            for (String defaultPermission : defaultPermissions) {
                // Check if the current permission exists in the category
                Optional<Permission> matchingPermission = permissionsInCategory.stream()
                        .filter(permission -> permission.getName().equals(category + "." + defaultPermission))
                        .findFirst();

                // Create a new PermissionResponse
                PermissionResponse permissionResponse = new PermissionResponse();

                if (matchingPermission.isPresent()) {
                    permissionResponse.setPermissionId(matchingPermission.get().getId());
                    permissionResponse.setPermissionName(defaultPermission);
                    permissionResponse.setDisabled(false);
                } else {
                    permissionResponse.setPermissionId(null);
                    permissionResponse.setPermissionName(defaultPermission);
                    permissionResponse.setDisabled(true);
                }

                permissionResponses.add(permissionResponse);
            }

            permissionGroups.put(category, permissionResponses);
        }

        return permissionGroups;
    }
}