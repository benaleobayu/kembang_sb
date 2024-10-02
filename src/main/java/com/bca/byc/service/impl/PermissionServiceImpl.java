package com.bca.byc.service.impl;

import com.bca.byc.entity.Permission;
import com.bca.byc.model.PermissionListResponse;
import com.bca.byc.repository.PermissionRepository;
import com.bca.byc.response.PermissionResponse;
import com.bca.byc.response.ResultPageResponseDTO;
import com.bca.byc.service.PermissionService;
import com.bca.byc.util.PaginationUtil;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.*;
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

    @Override
    public ResultPageResponseDTO<PermissionListResponse> listDataList(Integer pages, Integer limit, String sortBy, String direction, String keyword) {
        // Step 1: Fetch all data
        List<PermissionListResponse> allPermissions = findAllData();

        // Step 2: Filter based on keyword
        if (!StringUtils.isEmpty(keyword)) {
            allPermissions = allPermissions.stream()
                    .filter(permissionListResponse ->
                            permissionListResponse.getMenuName().toLowerCase().contains(keyword.toLowerCase()) ||
                                    permissionListResponse.getPermissions().stream()
                                            .anyMatch(permissionResponse -> permissionResponse.getPermissionName().toLowerCase().contains(keyword.toLowerCase()))
                    )
                    .collect(Collectors.toList());
        }

        // Step 3: Sort
        allPermissions.sort(Comparator.comparing(PermissionListResponse::getMenuName)); // Example sort by menu name

        // Step 4: Paginate the results
        long totalItems = allPermissions.size(); // Use long for totalItems
        int totalPages = (int) Math.ceil((double) totalItems / limit);

        // Calculate fromIndex and toIndex for pagination
        int fromIndex = Math.min(pages * limit, (int) totalItems); // Cast totalItems to int for indexing
        int toIndex = Math.min(fromIndex + limit, (int) totalItems);

        // Create the sublist for the paginated permissions
        List<PermissionListResponse> paginatedPermissions = allPermissions.subList(fromIndex, toIndex);

        // Step 5: Create the result DTO
        return PaginationUtil.createResultPageDTO(
                totalItems, // total items as Long
                paginatedPermissions, // the current page items
                pages + 1, // current page (1-based)
                pages > 0 ? pages : 0, // previous page
                pages < totalPages - 1 ? pages + 2 : totalPages - 1, // next page
                1, // first page
                totalPages, // last page
                limit // per page
        );
    }

}