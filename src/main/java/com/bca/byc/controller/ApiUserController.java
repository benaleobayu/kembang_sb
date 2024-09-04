package com.bca.byc.controller;

import com.bca.byc.model.ApiUserInfoDetailResponse;
import com.bca.byc.response.ApiResponse;
import com.bca.byc.service.AppUserService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

@Slf4j
@AllArgsConstructor
@RestController
@RequestMapping("/api/v1/users")
@Tag(name = "Apps User API", description = "User API")
public class ApiUserController {

    private final AppUserService userService;

    @GetMapping("/info")
    public ResponseEntity<ApiResponse> getUserDetail(Principal principal) {
        log.info("GET /api/v1/users/info endpoint hit");

        ApiUserInfoDetailResponse user = userService.getUserDetails(principal.getName());
        // response true
        return ResponseEntity.ok(new ApiResponse(true, "User found", user));
    }

}
