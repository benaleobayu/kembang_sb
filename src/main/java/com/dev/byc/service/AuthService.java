package com.dev.byc.service;

import com.dev.byc.entity.Admin;
import com.dev.byc.repository.AdminRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    @Autowired
    private AdminRepository adminRepository;

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    public boolean authenticate(String email, String password) {
        Admin admin = adminRepository.findByEmail(email);
        if (admin != null && bCryptPasswordEncoder.matches(password, admin.getPassword())) {
            // Authentication successful
            return true;
        }
        // Authentication failed
        return false;
    }
}
