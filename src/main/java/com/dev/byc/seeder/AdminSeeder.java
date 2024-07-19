package com.dev.byc.seeder;

import com.dev.byc.entity.Admin;
import com.dev.byc.entity.Role;
import com.dev.byc.repository.AdminRepository;
import com.dev.byc.repository.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
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
        if (adminRepository.count() == 0) {
            Admin admin = new Admin();
            admin.setName("Admin Unictive");
            admin.setEmail("admin@unictive.net");
            admin.setPassword(passwordEncoder.encode("password"));
            admin.setCreatedAt(new Date());
            admin.setUpdatedAt(new Date());

            // Assign role
            Role adminRole = roleRepository.findByName("ROLE_ADMIN").orElseThrow(() -> new RuntimeException("Role not found"));
            admin.setRole(adminRole);

            adminRepository.save(admin);
        }
    }
}
