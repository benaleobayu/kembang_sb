package com.bca.byc.service.impl;

import com.bca.byc.converter.RoleDTOConverter;
import com.bca.byc.converter.dictionary.PageCreateReturn;
import com.bca.byc.entity.AppAdmin;
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
import com.bca.byc.repository.auth.AppAdminRepository;
import com.bca.byc.repository.handler.HandlerRepository;
import com.bca.byc.response.ResultPageResponseDTO;
import com.bca.byc.security.util.ContextPrincipal;
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

    private final AppAdminRepository adminRepository;
    private final RoleRepository roleRepository;
    private final PermissionRepository permissionRepository;
    private final RoleHasPermissionRepository roleHasPermissionRepository;

    private final RoleDTOConverter converter;
    private final EntityManager entityManager;

    private final String notFoundMessage = " not found.";

    @Override
    public RoleDetailResponse findDataBySecureId(String id) throws BadRequestException {
        Role data = HandlerRepository.getEntityBySecureId(id, roleRepository, "Role " + notFoundMessage);

        return converter.convertToDetailResponse(data);
    }

    @Override
    @Transactional
    public void saveData(@Valid RoleCreateUpdateRequest dto) throws BadRequestException {
        String email = ContextPrincipal.getSecureUserId();
        AppAdmin admin = HandlerRepository.getAdminByEmail(email, adminRepository, "user" + notFoundMessage);
        // set entity to add with model mapper
        Role data = converter.convertToCreateRequest(dto, admin);
        // save data
        roleRepository.save(data);
    }

    @Override
    @Transactional
    public void updateData(String roleId, RoleCreateUpdateRequest dto) throws BadRequestException {
        String email = ContextPrincipal.getSecureUserId();
        AppAdmin admin = HandlerRepository.getAdminByEmail(email, adminRepository, "user" + notFoundMessage);
        // Check if the role exists and get it
        Role role = HandlerRepository.getEntityBySecureId(roleId, roleRepository, "Role " + notFoundMessage);

        converter.convertToUpdateRequest(role, dto, admin);

        roleRepository.save(role);
    }


    @Override
    @Transactional
    public void deleteData(String id) throws BadRequestException {
        Role role = HandlerRepository.getEntityBySecureId(id, roleRepository, "Role " + notFoundMessage);
        Long roleId = role.getId();

        if (roleId == 1 || roleId == 2 || roleId == 3 || roleId == 4) {
            throw new BadRequestException("Role " + role.getName() + " cannot be deleted.");
        }

        // Delete data
        if (!roleRepository.existsById(roleId)) {
            throw new BadRequestException("Role not found");
        } else {
            roleRepository.deleteById(roleId);
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
