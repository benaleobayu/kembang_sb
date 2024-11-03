package com.bca.byc.service.impl;

import com.bca.byc.converter.RoleDTOConverter;
import com.bca.byc.converter.dictionary.PageCreateReturnApps;
import com.bca.byc.converter.parsing.GlobalConverter;
import com.bca.byc.entity.AppAdmin;
import com.bca.byc.entity.Role;
import com.bca.byc.exception.BadRequestException;
import com.bca.byc.model.RoleCreateUpdateRequest;
import com.bca.byc.model.RoleDetailResponse;
import com.bca.byc.model.RoleListResponse;
import com.bca.byc.model.search.ListOfFilterPagination;
import com.bca.byc.model.search.SavedKeywordAndPageable;
import com.bca.byc.repository.RoleHasPermissionRepository;
import com.bca.byc.repository.RoleRepository;
import com.bca.byc.repository.auth.AppAdminRepository;
import com.bca.byc.repository.handler.HandlerRepository;
import com.bca.byc.response.ResultPageResponseDTO;
import com.bca.byc.security.util.ContextPrincipal;
import com.bca.byc.service.RoleService;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

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
        ListOfFilterPagination filter = new ListOfFilterPagination(
                keyword
        );
        SavedKeywordAndPageable set = GlobalConverter.createPageable(pages, limit, sortBy, direction, keyword, filter);

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
