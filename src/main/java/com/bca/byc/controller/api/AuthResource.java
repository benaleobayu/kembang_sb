package com.bca.byc.controller.api;

import com.bca.byc.entity.StatusType;
import com.bca.byc.entity.User;
import com.bca.byc.exception.BadRequestException;
import com.bca.byc.model.UserSetPasswordRequest;
import com.bca.byc.model.auth.AuthRegisterRequest;
import com.bca.byc.model.auth.AuthenticationRequest;
import com.bca.byc.model.auth.OtpRequest;
import com.bca.byc.model.auth.RegisterRequest;
import com.bca.byc.repository.UserRepository;
import com.bca.byc.response.ApiListResponse;
import com.bca.byc.response.ApiResponse;
import com.bca.byc.response.UserApiResponse;
import com.bca.byc.service.AuthService;
import com.bca.byc.service.auth.CustomAdminDetailsService;
import com.bca.byc.service.UserService;
import com.bca.byc.service.email.EmailService;
import com.bca.byc.service.auth.UserDetailsServiceImpl;
import com.bca.byc.util.JwtUtil;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.NoSuchElementException;

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

    private EmailService emailService;

    @PostMapping("/register")
    @ExceptionHandler(NoSuchElementException.class)
    public ResponseEntity<ApiResponse> registerUser(@RequestBody RegisterRequest dto) {
        log.debug("Register request received: {}", dto.getEmail());
        try {
            authService.saveUser(dto);
            log.info("User registered successfully: {}", dto.getEmail());
            return ResponseEntity.ok(new ApiResponse(true, "User registered successfully. please check email to activate your account."));
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
    public ResponseEntity<ApiListResponse> validateOtp(@RequestBody OtpRequest otpRequest) {
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
    public ResponseEntity<ApiResponse> resendOtp(@RequestBody OtpRequest otpRequest) {
        try {
            authService.resendOtp(otpRequest.getEmail());
            log.info("OTP sent successfully: {}", otpRequest.getEmail());
            return ResponseEntity.ok(new ApiResponse(true, "OTP sent successfully."));
        } catch (Exception e) {
            log.error("Failed to send OTP for email {}: {}", otpRequest.getEmail(), e.getMessage());
            return ResponseEntity.badRequest().body(new ApiResponse(false, "Failed to resend OTP: " + e.getMessage()));
        }
    }

    @PostMapping("/login")
    public ResponseEntity<UserApiResponse> createAuthenticationToken(@RequestBody AuthenticationRequest authenticationRequest) {
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(authenticationRequest.getEmail(), authenticationRequest.getPassword())
            );
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new UserApiResponse(false, "Incorrect email or password", null, null, 0));
        }

        final UserDetails userDetails = userDetailsService.loadUserByUsername(authenticationRequest.getEmail());
        final String jwt = jwtUtil.generateToken(userDetails);
        final long expirationTime = jwtUtil.getExpirationTime(); // Implement this method in JwtUtil to get the expiration time of the token.

        return ResponseEntity.ok(new UserApiResponse(true, "Authentication successful", jwt, "Bearer", expirationTime));
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

        // Validate the token
        if (!jwtUtil.validateToken(token)) {
            log.error("Invalid token");
            return ResponseEntity.badRequest().body(new ApiResponse(false, "Invalid token"));
        }

        // Extract the email from the token
        String email = jwtUtil.extractEmailFromToken(token);
        log.info("Extracted email from token: {}", email);

        // Find the user by email
        User user = userService.findByEmail(email);
        log.info("User found: {}", user != null);

        try {
            // If user not found
            if (user == null) {
                log.error("User not found");
                return ResponseEntity.badRequest().body(new ApiResponse(false, "User not found"));
            }

            // If user status is not approved
            if (!user.getStatus().equals(StatusType.VERIFIED)) {
                log.error("User not approved");
                return ResponseEntity.badRequest().body(new ApiResponse(false, "User not approved"));
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


    // admin
    @PostMapping("/cms-login")
    public ResponseEntity<UserApiResponse> authCMS(@RequestBody AuthenticationRequest authenticationRequest) {
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(authenticationRequest.getEmail(), authenticationRequest.getPassword())
            );
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new UserApiResponse(false, "Incorrect email or password", null, null, 0));
        }

        final UserDetails userDetails = adminDetailsService.loadUserByUsername(authenticationRequest.getEmail());
        final String jwt = jwtUtil.generateToken(userDetails);
        final long expirationTime = jwtUtil.getExpirationTime(); // Implement this method in JwtUtil to get the expiration time of the token.

        return ResponseEntity.ok(new UserApiResponse(true, "Authentication successful", jwt, "Bearer", expirationTime));
    }

}
