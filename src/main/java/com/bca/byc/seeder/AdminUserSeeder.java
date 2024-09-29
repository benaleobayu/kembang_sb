package com.bca.byc.seeder;

import com.bca.byc.entity.AppAdmin;
import com.bca.byc.entity.Role;
import com.bca.byc.repository.RoleRepository;
import com.bca.byc.repository.auth.AppAdminRepository;
import lombok.AllArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
@Order(2)
@AllArgsConstructor
public class AdminUserSeeder implements CommandLineRunner {
//public class AdminUserSeeder  {

    private final AppAdminRepository appAdminRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) throws Exception {
        //create admin
        if (appAdminRepository.findByEmail("admin@unictive.net").isEmpty()) {
            AppAdmin admin = new AppAdmin();
            admin.setName("admin");
            admin.setEmail("admin@unictive.net");
            admin.setPassword(passwordEncoder.encode("password"));
            admin.setIsActive(true);
            admin.setCreatedAt(LocalDateTime.now());
            admin.setUpdatedAt(LocalDateTime.now());

            // set role
            Role adminRole;
            if (roleRepository.findByName("SUPERADMIN").isPresent()) {
                adminRole = roleRepository.findByName("SUPERADMIN")
                        .orElseThrow(() -> new RuntimeException("Role not found"));
            } else {
                adminRole = new Role();
                adminRole.setName("SUPERADMIN");
                roleRepository.save(adminRole);
            }
            admin.setRole(adminRole);

            appAdminRepository.save(admin);
        }

        if (appAdminRepository.findByEmail("admin-opt@unictive.net").isEmpty()) {
            AppAdmin adminOperator = new AppAdmin();
            adminOperator.setName("admin-operator");
            adminOperator.setEmail("admin-opt@unictive.net");
            adminOperator.setPassword(passwordEncoder.encode("password"));
            adminOperator.setIsActive(true);
            adminOperator.setCreatedAt(LocalDateTime.now());
            adminOperator.setUpdatedAt(LocalDateTime.now());

            Role adminOperatorRole;
            if (roleRepository.findByName("ADMIN-OPERATOR").isPresent()) {
                adminOperatorRole = roleRepository.findByName("ADMIN-OPERATOR")
                        .orElseThrow(() -> new RuntimeException("Role not found"));
            } else {
                adminOperatorRole = new Role();
                adminOperatorRole.setName("ADMIN-OPERATOR");
                roleRepository.save(adminOperatorRole);
            }
            adminOperator.setRole(adminOperatorRole);

            appAdminRepository.save(adminOperator);
        }

        if (appAdminRepository.findByEmail("admin-spv@unictive.net").isEmpty()) {
            AppAdmin adminSPV = new AppAdmin();
            adminSPV.setName("admin-spv");
            adminSPV.setEmail("admin-spv@unictive.net");
            adminSPV.setPassword(passwordEncoder.encode("password"));
            adminSPV.setIsActive(true);
            adminSPV.setCreatedAt(LocalDateTime.now());
            adminSPV.setUpdatedAt(LocalDateTime.now());

            Role adminSPVRole;
            if (roleRepository.findByName("ADMIN-SUPERVISOR").isPresent()) {
                adminSPVRole = roleRepository.findByName("ADMIN-SUPERVISOR")
                        .orElseThrow(() -> new RuntimeException("Role not found"));
            } else {
                adminSPVRole = new Role();
                adminSPVRole.setName("ADMIN-SUPERVISOR");
                roleRepository.save(adminSPVRole);
            }
            adminSPV.setRole(adminSPVRole);

            appAdminRepository.save(adminSPV);
        }

        if (appAdminRepository.findByEmail("admin-mgr@unictive.net").isEmpty()) {
            AppAdmin adminManager = new AppAdmin();
            adminManager.setName("admin-manager");
            adminManager.setEmail("admin-mgr@unictive.net");
            adminManager.setPassword(passwordEncoder.encode("password"));
            adminManager.setIsActive(true);
            adminManager.setCreatedAt(LocalDateTime.now());
            adminManager.setUpdatedAt(LocalDateTime.now());

            Role adminManagerRole;
            if (roleRepository.findByName("ADMIN-MANAGER").isPresent()) {
                adminManagerRole = roleRepository.findByName("ADMIN-MANAGER")
                        .orElseThrow(() -> new RuntimeException("Role not found"));
            } else {
                adminManagerRole = new Role();
                adminManagerRole.setName("ADMIN-MANAGER");
                roleRepository.save(adminManagerRole);
            }
            adminManager.setRole(adminManagerRole);

            appAdminRepository.save(adminManager);
        }


        // create user
//        if (appUserRepository.count() == 0) {
//            AppUser user = new AppUser();
//            user.setName("user");
//            user.setEmail("user@unictive.net");
//            user.setPassword(passwordEncoder.encode("password"));
//            user.setActive(true);
//            user.setCreatedAt(LocalDateTime.now());
//            user.setUpdatedAt(LocalDateTime.now());
//
//            // set role
//            Role userRole = roleRepository.findByName("USER")
//                    .orElseThrow(() -> new RuntimeException("Role not found"));
//            user.setRole(userRole);
//
//            appUserRepository.save(user);
//        }

    }
}
