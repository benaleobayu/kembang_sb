package com.bca.byc.controller.api;

import com.bca.byc.entity.User;
import com.bca.byc.exception.BadRequestException;
import com.bca.byc.model.RegisterRequest;
import com.bca.byc.model.auth.AuthenticationRequest;
import com.bca.byc.model.auth.OtpRequest;
import com.bca.byc.repository.UserRepository;
import com.bca.byc.response.ApiResponse;
import com.bca.byc.response.UserApiResponse;
import com.bca.byc.service.AuthService;
import com.bca.byc.service.UserDetailsServiceImpl;
import com.bca.byc.service.email.EmailService;
import com.bca.byc.util.JwtUtil;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.mail.MessagingException;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/api/auth")
@Tag(name = "Auth API")
@AllArgsConstructor
public class AuthResource {

    private AuthenticationManager authenticationManager;

    private AuthService authService;

    private UserDetailsServiceImpl userDetailsService;

    private UserRepository repository;

    private JwtUtil jwtUtil;

    private EmailService emailService;

    @PostMapping("/register")
    public ResponseEntity<ApiResponse> registerUser(@RequestBody RegisterRequest dto) {
        log.debug("Register request received: {}", dto.getEmail());

        if (repository.existsByEmail(dto.getEmail())) {
            log.error("User registration failed: Email {} already exists", dto.getEmail());
            return ResponseEntity.badRequest().body(new ApiResponse(false, "Email already exists"));
        }
        try {
            authService.saveUser(dto);
            log.info("User registered successfully: {}", dto.getEmail());
            return ResponseEntity.ok(new ApiResponse(true, "User registered successfully. You are in the waiting status for approval. please check the email."));
        } catch (Exception e) {
            log.error("User registration failed for email {}: {}", dto.getEmail(), e.getMessage());
            return ResponseEntity.badRequest().body(new ApiResponse(false, "User registration failed: " + e.getMessage()));
        }
    }

    @PostMapping("/validate-otp")
    public ResponseEntity<ApiResponse> validateOtp(@RequestBody OtpRequest otpRequest) {
        boolean isValid = authService.validateOtp(otpRequest.getEmail(), otpRequest.getOtp());
        if (isValid) {
            return ResponseEntity.ok(new ApiResponse(true, "OTP validated successfully."));
        } else {
            return ResponseEntity.badRequest().body(new ApiResponse(false, "Invalid OTP."));
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

}
