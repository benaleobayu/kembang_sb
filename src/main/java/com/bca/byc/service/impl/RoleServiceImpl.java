package com.bca.byc.service.impl;

import com.bca.byc.converter.RoleDTOConverter;
import com.bca.byc.converter.dictionary.PageCreateReturn;
import com.bca.byc.entity.Permission;
import com.bca.byc.entity.Role;
import com.bca.byc.entity.RoleHasPermission;
import com.bca.byc.exception.BadRequestException;
import com.bca.byc.model.PrivilegeRoleCreateUpdateRequest;
import com.bca.byc.model.RoleCreateUpdateRequest;
import com.bca.byc.model.RoleDetailResponse;
import com.bca.byc.model.RoleListResponse;
import com.bca.byc.repository.PermissionRepository;
import com.bca.byc.repository.RoleHasPermissionRepository;
import com.bca.byc.repository.RoleRepository;
import com.bca.byc.repository.handler.HandlerRepository;
import com.bca.byc.response.ResultPageResponseDTO;
import com.bca.byc.service.RoleService;
import com.bca.byc.util.PaginationUtil;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@Service
@AllArgsConstructor
public class RoleServiceImpl implements RoleService {

    private final RoleRepository roleRepository;
    private final PermissionRepository permissionRepository;
    private final RoleHasPermissionRepository roleHasPermissionRepository;

    private final RoleDTOConverter converter;
    private final EntityManager entityManager;

    @Override
    public RoleDetailResponse findDataById(Long id) throws BadRequestException {
        Role data = roleRepository.findById(id)
                .orElseThrow(() -> new BadRequestException("Role not found"));

        return converter.convertToDetailResponse(data);
    }

    @Override
    public List<RoleListResponse> findAllData() {
        // Get the list
        List<Role> datas = roleRepository.findAll();

        // stream into the list
        return datas.stream()
                .map(converter::convertToListResponse)
                .collect(Collectors.toList());
    }

    @Override
    public void saveData(@Valid RoleCreateUpdateRequest dto) throws BadRequestException {
        // set entity to add with model mapper
        Role data = converter.convertToCreateRequest(dto);
        // save data
        roleRepository.save(data);
    }

    @Override
    @Transactional
    public void updateData(Long roleId, RoleCreateUpdateRequest dto) throws BadRequestException {
        // Check if the role exists and get it
        Role role = HandlerRepository.getEntityById(roleId, roleRepository, "Role not found");

        // Update name if provided
        if (dto.getName() != null) {
            role.setName(dto.getName().toUpperCase());
        }

        // Update status if provided
        if (dto.getStatus() != null) {
            role.setStatus(dto.getStatus());
        }

        // Retrieve existing permissions for the role
        List<RoleHasPermission> existingPermissions = roleHasPermissionRepository.findByRole(role);
        Set<Long> existingPermissionIds = existingPermissions.stream()
                .map(rhp -> rhp.getPermission().getId())
                .collect(Collectors.toSet());

        // Step 1: Remove permissions that are inactive
        if (dto.getPermissions() != null) {
            for (PrivilegeRoleCreateUpdateRequest permissionDto : dto.getPermissions()) {
                Long permissionId = permissionDto.getPermissionId();

                if (permissionId != null && !permissionDto.isActive() && existingPermissionIds.contains(permissionId)) {
                    Permission permission = HandlerRepository.getEntityById(permissionId, permissionRepository, "Permission not found");

                    RoleHasPermission roleHasPermission = roleHasPermissionRepository.findByRoleAndPermission(role, permission);
                    if (roleHasPermission != null) {
                        // Log the deletion
                        log.info("Deleting RoleHasPermission: roleId={}, permissionId={}", role.getId(), permissionId);
                        roleHasPermissionRepository.delete(roleHasPermission);
                    } else {
                        log.warn("RoleHasPermission not found for roleId: {} and permissionId: {}", role.getId(), permissionId);
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

                    RoleHasPermission roleHasPermission = new RoleHasPermission(role, permission);
                    roleHasPermissionRepository.save(roleHasPermission);
                }
            }
        }

        role.setUpdatedAt(LocalDateTime.now());
        roleRepository.save(role);
    }


    @Override
    public void deleteData(Long id) throws BadRequestException {
        // delete data
        if (!roleRepository.existsById(id)) {
            throw new BadRequestException("Role not found");
        } else {
            roleRepository.deleteById(id);
        }
    }

    @Override
    public ResultPageResponseDTO<RoleListResponse> listData(Integer pages, Integer limit, String sortBy, String direction, String keyword) {
        keyword = StringUtils.isEmpty(keyword) ? "%" : (keyword + "%");
        Sort sort = Sort.by(new Sort.Order(PaginationUtil.getSortBy(direction), sortBy));
        Pageable pageable = PageRequest.of(pages, limit, sort);
        Page<Role> pageResult = roleRepository.findByNameLikeIgnoreCase(keyword, pageable);
        List<RoleListResponse> dtos = pageResult.stream().map((c) -> {
            RoleListResponse dto = converter.convertToListResponse(c);
            return dto;
        }).collect(Collectors.toList());

        return PageCreateReturn.create(pageResult, dtos);
    }

    @Override
    public List<String> getAdminRoles() {
        List<String> adminRoles = roleRepository.findAllAdminRoles();
        return adminRoles.stream()
                .map(String::toUpperCase)
                .collect(Collectors.toList());
    }


}
