package com.bca.byc.controller;

import com.bca.byc.service.security.JwtTokenService;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth/")
public class SecretController {

    @Autowired
    private JwtTokenService jwtTokenService;

    @Operation(hidden = true)
    @GetMapping("/generate-token")
    public String generateToken(@RequestParam String email) {
        return jwtTokenService.generateToken(email);
    }

}
