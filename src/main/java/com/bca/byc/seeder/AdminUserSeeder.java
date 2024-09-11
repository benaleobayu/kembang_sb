package com.bca.byc.seeder;

import com.bca.byc.entity.AppAdmin;
import com.bca.byc.entity.AppUser;
import com.bca.byc.entity.Role;
import com.bca.byc.repository.RoleRepository;
import com.bca.byc.repository.auth.AppAdminRepository;
import com.bca.byc.repository.auth.AppUserRepository;
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
    private final AppUserRepository appUserRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) throws Exception {
//        //create admin
//        if (appAdminRepository.count() == 0) {
//            AppAdmin admin = new AppAdmin();
//            admin.setName("admin");
//            admin.setEmail("admin@unictive.net");
//            admin.setPassword(passwordEncoder.encode("password"));
//            admin.setActive(true);
//            admin.setCreatedAt(LocalDateTime.now());
//            admin.setUpdatedAt(LocalDateTime.now());
//
//            // set role
//            Role adminRole = roleRepository.findByName("SUPERADMIN")
//                    .orElseThrow(() -> new RuntimeException("Role not found"));
//            admin.setRole(adminRole);
//
//            appAdminRepository.save(admin);
//        }
//
//        // create user
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
