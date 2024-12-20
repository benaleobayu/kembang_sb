package com.kembang.seeder;


import com.kembang.entity.Permission;
import com.kembang.entity.Role;
import com.kembang.entity.RoleHasPermission;
import com.kembang.entity.RoleHasPermissionId;
import com.kembang.repository.PermissionRepository;
import com.kembang.repository.RoleHasPermissionRepository;
import com.kembang.repository.RoleRepository;
import lombok.AllArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
@Order(1)
@AllArgsConstructor
public class RoleSeeder implements CommandLineRunner {
//public class RoleSeeder  {

    private final RoleRepository roleRepository;
    private final PermissionRepository permissionRepository;
    private final RoleHasPermissionRepository roleHasPermissionRepository;


    @Override
    public void run(String... args) {
        Map<String, List<String>> actionByResource = new LinkedHashMap<>();
        actionByResource.put("admin", List.of("view", "create", "read", "update", "delete"));
        actionByResource.put("role", List.of("view", "create", "read", "update", "delete"));

        actionByResource.put("location", List.of("view", "create" , "read","update", "delete", "export"));
        
        actionByResource.put("order", List.of("view", "create" , "read","update", "delete", "export"));
        actionByResource.put("order_route", List.of("view", "create" , "read","update", "delete", "export"));

        actionByResource.put("product", List.of("view", "create" , "read","update", "delete", "export"));
        actionByResource.put("product_category", List.of("view", "create" , "read","update", "delete", "export"));


        List<Permission> allPermissions = new ArrayList<>();

        for (String resource : actionByResource.keySet()) {
            List<String> actions = actionByResource.get(resource);
            for (String action : actions) {
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
            }
        }

        allPermissions.forEach(permission -> System.out.println(permission.getName()));

        // check role exist
        Role adminRole = roleRepository.findByName("SUPERADMIN")
                .orElseGet(() -> {
                    Role role = new Role();
                    role.setName("SUPERADMIN");
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


    }
}
