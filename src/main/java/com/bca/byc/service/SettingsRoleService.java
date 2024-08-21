package com.bca.byc.service;

import com.bca.byc.convert.RoleDTOConverter;
import com.bca.byc.entity.Role;
import com.bca.byc.model.cms.RoleModelDTO;
import com.bca.byc.repository.RoleRepository;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class SettingsRoleService {

    private RoleRepository roleRepository;

    private RoleDTOConverter converter;

    public List<RoleModelDTO.RoleDetailResponse> getAllRoles() {
        List<Role> datas = roleRepository.findAll();

        return datas.stream()
                .map(converter::convertToListResponse)
                .collect(Collectors.toList());
    }

    public Page<Role> getRolePagination(Pageable pageable) {
        return roleRepository.findAll(pageable);
    }

    public Optional<Role> getRoleById(Long id) {
        return roleRepository.findById(id);
    }

    public void createRole(Role role) {
        roleRepository.save(role);
    }

    public void updateRole(Long id, Role roleDetails) {
        Role role = roleRepository.findById(id).orElseThrow(() -> new RuntimeException("Role not found"));
        role.setName(roleDetails.getName());
        roleRepository.save(role);
    }

    public void deleteRole(Long id) {
        Role role = roleRepository.findById(id).orElseThrow(() -> new RuntimeException("Role not found"));
        roleRepository.delete(role);
    }
}


