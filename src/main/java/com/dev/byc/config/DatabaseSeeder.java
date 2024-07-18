package com.dev.byc.config;

import com.dev.byc.entity.Admin;
import com.dev.byc.repository.AdminRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class DatabaseSeeder implements CommandLineRunner {

    private final AdminRepository adminRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public DatabaseSeeder(AdminRepository adminRepository, PasswordEncoder passwordEncoder) {
        this.adminRepository = adminRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) throws Exception {
        seedAdmins();
    }

    private void seedAdmins() {
        if (adminRepository.count() == 0) {
            Admin admin1 = new Admin();
            admin1.setName("Administrator");
            admin1.setEmail("admin@unictive.net");
            admin1.setPassword(passwordEncoder.encode("password"));
            admin1.setCreatedAt(new Date());
            admin1.setUpdatedAt(new Date());
            adminRepository.save(admin1);
        }
    }
}
