package com.bca.byc.seeder;


import com.bca.byc.entity.Permission;
import com.bca.byc.entity.Role;
import com.bca.byc.entity.RoleHasPermission;
import com.bca.byc.entity.RoleHasPermissionId;
import com.bca.byc.repository.PermissionRepository;
import com.bca.byc.repository.RoleHasPermissionRepository;
import com.bca.byc.repository.RoleRepository;
import lombok.AllArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
@Order(1)
@AllArgsConstructor
public class RoleSeeder implements CommandLineRunner {

    private final RoleRepository roleRepository;
    private final PermissionRepository permissionRepository;
    private final RoleHasPermissionRepository roleHasPermissionRepository;


    @Override
    public void run(String... args) throws Exception {
        Map<String, List<String>> actionByResource = new HashMap<>();
        actionByResource.put("admin", List.of("view", "create", "read", "update", "delete"));
        actionByResource.put("user", List.of("view", "create", "read", "update", "delete"));
        actionByResource.put("role", List.of("view", "create", "read", "update", "delete"));
        actionByResource.put("expect_category", List.of("view", "create", "read", "update", "delete"));
        actionByResource.put("expect_item", List.of("view", "create", "read", "update", "delete"));

        List<Permission> allPermissions = new ArrayList<>();

        actionByResource.forEach((resource, actions) -> {
            actions.forEach(action -> {
                String permissionName = resource + "." + action;
                Optional<Permission> existingPermission = permissionRepository.findByName(permissionName);
                if (existingPermission.isEmpty()) {
                    Permission permission = new Permission();
                    permission.setName(permissionName);
                    permission.setGuardName("admin");
                    allPermissions.add(permissionRepository.save(permission));
                } else {
                    allPermissions.add(existingPermission.get());
                }
            });
        });

        // check role exist
        Role adminRole = roleRepository.findByName("ADMIN")
                .orElseGet(() -> {
                    Role role = new Role();
                    role.setName("ADMIN");
                    return roleRepository.save(role);
                });

        // assign
        allPermissions.forEach(permission -> {
            RoleHasPermissionId rolePermissionId = new RoleHasPermissionId();
            rolePermissionId.setRoleId(adminRole.getId());
            rolePermissionId.setPermissionId(permission.getId());

            RoleHasPermission roleHasPermissions = new RoleHasPermission();
            roleHasPermissions.setId(rolePermissionId);
            roleHasPermissions.setRole(adminRole);
            roleHasPermissions.setPermission(permission);

            roleHasPermissionRepository.save(roleHasPermissions);
        });

        // check if role exist
        if (!roleRepository.findByName("USER").isPresent()) {
            Role role = new Role();
            role.setName("USER");
            roleRepository.save(role);
        }

    }
}
