package com.bca.byc.controller.api;

import com.bca.byc.entity.LogDevice;
import com.bca.byc.entity.StatusType;
import com.bca.byc.entity.User;
import com.bca.byc.enums.ActionType;
import com.bca.byc.exception.BadRequestException;
import com.bca.byc.model.UserSetPasswordRequest;
import com.bca.byc.model.auth.*;
import com.bca.byc.repository.LogDeviceRespository;
import com.bca.byc.repository.UserRepository;
import com.bca.byc.response.*;
import com.bca.byc.service.AuthService;
import com.bca.byc.service.UserService;
import com.bca.byc.service.auth.CustomAdminDetailsService;
import com.bca.byc.service.auth.UserDetailsServiceImpl;
import com.bca.byc.util.JwtUtil;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

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
    private LogDeviceRespository logDeviceRespository;

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
            log.error("Error registering user", e);
            return ResponseEntity.badRequest().body(new ApiResponse(false, "Error registering user: " + e.getMessage()));
        }
    }

    @PostMapping("/validate-otp")
    public ResponseEntity<ApiResponse> validateOtp(@RequestBody OtpModelDTO.OtpRequest otpRequest) {
        boolean isValid = authService.validateOtp(otpRequest.getEmail(), otpRequest.getOtp());
        User user = userService.findByEmail(otpRequest.getEmail());
        if (isValid) {
            String token = jwtUtil.generateTokenByEmail(otpRequest.getEmail());
            return ResponseEntity.ok(new ApiResponse(true, "OTP validated successfully.", token));
        } else if (!user.getStatus().equals(StatusType.APPROVED)) {
            return ResponseEntity.badRequest().body(new ApiResponse(false, "User not approved.", null));
        } else {
            return ResponseEntity.badRequest().body(new ApiResponse(false, "Invalid OTP.", null));
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
    public ResponseEntity<ApiResponse> createAuthenticationToken(
            @RequestParam(name = "deviceId") String deviceId,
            @RequestParam(name = "version") String version,
            @RequestBody LoginRequest loginRequest, HttpServletRequest request) {

        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getPassword())
            );
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new ApiResponse(false, "Incorrect email or password" + e.getMessage(), null));
        }

        final UserDetails userDetails = userDetailsService.loadUserByUsername(loginRequest.getEmail());
        final String jwt = jwtUtil.generateToken(userDetails);
        final long expirationTime = jwtUtil.getExpirationTime();

        // insert log
        User user = userService.findByEmail(loginRequest.getEmail());
        LogDevice logDevice = new LogDevice();
        logDevice.setUser(user);
        logDevice.setDeviceId(deviceId);
        logDevice.setVersion(version);
        logDevice.setIpAddress(getClientIp(request));
        logDevice.setActionType(ActionType.LOGIN);
        logDeviceRespository.save(logDevice);

        DataAccess data = new DataAccess(jwt, "Bearer", expirationTime , user.getStatus().toString());

        return ResponseEntity.ok(new ApiResponse(true, "Authentication successful", data));
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

        DataAccess data = new DataAccess(jwt, "Bearer", expirationTime);

        return ResponseEntity.ok(new UserApiResponse(true, "Authentication successful", data));
    }

    private String getClientIp(HttpServletRequest request) {
        String remoteAddr = "";

        if (request != null) {
            remoteAddr = request.getHeader("X-FORWARDED-FOR");
            if (remoteAddr == null || remoteAddr.isEmpty()) {
                remoteAddr = request.getRemoteAddr();
            }
        }

        return remoteAddr;
    }
}
