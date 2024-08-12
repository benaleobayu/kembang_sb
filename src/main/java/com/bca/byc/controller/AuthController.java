package com.bca.byc.controller;

import com.bca.byc.repository.AdminRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
@RequestMapping("/api/auth")
public class AuthController {

    private final AdminRepository adminRepository;
    private final PasswordEncoder passwordEncoder;

    // @PostMapping("/login")
    // public ResponseEntity<String> login(@RequestParam String email, @RequestParam
    // String password) {
    // Admin admin = adminRepository.findByEmail(email);
    // if (admin == null || !passwordEncoder.matches(password, admin.getPassword()))
    // {
    // return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid
    // email/password combination");
    // }
    // // Here you might generate a JWT token and return it in the response
    // return ResponseEntity.ok("Login successful");
    // }

}
