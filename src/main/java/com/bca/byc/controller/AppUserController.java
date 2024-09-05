package com.bca.byc.controller;

import com.bca.byc.response.ApiResponse;
import com.bca.byc.security.util.JWTTokenFactory;
import com.bca.byc.service.AppUserService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@Slf4j
@RestController
@RequestMapping("/api/v1/app-user")
@AllArgsConstructor
@Tag(name = "Apps")
@SecurityRequirement(name = "Authorization")
public class AppUserController {

    private AppUserService appUserService;
    private JWTTokenFactory jwtUtil;

    // Method to invalidate token by blacklisting it
    @GetMapping("/logout")
    public ResponseEntity<String> logout(@RequestHeader("Authorization") String authHeader) {
        // Extract the token from the Authorization header
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return ResponseEntity.badRequest().body("Invalid Authorization header.");
        }

        String token = authHeader.substring(7); // Remove "Bearer " prefix

        // Invalidate the token (blacklist it)
        jwtUtil.invalidateToken(token);

        return ResponseEntity.ok("Token invalidated and logged out successfully.");
    }

    @PostMapping("/{userId}/follow")
    public ResponseEntity<ApiResponse> followUser(@PathVariable("userId") Long userId, Principal principal) {
        log.info("POST /api/v1/users/{}/follow endpoint hit", userId);
        appUserService.followUser(userId, principal.getName());
        return ResponseEntity.ok(new ApiResponse(true, "User followed successfully"));
    }

    @PostMapping("/{userId}/unfollow")
    public ResponseEntity<ApiResponse> unfollowUser(@PathVariable("userId") Long userId, Principal principal) {
        log.info("POST /api/v1/users/{}/unfollow endpoint hit", userId);
        appUserService.unfollowUser(userId, principal.getName());
        return ResponseEntity.ok(new ApiResponse(true, "User unfollowed successfully"));
    }

}
