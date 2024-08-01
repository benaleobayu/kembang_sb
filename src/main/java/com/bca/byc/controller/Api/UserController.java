package com.bca.byc.controller.Api;

import com.bca.byc.model.AuthenticationRequest;
import com.bca.byc.model.OtpRequest;
import com.bca.byc.model.RegisterRequest;
import com.bca.byc.repository.UserRepository;
import com.bca.byc.response.ApiResponse;
import com.bca.byc.response.UserApiResponse;
import com.bca.byc.service.EmailService;
import com.bca.byc.service.UserService;
import com.bca.byc.service.UserDetailsServiceImpl;
import com.bca.byc.util.JwtUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/auth")
public class UserController {

    private static final Logger logger = LoggerFactory.getLogger(UserController.class);

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UserService userService;

    @Autowired
    private UserDetailsServiceImpl userDetailsService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private EmailService emailService;

    @PostMapping("/register")
    public ResponseEntity<ApiResponse> registerUser(@Valid @RequestBody RegisterRequest registerRequest) {
        logger.debug("Register request received: {}", registerRequest.getEmail());

        if (userRepository.existsByEmail(registerRequest.getEmail())) {
            logger.error("User registration failed: Email {} already exists", registerRequest.getEmail());
            return ResponseEntity.badRequest().body(new ApiResponse(false, "Email already exists"));
        }
        try {
            userService.saveUser(registerRequest);
            logger.info("User registered successfully: {}", registerRequest.getEmail());
            return ResponseEntity.ok(new ApiResponse(true, "User registered successfully. OTP sent to email."));
        } catch (Exception e) {
            logger.error("User registration failed for email {}: {}", registerRequest.getEmail(), e.getMessage());
            return ResponseEntity.badRequest().body(new ApiResponse(false, "User registration failed: " + e.getMessage()));
        }
    }

    @PostMapping("/validate-otp")
    public ResponseEntity<ApiResponse> validateOtp(@RequestBody OtpRequest otpRequest) {
        boolean isValid = userService.validateOtp(otpRequest.getEmail(), otpRequest.getOtp());
        if (isValid) {
            return ResponseEntity.ok(new ApiResponse(true, "OTP validated successfully."));
        } else {
            return ResponseEntity.badRequest().body(new ApiResponse(false, "Invalid OTP."));
        }
    }

    @PostMapping("/resend-otp")
    public ResponseEntity<ApiResponse> resendOtp(@RequestBody OtpRequest otpRequest) {
        try {
            userService.resendOtp(otpRequest.getEmail());
            logger.info("OTP resent successfully: {}", otpRequest.getEmail());
            return ResponseEntity.ok(new ApiResponse(true, "OTP resent successfully."));
        } catch (Exception e) {
            logger.error("Failed to resend OTP for email {}: {}", otpRequest.getEmail(), e.getMessage());
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
