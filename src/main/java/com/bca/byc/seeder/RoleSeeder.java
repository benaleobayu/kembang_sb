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
//public class RoleSeeder  {

    private final RoleRepository roleRepository;
    private final PermissionRepository permissionRepository;
    private final RoleHasPermissionRepository roleHasPermissionRepository;


    @Override
    public void run(String... args) {
        Map<String, List<String>> actionByResource = new LinkedHashMap<>();
        actionByResource.put("admin", List.of("view", "create", "read", "update", "delete"));
        actionByResource.put("role", List.of("view", "create", "read", "update", "delete"));
        // user management
        actionByResource.put("pre-registration", List.of("view", "create", "read", "update", "delete", "export", "import", "approval"));
        actionByResource.put("user-active", List.of("view", "read", "update", "delete", "export","import", "approval"));
        actionByResource.put("user-suspended", List.of("view", "read", "update", "delete", "export","import", "approval"));
        actionByResource.put("user-deleted", List.of("view", "read", "update", "delete", "export","import", "approval"));

        // content management
        actionByResource.put("channel_management", List.of("view", "read", "create", "update", "delete", "export"));
        actionByResource.put("content_management", List.of("view", "read", "create", "update", "delete", "export"));
        // report
        actionByResource.put("report_content", List.of("view", "read", "update", "delete", "export"));
        actionByResource.put("report_comment", List.of("view", "read", "update", "delete", "export"));
        actionByResource.put("report_user", List.of("view", "read", "update", "delete", "export"));
        actionByResource.put("report_chat", List.of("view", "read", "update", "delete", "export"));
        actionByResource.put("report_request", List.of("view", "read", "update", "delete", "export"));

        // inbox
        actionByResource.put("broadcast", List.of("view", "create","read", "update", "delete", "export"));

        // master data
        actionByResource.put("faq", List.of("view", "create", "read", "update", "delete"));
        actionByResource.put("business_category", List.of("view", "create", "read", "update", "delete"));
        actionByResource.put("expect_category", List.of("view", "create", "read", "update", "delete"));
        actionByResource.put("location", List.of("view", "create", "read", "update", "delete", "import"));
        actionByResource.put("branch", List.of("view", "create", "read", "update", "delete", "import"));
        actionByResource.put("tag", List.of("view", "create", "read", "update", "delete"));
        actionByResource.put("reason_report", List.of("view", "create", "read", "update", "delete"));
        actionByResource.put("blacklist_keyword", List.of("view", "create", "read", "update", "delete", "export", "import"));
        actionByResource.put("document", List.of("view", "create", "read", "update", "delete"));
        actionByResource.put("setting", List.of("view", "create", "read", "update", "delete"));

        actionByResource.put("request_contact", List.of("view", "read", "update", "delete"));
        actionByResource.put("accounts", List.of("view", "create" , "read","update", "delete", "export"));


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
