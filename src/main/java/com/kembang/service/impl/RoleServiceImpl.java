package com.kembang.service.impl;

import com.kembang.converter.RoleDTOConverter;
import com.kembang.converter.dictionary.PageCreateReturnApps;
import com.kembang.converter.parsing.GlobalConverter;
import com.kembang.entity.AppAdmin;
import com.kembang.entity.Role;
import com.kembang.exception.BadRequestException;
import com.kembang.model.RoleCreateUpdateRequest;
import com.kembang.model.RoleDetailResponse;
import com.kembang.model.RoleListResponse;
import com.kembang.model.search.SavedKeywordAndPageable;
import com.kembang.repository.RoleHasPermissionRepository;
import com.kembang.repository.RoleRepository;
import com.kembang.repository.auth.AppAdminRepository;
import com.kembang.repository.handler.HandlerRepository;
import com.kembang.response.ResultPageResponseDTO;
import com.kembang.security.util.ContextPrincipal;
import com.kembang.service.RoleService;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

import static com.kembang.converter.parsing.TreeGetEntityProjection.getParsingAdminByProjection;

@Slf4j
@Service
@AllArgsConstructor
public class RoleServiceImpl implements RoleService {

    private final AppAdminRepository adminRepository;
    private final RoleRepository roleRepository;
    private final RoleHasPermissionRepository roleHasPermissionRepository;

    private final RoleDTOConverter converter;

    private final String notFoundMessage = " not found.";

    @Override
    public RoleDetailResponse findDataBySecureId(String id) throws BadRequestException {
        Role data = HandlerRepository.getEntityBySecureId(id, roleRepository, "Role " + notFoundMessage);

        return converter.convertToDetailResponse(data);
    }

    @Override
    @Transactional
    public void saveData(@Valid RoleCreateUpdateRequest dto) throws BadRequestException {
        AppAdmin admin = getParsingAdminByProjection(ContextPrincipal.getSecureId(), adminRepository);
        // set entity to add with model mapper
        Role data = converter.convertToCreateRequest(dto, admin);
        // save data
        roleRepository.save(data);
    }

    @Override
    @Transactional
    public void updateData(String roleId, RoleCreateUpdateRequest dto) throws BadRequestException {
        AppAdmin adminLogin = GlobalConverter.getAdminEntity(adminRepository);
        // Check if the role exists and get it
        Role role = HandlerRepository.getEntityBySecureId(roleId, roleRepository, "Role " + notFoundMessage);

        converter.convertToUpdateRequest(role, dto, adminLogin);

        roleRepository.save(role);
    }


    @Override
    @Transactional
    public void deleteData(String id) throws BadRequestException {
        Role role = HandlerRepository.getEntityBySecureId(id, roleRepository, "Role " + notFoundMessage);
        Long roleId = role.getId();

        // Check for protected roles
        if (roleId >= 1 && roleId <= 5) {
            throw new BadRequestException("Role " + role.getName() + " cannot be deleted.");
        }

        // Check for dependencies (e.g., users assigned to this role)
        if (adminRepository.existsByRoleId(roleId)) {
            throw new BadRequestException("Role " + role.getName() + " cannot be deleted because it is assigned to users.");
        }

        // Proceed to delete associated permissions if needed
        // Assuming permissions should be removed, otherwise skip this part
        role.getRolePermission().forEach(roleHasPermission -> {
            // Here you can decide whether to delete or keep permissions
            // If deleting:
            roleHasPermissionRepository.delete(roleHasPermission);
        });

        // Finally, delete the role
        if (roleRepository.existsById(roleId)) {
            roleRepository.deleteById(roleId);
        } else {
            throw new BadRequestException("Role not found");
        }
    }


    @Override
    public ResultPageResponseDTO<RoleListResponse> listData(Integer pages, Integer limit, String sortBy, String direction, String keyword) {

        Page<Role> firstResult = roleRepository.findByNameLikeIgnoreCase(null, null);
        SavedKeywordAndPageable set = GlobalConverter.createPageable(pages, limit, sortBy, direction, keyword, firstResult);

        Page<Role> pageResult = roleRepository.findByNameLikeIgnoreCase(set.keyword(), set.pageable());
        List<RoleListResponse> dtos = pageResult.stream().map((c) -> {
            RoleListResponse dto = converter.convertToListResponse(c);
            return dto;
        }).collect(Collectors.toList());

        return PageCreateReturnApps.create(pageResult, dtos);
    }

    @Override
    public List<String> getAdminRoles() {
        List<String> adminRoles = roleRepository.findAllAdminRoles();
        return adminRoles.stream()
                .map(String::toUpperCase)
                .collect(Collectors.toList());
    }


}
