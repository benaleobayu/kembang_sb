package com.bca.byc.controller.api;

import com.bca.byc.entity.StatusType;
import com.bca.byc.entity.User;
import com.bca.byc.exception.BadRequestException;
import com.bca.byc.model.UserSetPasswordRequest;
import com.bca.byc.model.auth.*;
import com.bca.byc.repository.UserRepository;
import com.bca.byc.response.ApiListResponse;
import com.bca.byc.response.ApiResponse;
import com.bca.byc.response.UserApiResponse;
import com.bca.byc.response.dataAccess;
import com.bca.byc.security.UserPrincipal;
import com.bca.byc.service.AuthService;
import com.bca.byc.service.UserService;
import com.bca.byc.service.auth.CustomAdminDetailsService;
import com.bca.byc.service.auth.UserDetailsServiceImpl;
import com.bca.byc.util.JwtUtil;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@Slf4j
@RestController
@RequestMapping("/api/auth")
@Tag(name = "Auth", description = "Authentication API")
@AllArgsConstructor
public class AuthResource {

    private AuthenticationManager authenticationManager;

    private AuthService authService;
    private UserService userService;

    private UserDetailsServiceImpl userDetailsService;
    private CustomAdminDetailsService adminDetailsService;

    private UserRepository repository;

    private JwtUtil jwtUtil;

    @PostMapping("/register")
    public ResponseEntity<ApiResponse> registerUser(
            @RequestBody RegisterRequest dto) {

        if (dto == null) {
            log.error("Request body is null or unsupported content type.");
            return ResponseEntity.badRequest().body(new ApiResponse(false, "Request body is missing or malformed."));
        }

        log.debug("Register request received: {}", dto.getEmail());

        try {
            authService.saveUser(dto);
            log.info("User registered successfully: {}", dto.getEmail());
            return ResponseEntity.ok(new ApiResponse(true, "User registered successfully. Please check your email to activate your account."));
        } catch (Exception e) {
            log.error("User registration failed for email {}: {}", dto.getEmail(), e.getMessage());
            return ResponseEntity.badRequest().body(new ApiResponse(false, "User registration failed: " + e.getMessage()));
        }
    }

    @PostMapping("/auth-register")
    public ResponseEntity<ApiResponse> registerUser(@RequestBody AuthRegisterRequest dto) {
        log.debug("Register request received: {}", dto.getEmail());

        if (repository.existsByEmail(dto.getEmail())) {
            log.error("User registration failed: Email {} already exists", dto.getEmail());
            return ResponseEntity.badRequest().body(new ApiResponse(false, "Email already exists"));
        }
        try {
            authService.saveUserWithRelations(dto);
            log.info("User registered successfully: {}", dto.getEmail());
            return ResponseEntity.ok(new ApiResponse(true, "User registered successfully. You are in the waiting status for approval. please check the email."));
        } catch (Exception e) {
            log.error("User registration failed for email {}: {}", dto.getEmail(), e.getMessage());
            return ResponseEntity.badRequest().body(new ApiResponse(false, "User registration failed: " + e.getMessage()));
        }
    }


    @PostMapping("/validate-otp")
    public ResponseEntity<ApiListResponse> validateOtp(@RequestBody OtpModelDTO.OtpRequest otpRequest) {
        boolean isValid = authService.validateOtp(otpRequest.getEmail(), otpRequest.getOtp());
        User user = userService.findByEmail(otpRequest.getEmail());
        if (isValid) {
            String token = jwtUtil.generateTokenByEmail(otpRequest.getEmail());
            return ResponseEntity.ok(new ApiListResponse(true, "OTP validated successfully.", token));
        } else if (!user.getStatus().equals(StatusType.APPROVED)) {
            return ResponseEntity.badRequest().body(new ApiListResponse(false, "User not approved.", null));
        } else {
            return ResponseEntity.badRequest().body(new ApiListResponse(false, "Invalid OTP.", null));
        }
    }

    @PostMapping("/send-otp")
    public ResponseEntity<ApiResponse> resendOtp(@RequestParam(value = "identity") String identity, @RequestBody OtpModelDTO.OtpResend otpResend) {
        try {
            authService.resendOtp(identity, otpResend.getEmail());
            log.info("OTP sent successfully: {}", otpResend.getEmail());
            return ResponseEntity.ok(new ApiResponse(true, "OTP sent successfully."));
        } catch (Exception e) {
            log.error("Failed to send OTP for email {}: {}", otpResend.getEmail(), e.getMessage());
            return ResponseEntity.badRequest().body(new ApiResponse(false, "Failed to resend OTP: " + e.getMessage()));
        }
    }

    @PostMapping("/login")
    public ResponseEntity<UserApiResponse> createAuthenticationToken(
            @RequestBody LoginRequest loginRequest) {

        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getPassword())
            );
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new UserApiResponse(false, "Incorrect email or password", null));
        }

        final UserDetails userDetails = userDetailsService.loadUserByUsername(loginRequest.getEmail());
        final String jwt = jwtUtil.generateToken(userDetails);
        final long expirationTime = jwtUtil.getExpirationTime();

        dataAccess data = new dataAccess(jwt, "Bearer", expirationTime);

        return ResponseEntity.ok(new UserApiResponse(true, "Authentication successful", data));
    }

    @SecurityRequirement(name = "Authorization")
    @PatchMapping("/setpassword")
    public ResponseEntity<ApiResponse> setPassword(
            @RequestHeader(value = "Authorization", required = false) String authorizationHeader,
            @RequestBody UserSetPasswordRequest dto) {
        log.info("PATCH /api/v1/users/setpassword endpoint hit");

        if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
            log.error("Authorization header is missing or not in the correct format");
            return ResponseEntity.badRequest().body(new ApiResponse(false, "Authorization header missing or invalid"));
        }

        // Extract the token from the Authorization header
        String token = authorizationHeader.replace("Bearer ", "");
        log.info("Extracted token: {}", token);

        // Extract the email from the token
        String email = jwtUtil.extractEmailFromToken(token);
        log.info("Extracted email from token: {}", email);

        // Find the user by email
        User user = userService.findByEmail(email);
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
            userService.setNewPassword(email, dto);
            log.info("Password set successfully for user: {}", email);

            return ResponseEntity.ok(new ApiResponse(true, "Password set successfully"));

        } catch (BadRequestException e) {
            log.error("BadRequestException: {}", e.getMessage());
            return ResponseEntity.badRequest().body(new ApiResponse(false, e.getMessage()));
        }
    }

//    @SecurityRequirement(name = "Authorization")
//    @PatchMapping("/setpassword")
//    public ResponseEntity<ApiListResponse> setPassword(
//            @RequestBody UserSetPasswordRequest dto) {
//        log.info("PATCH /api/v1/users/setpassword endpoint hit");
//
//        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
//        String email;
//
//        if (principal instanceof UserPrincipal) {
//            email = ((UserPrincipal) principal).getUsername();
//        } else if (principal instanceof String) {
//            email = (String) principal;
//        } else {
//            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
//                    .body(new ApiListResponse(false, "Unauthorized: Principal is not of expected type.", null));
//        }

//        try {
//            // Set the new password
//            userService.setNewPassword(email, dto);
//            log.info("Password set successfully for user: {}", email);
//
//            Map<String, String> data = new HashMap<>();
//            // get status of user
//            data.put("status", (String) userService.findInfoByEmail(email).getStatus());
//
//            return ResponseEntity.ok(new ApiListResponse(true, "Password set successfully", data));
//
//        } catch (BadRequestException e) {
//            log.error("BadRequestException: {}", e.getMessage());
//            return ResponseEntity.badRequest().body(new ApiListResponse(false, e.getMessage(), null));
//        }
//    }


    // admin
    @PostMapping("/cms-login")
    public ResponseEntity<UserApiResponse> authCMS(@RequestBody AuthenticationRequest authenticationRequest) {
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(authenticationRequest.getEmail(), authenticationRequest.getPassword())
            );
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new UserApiResponse(false, "Incorrect email or password", null));
        }

        final UserDetails userDetails = adminDetailsService.loadUserByUsername(authenticationRequest.getEmail());
        final String jwt = jwtUtil.generateTokenAdmin(userDetails);
        final long expirationTime = jwtUtil.getExpirationTime(); // Implement this method in JwtUtil to get the expiration time of the token.

        dataAccess data = new dataAccess(jwt, "Bearer", expirationTime);

        return ResponseEntity.ok(new UserApiResponse(true, "Authentication successful", data));
    }

}
