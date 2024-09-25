package com.bca.byc.service.impl;

import com.bca.byc.converter.RoleDTOConverter;
import com.bca.byc.converter.dictionary.PageCreateReturn;
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
import com.bca.byc.response.ResultPageResponseDTO;
import com.bca.byc.service.RoleService;
import com.bca.byc.util.PaginationUtil;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class RoleServiceImpl implements RoleService {

    private final RoleRepository roleRepository;
    private final PermissionRepository permissionRepository;
    private final RoleHasPermissionRepository roleHasPermissionRepository;

    private final RoleDTOConverter converter;

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
    public void saveData(@Valid RoleCreateRequest dto) throws BadRequestException {
        // set entity to add with model mapper
        Role data = converter.convertToCreateRequest(dto);
        // save data
        roleRepository.save(data);
    }

    @Override
    @Transactional
    public void updateData(Long roleId, RoleUpdateRequest dto) throws BadRequestException {
        // check exist and get
        Role role = roleRepository.findById(roleId)
                .orElseThrow(() -> new BadRequestException("INVALID Role ID"));

        // update
        if (dto.getAddPermissionIds() != null) {
            for (Long permissionId : dto.getAddPermissionIds()) {
                Permission permission = permissionRepository.findById(permissionId)
                        .orElseThrow(() -> new RuntimeException("Permission not found"));
                RoleHasPermission roleHasPermission = new RoleHasPermission(role, permission);
                if (!roleHasPermissionRepository.existsByRoleAndPermission(role, permission)) {
                    roleHasPermissionRepository.save(roleHasPermission);
                }
            }
        }

        // remove
        if (dto.getRemovePermissionIds() != null) {
            for (Long permissionId : dto.getRemovePermissionIds()) {
                RoleHasPermission roleHasPermission = roleHasPermissionRepository.findByRoleIdAndPermissionId(roleId, permissionId);
                if (roleHasPermission != null) {
                    roleHasPermissionRepository.delete(roleHasPermission);
                }
            }
        }

        // Updatename
        if (dto.getName() != null) {
            role.setName(dto.getName().toUpperCase());
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
