package com.bca.byc.controller;

import com.bca.byc.entity.AppUser;
import com.bca.byc.enums.StatusType;
import com.bca.byc.exception.BadRequestException;
import com.bca.byc.model.*;
import com.bca.byc.repository.auth.AppUserRepository;
import com.bca.byc.response.ApiDataResponse;
import com.bca.byc.response.ApiResponse;
import com.bca.byc.response.DataAccess;
import com.bca.byc.security.util.JWTHeaderTokenExtractor;
import com.bca.byc.security.util.JWTTokenFactory;
import com.bca.byc.service.UserAuthService;
import com.bca.byc.service.impl.AppUserServiceImpl;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
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
    private final UserAuthService authService;
    private final JWTTokenFactory jwtUtil;
    private final JWTHeaderTokenExtractor jwtHeaderTokenExtractor;

    private final AppUserRepository userRepository;

    @PostMapping("/login")
    public ResponseEntity<ApiDataResponse> authLogin(@RequestBody LoginRequestDTO dto) {

        final UserDetails userDetails = appUserService.loadUserByUsername(dto.email());
        final String tokens = jwtUtil.createAccessJWTToken(userDetails.getUsername(), new ArrayList<GrantedAuthority>(userDetails.getAuthorities())).getToken();
        final DataAccess dataAccess = new DataAccess(tokens, "Bearer", jwtUtil.getExpirationTime());
        return ResponseEntity.ok().body(new ApiDataResponse(true, "success", dataAccess));
    }

    @PostMapping("/register")
    public ResponseEntity<ApiResponse> authRegister(@Valid @RequestBody AppRegisterRequest dto) {
        log.debug("Register request received: {}", dto.email());

        try {
            authService.saveUser(dto);
            log.info("User registered successfully: {}", dto.email());
            return ResponseEntity.ok(new ApiResponse(true, "User registered successfully. Please check your email to activate your account."));
        } catch (Exception e) {
            log.error("User registration failed: {}", dto.email(), e);
            return ResponseEntity.badRequest().body(new ApiResponse(false, e.getMessage()));
        }
    }

    @PostMapping("/validate-otp")
    public ResponseEntity<ApiDataResponse> validateOtp(@RequestBody OtpValidateRequest dto) {
        boolean isValid = authService.validateOtp(dto.email(), dto.otp());
        Optional<AppUser> user = userRepository.findByEmail(dto.email());
        if (isValid) {
            final UserDetails userDetails = appUserService.loadUserByUsername(dto.email());
            final String token = jwtUtil.createAccessJWTToken(userDetails.getUsername(), new ArrayList<GrantedAuthority>(userDetails.getAuthorities())).getToken();
            return ResponseEntity.ok(new ApiDataResponse<>(true, "OTP validated successfully.", token));
        } else if (user.isPresent() && user.get().getAppUserDetail().getStatus().equals(StatusType.REJECTED)) {
            return ResponseEntity.badRequest().body(new ApiDataResponse<>(false, "User not approved.", null));
        } else {
            return ResponseEntity.badRequest().body(new ApiDataResponse<>(false, "Invalid OTP.", null));
        }
    }

    @PostMapping("/send-otp")
    public ResponseEntity<ApiResponse> resendOtp(@RequestParam(value = "identity") String identity, @RequestBody OtpSendRequest otpResend) {
        try {
            authService.resendOtp(identity, otpResend.email());
            log.info("OTP sent successfully: {}", otpResend.email());
            return ResponseEntity.ok(new ApiResponse(true, "OTP sent successfully."));
        } catch (Exception e) {
            log.error("Failed to send OTP for email {}: {}", otpResend.email(), e.getMessage());
            return ResponseEntity.badRequest().body(new ApiResponse(false, "Failed to resend OTP: " + e.getMessage()));
        }
    }

    @SecurityRequirement(name = "Authorization")
    @PatchMapping("/setpassword")
    public ResponseEntity<ApiResponse> setPassword(
            @RequestHeader(value = "Authorization", required = false) String authorizationHeader,
            @RequestBody UserSetPasswordRequest dto) {
        log.info("PATCH /api/auth/setpassword endpoint hit");

        String tokenz = jwtHeaderTokenExtractor.extract(authorizationHeader);
        log.info("Extracted token: {}", tokenz);

        // Extract the email from the token
        String email = jwtHeaderTokenExtractor.getEmail(tokenz);
        log.info("Extracted email from token: {}", email);

        // Find the user by email
        AppUser user = appUserService.findByEmail(email);
        log.info("User found: {}", user != null);

        Map<String, Object> jsonMap = new HashMap<>();
        jsonMap.put("password", dto.getPassword());

        try {
            // If user not found
            if (user == null) {
                log.error("User not found");
                return ResponseEntity.badRequest().body(new ApiResponse(false, "User not found"));
            }

            // Set the new password
            authService.setNewPassword(email, dto);
            log.info("Password set successfully for user: {}", email);

            return ResponseEntity.ok(new ApiResponse(true, "Password set successfully"));

        } catch (BadRequestException e) {
            log.error("BadRequestException: {}", e.getMessage());
            return ResponseEntity.badRequest().body(new ApiResponse(false, e.getMessage()));
        }
    }


}
