package com.bca.byc.controller;

import com.bca.byc.entity.AppUser;
import com.bca.byc.model.OnboardingCreateRequest;
import com.bca.byc.model.OnboardingListUserResponse;
import com.bca.byc.response.ApiResponse;
import com.bca.byc.response.PaginationAppsResponse;
import com.bca.byc.response.ResultPageResponseDTO;
import com.bca.byc.service.OnboardingService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import static com.bca.byc.controller.OnboardingController.urlRoute;

@Slf4j
@RestController
@RequestMapping(urlRoute)
@Tag(name = "Apps Onboarding API")
@AllArgsConstructor
@SecurityRequirement(name = "Authorization")
public class OnboardingController {

    static final String urlRoute = "/api/v1/onboarding";

    private final OnboardingService service;

    @PreAuthorize("isAuthenticated()")
    @PostMapping
    public ResponseEntity<ApiResponse> createOnboarding(@RequestBody OnboardingCreateRequest dto) {


        log.debug("Hit /api/v1/onboarding endpoint ");
        try {
            service.createData(dto);
            return ResponseEntity.ok(new ApiResponse(true, "Onboarding created successfully."));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new ApiResponse(false, "Onboarding creation failed: " + e.getMessage()));
        }
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/onboarding-user")
    public ResponseEntity<PaginationAppsResponse<ResultPageResponseDTO<OnboardingListUserResponse>>> listFollowUser(
            @RequestParam(name = "pages", required = false, defaultValue = "0") Integer pages,
            @RequestParam(name = "limit", required = false, defaultValue = "10") Integer limit,
            @RequestParam(name = "sortBy", required = false, defaultValue = "name") String sortBy,
            @RequestParam(name = "direction", required = false, defaultValue = "asc") String direction,
            @RequestParam(name = "userName", required = false) String userName) {
        log.info("GET /api/v1/users/onboarding-user endpoint hit");
        // response true
        return ResponseEntity.ok().body(new PaginationAppsResponse<>(true, "Success get list onboarding user", service.listFollowUser(pages, limit, sortBy, direction, userName)));
    }


}