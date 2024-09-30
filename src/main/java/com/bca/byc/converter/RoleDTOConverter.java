package com.bca.byc.converter;

import com.bca.byc.converter.parsing.GlobalConverter;
import com.bca.byc.converter.parsing.TreeRolePermissionConverter;
import com.bca.byc.entity.AppAdmin;
import com.bca.byc.entity.Permission;
import com.bca.byc.entity.Role;
import com.bca.byc.entity.RoleHasPermission;
import com.bca.byc.model.*;
import com.bca.byc.repository.PermissionRepository;
import com.bca.byc.repository.RoleHasPermissionRepository;
import com.bca.byc.repository.RoleRepository;
import com.bca.byc.repository.handler.HandlerRepository;
import com.bca.byc.response.PermissionResponse;
import jakarta.persistence.EntityManager;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@Component
@AllArgsConstructor
public class RoleDTOConverter {

    private final RoleRepository roleRepository;
    private final PermissionRepository permissionRepository;
    private final RoleHasPermissionRepository roleHasPermissionRepository;
    private ModelMapper modelMapper;
    private EntityManager entityManager;

    // for get data
    public RoleListResponse convertToListResponse(Role data) {
        GlobalConverter converter = new GlobalConverter();
        // mapping Entity with DTO Entity
        RoleListResponse dto = new RoleListResponse();
        dto.setName(data.getName());
        dto.setStatus(data.getIsActive());
        dto.setOrders(data.getOrders());
        converter.CmsIDTimeStampResponse(dto, data);

        // return the DTO
        return dto;
    }

    public RoleDetailResponse convertToDetailResponse(Role data) {
        // mapping Entity with DTO Entity
        RoleDetailResponse dto = new RoleDetailResponse();
        dto.setId(data.getSecureId());
        dto.setIndex(data.getId());
        dto.setName(data.getName());

        List<RoleHasPermission> roleHasPermissionList = data.getRolePermission();
        TreeRolePermissionConverter converter = new TreeRolePermissionConverter();
        List<PermissionListResponse> menuNames = converter.convertRolePermissions(roleHasPermissionList);

        // Set the grouped permissions to the DTO
        dto.setPermissions(menuNames);

        // return the DTO
        return dto;
    }

    // for create data
    public Role convertToCreateRequest(@Valid RoleCreateUpdateRequest dto, AppAdmin admin) {
        // Mapping DTO to Entity
        Role data = new Role();
        data.setName(dto.getName().toUpperCase());
        data.setIsActive(dto.getStatus());

        // Save the Role entity to the database first
        Role savedRole = roleRepository.save(data); // Ensure it's saved to get a valid ID

        // Retrieve existing permissions for the role
        List<RoleHasPermission> existingPermissions = roleHasPermissionRepository.findByRole(savedRole);
        Set<Long> existingPermissionIds = existingPermissions.stream()
                .map(rhp -> rhp.getPermission().getId())
                .collect(Collectors.toSet());

        // Step 1: Remove permissions that are inactive
        if (dto.getPermissions() != null) {
            for (PrivilegeRoleCreateUpdateRequest permissionDto : dto.getPermissions()) {
                Long permissionId = permissionDto.getPermissionId();

                if (permissionId != null && !permissionDto.isActive() && existingPermissionIds.contains(permissionId)) {
                    Permission permission = HandlerRepository.getEntityById(permissionId, permissionRepository, "Permission not found");

                    RoleHasPermission roleHasPermission = roleHasPermissionRepository.findByRoleAndPermission(savedRole, permission);
                    if (roleHasPermission != null) {
                        // Log the deletion
                        log.info("Deleting RoleHasPermission: roleId={}, permissionId={}", savedRole.getId(), permissionId);
                        roleHasPermissionRepository.delete(roleHasPermission);
                    } else {
                        log.warn("RoleHasPermission not found for roleId: {} and permissionId: {}", savedRole.getId(), permissionId);
                    }
                }
            }
        }

        // Flush and clear the session to apply deletions
        entityManager.flush();
        entityManager.clear();

        // Step 2: Add permissions that are active and not already present
        if (dto.getPermissions() != null) {
            for (PrivilegeRoleCreateUpdateRequest permissionDto : dto.getPermissions()) {
                Long permissionId = permissionDto.getPermissionId();

                if (permissionId != null && permissionDto.isActive() && !existingPermissionIds.contains(permissionId)) {
                    Permission permission = HandlerRepository.getEntityById(permissionId, permissionRepository, "Permission not found");

                    RoleHasPermission roleHasPermission = new RoleHasPermission(savedRole, permission);
                    roleHasPermissionRepository.save(roleHasPermission);
                }
            }
        }

        savedRole.setCreatedBy(admin);
        savedRole.setUpdatedAt(LocalDateTime.now());

        // Return the saved role
        return savedRole;
    }


    // update
    public void convertToUpdateRequest(Role data, @Valid RoleCreateUpdateRequest dto, AppAdmin admin) {
        // Update fields based on the DTO
        data.setName(dto.getName().toUpperCase());
        data.setIsActive(dto.getStatus());

        // Retrieve existing permissions for the role
        List<RoleHasPermission> existingPermissions = roleHasPermissionRepository.findByRole(data);
        Set<Long> existingPermissionIds = existingPermissions.stream()
                .map(rhp -> rhp.getPermission().getId())
                .collect(Collectors.toSet());

        // Lists to hold new permission IDs
        List<Long> addPermissionIds = new ArrayList<>();
        List<Long> removePermissionIds = new ArrayList<>();

        // Process permissions from the DTO
        if (dto.getPermissions() != null) {
            for (PrivilegeRoleCreateUpdateRequest permissionDto : dto.getPermissions()) {
                Long permissionId = permissionDto.getPermissionId();

                if (permissionId != null) {
                    if (permissionDto.isActive()) {
                        // Add to new permissions if active and not already present
                        if (!existingPermissionIds.contains(permissionId)) {
                            addPermissionIds.add(permissionId);
                        }
                    } else {
                        // Add to remove permissions if inactive and already present
                        if (existingPermissionIds.contains(permissionId)) {
                            removePermissionIds.add(permissionId);
                        }
                    }
                }
            }
        }

        // Step 1: Remove inactive permissions
//        for (Long permissionId : removePermissionIds) {
//            Permission permission = HandlerRepository.getEntityById(permissionId, permissionRepository, "Permission not found");
//            RoleHasPermission roleHasPermission = roleHasPermissionRepository.findByRoleAndPermission(data, permission);
//            if (roleHasPermission != null) {
//                log.info("Deleting RoleHasPermission: roleId={}, permissionId={}", data.getId(), permissionId);
//                roleHasPermissionRepository.delete(roleHasPermission);
//            }
//        }
        for (Long permissionId : removePermissionIds) {
            RoleHasPermission roleHasPermission = roleHasPermissionRepository.findByRoleIdAndPermissionId(data.getId(), permissionId);
            if (roleHasPermission != null) {
                roleHasPermissionRepository.delete(roleHasPermission);
            }
        }

        // Flush and clear the session to apply deletions
        entityManager.flush();
        entityManager.clear();

        // Step 2: Add new active permissions
        for (Long permissionId : addPermissionIds) {
            Permission permission = HandlerRepository.getEntityById(permissionId, permissionRepository, "Permission not found");
            RoleHasPermission roleHasPermission = new RoleHasPermission(data, permission);
            roleHasPermissionRepository.save(roleHasPermission);
        }
        log.info("addList : {}", addPermissionIds);
        log.info("removeList : {}", addPermissionIds);

        // Set updated metadata
        data.setUpdatedBy(admin);
        data.setUpdatedAt(LocalDateTime.now());
    }

}
