package com.dev.byc.controller;

import com.dev.byc.entity.Admin;
import com.dev.byc.repository.AdminRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AdminRepository adminRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public AuthController(AdminRepository adminRepository, PasswordEncoder passwordEncoder) {
        this.adminRepository = adminRepository;
        this.passwordEncoder = passwordEncoder;
    }

    // @PostMapping("/login")
    // public ResponseEntity<String> login(@RequestParam String email, @RequestParam String password) {
    //     Admin admin = adminRepository.findByEmail(email);
    //     if (admin == null || !passwordEncoder.matches(password, admin.getPassword())) {
    //         return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid email/password combination");
    //     }
    //     // Here you might generate a JWT token and return it in the response
    //     return ResponseEntity.ok("Login successful");
    // }

}
