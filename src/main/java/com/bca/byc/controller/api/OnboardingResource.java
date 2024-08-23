package com.bca.byc.controller.api;

import com.bca.byc.model.OnboardingModelDTO;
import com.bca.byc.repository.UserRepository;
import com.bca.byc.response.ApiResponse;
import com.bca.byc.service.OnboardingService;
import com.bca.byc.util.JwtUtil;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.tomcat.util.http.parser.Authorization;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authorization.method.AuthorizationAdvisor;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/api/v1/onboarding")
@Tag(name = "Onboarding")
@AllArgsConstructor
@SecurityRequirement(name = "Authorization")
public class OnboardingResource {

    private OnboardingService service;
    private JwtUtil jwtUtil;

    @SecurityRequirement(name = "Authorization")
    @PostMapping
    public ResponseEntity<ApiResponse> createOnboarding(
            @RequestHeader(value = "Authorization", required = false) String authorizationHeader,
            @ModelAttribute OnboardingModelDTO.OnboardingCreateRequest dto) {

        String token = authorizationHeader.replace("Bearer ", "");

        if (!jwtUtil.validateToken(token)) {
            log.error("Invalid token");
            return ResponseEntity.badRequest().body(new ApiResponse(false, "Invalid token"));
        }

        String email = jwtUtil.extractEmailFromToken(token);

        log.debug("Hit /api/v1/onboarding endpoint");
        try {
            service.createData(email, dto);
            return ResponseEntity.ok(new ApiResponse(true, "Onboarding created successfully."));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new ApiResponse(false, "Onboarding failed created: " + e.getMessage()));
        }
    }
}
