package com.bca.byc.seeder;

import com.bca.byc.entity.Permission;
import com.bca.byc.entity.Role;
import com.bca.byc.entity.RoleHasPermissions;
import com.bca.byc.entity.RolePermissionId;
import com.bca.byc.repository.PermissionRepository;
import com.bca.byc.repository.RoleRepository;
import com.bca.byc.repository.RoleHasPermissionsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
public class RoleSeeder implements CommandLineRunner {

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private PermissionRepository permissionRepository;

    @Autowired
    private RoleHasPermissionsRepository roleHasPermissionsRepository;

    @Override
    public void run(String... args) throws Exception {
        Map<String, List<String>> actionsByResource = new HashMap<>();
        actionsByResource.put("admins", Arrays.asList("viewAny", "view", "create", "update", "delete"));
        actionsByResource.put("users", Arrays.asList("viewAny", "view", "create", "update", "delete"));
        actionsByResource.put("roles", Arrays.asList("viewAny", "create", "update", "delete"));
        actionsByResource.put("activities", Arrays.asList("viewAny", "view"));

        List<Permission> allPermissions = new ArrayList<>();

        actionsByResource.forEach((resource, actions) -> {
            actions.forEach(action -> {
                String permissionName = resource + "." + action;
                Optional<Permission> existingPermission = permissionRepository.findByName(permissionName);
                if (!existingPermission.isPresent()) {
                    Permission permission = new Permission();
                    permission.setGuardName("admin");
                    permission.setName(permissionName);
                    allPermissions.add(permissionRepository.save(permission));
                } else {
                    allPermissions.add(existingPermission.get());
                }
            });
        });

        // Check if ROLE_ADMIN exists, if not create it
        Role adminRole = roleRepository.findByName("ROLE_ADMIN").orElseGet(() -> {
            Role role = new Role();
            role.setName("ROLE_ADMIN");
            return roleRepository.save(role);
        });

        // Assign all permissions to ROLE_ADMIN
        allPermissions.forEach(permission -> {
            RolePermissionId rolePermissionId = new RolePermissionId();
            rolePermissionId.setRoleId(adminRole.getId());
            rolePermissionId.setPermissionId(permission.getId());

            RoleHasPermissions roleHasPermissions = new RoleHasPermissions();
            roleHasPermissions.setId(rolePermissionId);
            roleHasPermissions.setRole(adminRole);
            roleHasPermissions.setPermission(permission);

            roleHasPermissionsRepository.save(roleHasPermissions);
        });

        // Check if ROLE_USER exists, if not create it
        if (!roleRepository.findByName("ROLE_USER").isPresent()) {
            Role userRole = new Role();
            userRole.setName("ROLE_USER");
            roleRepository.save(userRole);
        }
    }
}
