package com.bca.byc.controller;

import com.bca.byc.entity.AppUser;
import com.bca.byc.model.OnboardingCreateRequest;
import com.bca.byc.model.OnboardingListUserResponse;
import com.bca.byc.response.ApiResponse;
import com.bca.byc.response.PaginationResponse;
import com.bca.byc.response.ResultPageResponseDTO;
import com.bca.byc.service.AppUserService;
import com.bca.byc.service.OnboardingService;
import com.bca.byc.service.impl.AppUserServiceImpl;
import io.jsonwebtoken.ExpiredJwtException;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/api/v1/onboarding")
@Tag(name = "Apps Onboarding API")
@AllArgsConstructor
public class OnboardingController {

    private final AppUserServiceImpl appUserService;
    private final OnboardingService service;
    private final AppUserService userService;

    @PreAuthorize("hasRole('ROLE_USER')")
    @PostMapping
    public ResponseEntity<ApiResponse> createOnboarding(@RequestBody OnboardingCreateRequest dto) {

        // Get email from security context
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Object principal = authentication.getPrincipal();
        String email;
        if (principal instanceof AppUser) {
            email = ((AppUser) principal).getEmail();
        } else if (principal instanceof UserDetails) {
            email = ((UserDetails) principal).getUsername();
        } else {
            return ResponseEntity.badRequest().body(new ApiResponse(false, "Invalid authentication principal."));
        }

        log.debug("Hit /api/v1/onboarding endpoint with email: {}", email);
        try {
            service.createData(email, dto);
            return ResponseEntity.ok(new ApiResponse(true, "Onboarding created successfully."));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new ApiResponse(false, "Onboarding creation failed: " + e.getMessage()));
        }
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/onboarding-user")
    public ResponseEntity<PaginationResponse<ResultPageResponseDTO<OnboardingListUserResponse>>> listFollowUser(
            @RequestParam(name = "pages", required = false, defaultValue = "0") Integer pages,
            @RequestParam(name = "limit", required = false, defaultValue = "10") Integer limit,
            @RequestParam(name = "sortBy", required = false, defaultValue = "name") String sortBy,
            @RequestParam(name = "direction", required = false, defaultValue = "asc") String direction,
            @RequestParam(name = "userName", required = false) String userName) {
        log.info("GET /api/v1/users/onboarding-user endpoint hit");
        // response true
        try{
            return ResponseEntity.ok().body(new PaginationResponse<>(true, "Success get list onboarding user", service.listFollowUser(pages, limit, sortBy, direction, userName)));
        }catch (ExpiredJwtException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new PaginationResponse<>(false, "Unauthorized", null));
        }
    }


}