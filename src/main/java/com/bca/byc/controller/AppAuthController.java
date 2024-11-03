package com.bca.byc.controller;

import com.bca.byc.entity.AppUser;
import com.bca.byc.exception.BadRequestException;
import com.bca.byc.model.*;
import com.bca.byc.repository.AppUserRepository;
import com.bca.byc.response.ApiDataResponse;
import com.bca.byc.response.ApiResponse;
import com.bca.byc.security.util.JWTHeaderTokenExtractor;
import com.bca.byc.security.util.JWTTokenFactory;
import com.bca.byc.service.impl.AppUserServiceImpl;
import com.bca.byc.service.util.TokenBlacklistService;
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