package com.bca.byc.service.impl;

import com.bca.byc.entity.Permission;
import com.bca.byc.model.PermissionListResponse;
import com.bca.byc.repository.PermissionRepository;
import com.bca.byc.response.PermissionResponse;
import com.bca.byc.service.PermissionService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class PermissionServiceImpl implements PermissionService {

    private final PermissionRepository permissionRepository;

    @Override
    public List<PermissionListResponse> findAllData() {
        // Fetch all permissions
        List<Permission> allPermissions = permissionRepository.findAll();

        // Define (view, create, read, update, delete)
        List<String> defaultPermissions = Arrays.asList("view", "create", "read", "update", "delete", "export");

        // Group all permissions by category
        Map<String, List<Permission>> permissionsByCategory = allPermissions.stream()
                .collect(Collectors.groupingBy(permission -> {
                    String[] parts = permission.getName().split("\\.");
                    return parts.length > 1 ? parts[0] : "other"; // default to "other" if no category
                }));

        // Initialize the response list
        List<PermissionListResponse> responseList = new ArrayList<>();

        // For each category
        for (Map.Entry<String, List<Permission>> entry : permissionsByCategory.entrySet()) {
            String category = entry.getKey();
            List<Permission> permissionsInCategory = entry.getValue();

            List<PermissionResponse> permissionResponses = defaultPermissions.stream()
                    .map(defaultPermission -> {
                        // Check if the current permission exists in the category
                        return permissionsInCategory.stream()
                                .filter(permission -> permission.getName().equals(category + "." + defaultPermission))
                                .findFirst()
                                .map(matchingPermission -> new PermissionResponse(
                                        matchingPermission.getId(),
                                        defaultPermission,
                                        false
                                ))
                                .orElse(new PermissionResponse(null, defaultPermission, true));
                    })
                    .collect(Collectors.toList());

            // Create and add the new PermissionListResponse to the list
            responseList.add(new PermissionListResponse(category, permissionResponses));
        }

        return responseList;
    }
}