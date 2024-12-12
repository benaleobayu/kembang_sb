package com.kembang.controller;

import com.kembang.entity.AppUser;
import com.kembang.exception.BadRequestException;
import com.kembang.model.*;
import com.kembang.repository.AppUserRepository;
import com.kembang.response.ApiDataResponse;
import com.kembang.response.ApiResponse;
import com.kembang.security.util.JWTHeaderTokenExtractor;
import com.kembang.security.util.JWTTokenFactory;
import com.kembang.service.impl.AppUserServiceImpl;
import com.kembang.service.util.TokenBlacklistService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@RestController
@AllArgsConstructor
@Slf4j
@RequestMapping("/api/auth")
@Tag(name = "Apps Authentication API", description = "Authentication API")
public class AppAuthController {

    private final AppUserServiceImpl appUserService;
    private final JWTTokenFactory jwtUtil;
    private final JWTHeaderTokenExtractor jwtHeaderTokenExtractor;

    private final AppUserRepository userRepository;

    private final TokenBlacklistService tokenBlacklist;

    @PostMapping("/login")
    public ResponseEntity<?> authLogin(
            @RequestParam(name = "deviceId") String deviceId,
            @RequestParam(name = "version") String version,
            @RequestBody LoginRequestDTO dto,
            HttpServletRequest request) {

        return ResponseEntity.ok().body(new ApiResponse(true, "User logged in successfully"));
    }

    // Method to invalidate token by blacklisting it
    @SecurityRequirement(name = "Authorization")
    @PostMapping("/logout")
    public ResponseEntity<ApiResponse> logout(
            @RequestParam(name = "deviceId") String deviceId,
            @RequestParam(name = "version") String version,
            HttpServletRequest request) {
        String token = jwtHeaderTokenExtractor.extract(request.getHeader("Authorization"));
        tokenBlacklist.addToBlacklist(token);

        return ResponseEntity.ok().body(new ApiResponse(true, "User logged out successfully"));
    }


}