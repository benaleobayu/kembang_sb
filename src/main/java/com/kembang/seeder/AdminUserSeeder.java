package com.kembang.seeder;

import com.kembang.entity.AppAdmin;
import com.kembang.entity.Role;
import com.kembang.repository.RoleRepository;
import com.kembang.repository.auth.AppAdminRepository;
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

    private final AppAdminRepository appAdminRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) throws Exception {
        //create admin
        if (appAdminRepository.findByEmail("admin@apps.net").isEmpty()) {
            AppAdmin admin = new AppAdmin();
            admin.setName("admin");
            admin.setEmail("admin@apps.net");
            admin.setPassword(passwordEncoder.encode("janganpassword"));
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

        if (appAdminRepository.findByEmail("admin-product@apps.net").isEmpty()) {
            AppAdmin adminOperator = new AppAdmin();
            adminOperator.setName("admin-product");
            adminOperator.setEmail("admin-product@apps.net");
            adminOperator.setPassword(passwordEncoder.encode("janganpassword"));
            adminOperator.setIsActive(true);
            adminOperator.setCreatedAt(LocalDateTime.now());
            adminOperator.setUpdatedAt(LocalDateTime.now());

            Role adminOperatorRole;
            if (roleRepository.findByName("ADMIN-PRODUCT").isPresent()) {
                adminOperatorRole = roleRepository.findByName("ADMIN-PRODUCT")
                        .orElseThrow(() -> new RuntimeException("Role not found"));
            } else {
                adminOperatorRole = new Role();
                adminOperatorRole.setName("ADMIN-PRODUCT");
                roleRepository.save(adminOperatorRole);
            }
            adminOperator.setRole(adminOperatorRole);

            appAdminRepository.save(adminOperator);
        }



    }
}
