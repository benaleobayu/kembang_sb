package com.bca.byc.seeder;

import com.bca.byc.entity.Admin;
import com.bca.byc.entity.Role;
import com.bca.byc.repository.AdminRepository;
import com.bca.byc.repository.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
@Order(2)
public class AdminSeeder implements CommandLineRunner {

    @Autowired
    private AdminRepository adminRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) throws Exception {
        // Create admin
//        if (adminRepository.count() == 0) {
//            Admin admin = new Admin();
//            admin.setName("Admin Unictive");
//            admin.setEmail("admin@unictive.net");
//            admin.setPassword(passwordEncoder.encode("password"));
//            admin.setCreatedAt(LocalDateTime.now());
//            admin.setUpdatedAt(LocalDateTime.now());
//
//            // Assign role
//            Role adminRole = roleRepository.findByName("ROLE_ADMIN")
//                    .orElseThrow(() -> new RuntimeException("Role not found"));
//            admin.setRole(adminRole);
//
//            adminRepository.save(admin);
//        }
    }
}
