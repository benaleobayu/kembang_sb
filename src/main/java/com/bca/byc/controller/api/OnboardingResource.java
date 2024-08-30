package com.bca.byc.controller.api;

import com.bca.byc.model.OnboardingModelDTO;
import com.bca.byc.model.UserCmsDetailResponse;
import com.bca.byc.response.ApiResponse;
import com.bca.byc.response.ResultPageResponse;
import com.bca.byc.security.UserPrincipal;
import com.bca.byc.service.OnboardingService;
import com.bca.byc.service.UserService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/api/v1/onboarding")
@Tag(name = "Onboarding")
@AllArgsConstructor
@SecurityRequirement(name = "Authorization")
public class OnboardingResource {

    private final OnboardingService service;
    private final UserService userService;

    @PostMapping
    public ResponseEntity<ApiResponse> createOnboarding(@RequestBody OnboardingModelDTO.OnboardingCreateRequest dto) {

        // Get email from security context
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String email;

        if (principal instanceof UserPrincipal) {
            email = ((UserPrincipal) principal).getUsername();  // Adjust based on your UserPrincipal class
        } else if (principal instanceof String) {
            email = (String) principal;
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ApiResponse(false, "Unauthorized: Principal is not of expected type."));
        }

        log.debug("Hit /api/v1/onboarding endpoint with email: {}", email);
        try {
            service.createData(email, dto);
            return ResponseEntity.ok(new ApiResponse(true, "Onboarding created successfully."));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new ApiResponse(false, "Onboarding creation failed: " + e.getMessage()));
        }
    }

    @GetMapping("/onboarding-user")
    public ResponseEntity<ResultPageResponse<UserCmsDetailResponse>> listFollowUser(@RequestParam(name = "pages", required = true, defaultValue = "0") Integer pages, @RequestParam(name = "limit", required = true, defaultValue = "10") Integer limit, @RequestParam(name = "sortBy", required = true, defaultValue = "name") String sortBy, @RequestParam(name = "direction", required = true, defaultValue = "asc") String direction, @RequestParam(name = "userName", required = false) String userName) {
        log.info("GET /api/v1/users/onboarding-user endpoint hit");
        // response true
        return ResponseEntity.ok().body(userService.listFollowUser(pages, limit, sortBy, direction, userName));
    }


}