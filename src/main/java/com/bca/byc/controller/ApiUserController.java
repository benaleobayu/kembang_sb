package com.bca.byc.controller;

import com.bca.byc.model.UserInfoResponse;
import com.bca.byc.response.ApiResponse;
import com.bca.byc.service.AppUserService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

@Slf4j
@AllArgsConstructor
@RestController
@RequestMapping("/api/v1/users")
@Tag(name = "Apps User API", description = "User API")
@SecurityRequirement(name = "Authorization")
public class ApiUserController {

    private final AppUserService userService;

    @GetMapping("/info")
    public ResponseEntity<ApiResponse> getUserDetail(Principal principal) {
        log.info("GET /api/v1/users/info endpoint hit");

        UserInfoResponse user = userService.getUserDetails(principal.getName());
        // response true
        try {
            return ResponseEntity.ok(new ApiResponse(true, "User found", user));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new ApiResponse(false, e.getMessage()));
        }
    }

    @GetMapping("/detail")
    public ResponseEntity<ApiResponse> getUserDetail(@AuthenticationPrincipal UserDetails userDetails) {
        if (userDetails != null) {
            String email = userDetails.getUsername(); // Assuming email is used as the username
            UserInfoResponse data = userService.getUserDetails(email);
            return ResponseEntity.ok(new ApiResponse(true, "User found", data));
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(new ApiResponse(false, "Unauthorized access", null));
        }
    }

}
