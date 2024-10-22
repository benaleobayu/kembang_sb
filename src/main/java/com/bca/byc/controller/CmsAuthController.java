package com.bca.byc.controller;

import com.bca.byc.repository.LogDeviceRepository;
import com.bca.byc.response.ApiResponse;
import com.bca.byc.security.util.JWTHeaderTokenExtractor;
import com.bca.byc.service.util.ClientInfoService;
import com.bca.byc.entity.AppAdmin;
import com.bca.byc.entity.LogDevice;
import com.bca.byc.enums.ActionType;
import com.bca.byc.model.LoginRequestDTO;
import com.bca.byc.response.ApiDataResponse;
import com.bca.byc.response.DataAccessResponse;
import com.bca.byc.security.util.JWTTokenFactory;
import com.bca.byc.service.AdminService;
import com.bca.byc.service.impl.AppAdminServiceImpl;
import com.bca.byc.service.util.TokenBlacklistService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;

@Slf4j
@RestController
@RequestMapping("/cms/auth")
@Tag(name = "Authentication API")
@AllArgsConstructor
public class CmsAuthController {

    private final AppAdminServiceImpl appAdminService;
    private final AdminService adminService;

    private final JWTTokenFactory jwtUtil;
    private final JWTHeaderTokenExtractor jwtHeaderTokenExtractor;
    private final PasswordEncoder passwordEncoder;

    private final LogDeviceRepository logDeviceRepository;
    private final ClientInfoService clientInfoService;
    private final TokenBlacklistService tokenBlacklist;

    @PostMapping("/login")
    public ResponseEntity<?> authLogin(@RequestBody LoginRequestDTO dto, HttpServletRequest request) {

        AppAdmin admin = adminService.findByEmail(dto.email());

        if (admin == null || !passwordEncoder.matches(dto.password(), admin.getPassword()) || !admin.getIsActive() || admin.getIsDeleted()) {
            return ResponseEntity.badRequest().body(new ApiResponse(false, "The email address and password you entered do not match. Please try again."));
        }

        final UserDetails userDetails = appAdminService.loadUserByUsername(dto.email());

        final String tokens = jwtUtil.createAccessJWTToken(userDetails.getUsername(), new ArrayList<GrantedAuthority>(userDetails.getAuthorities())).getToken();
        final DataAccessResponse dataAccess = new DataAccessResponse(tokens, "Bearer", jwtUtil.getExpirationTime());

        final String ipAddress = clientInfoService.getClientIp(request);
        final String deviceId = clientInfoService.getBrowser(request);

        LogDevice logDevice = new LogDevice();
        logDevice.setAdmin(admin);
        logDevice.setDeviceId(deviceId);
        logDevice.setVersion("1.0.0");
        logDevice.setIpAddress(ipAddress);
        logDevice.setActionType(ActionType.LOGIN);
        logDeviceRepository.save(logDevice);

        return ResponseEntity.ok().body(new ApiDataResponse(true, "success", dataAccess));
    }

    @SecurityRequirement(name = "Authorization")
    @PostMapping("/logout")
    public ResponseEntity<ApiResponse> logout(
            @RequestParam(name = "deviceId") String deviceId,
            @RequestParam(name = "version") String version,
            HttpServletRequest request) {
        String token = jwtHeaderTokenExtractor.extract(request.getHeader("Authorization"));
        tokenBlacklist.addToBlacklist(token);

        final String ipAddress = clientInfoService.getClientIp(request);

        LogDevice logDevice = new LogDevice();
        logDevice.setDeviceId(deviceId);
        logDevice.setVersion(version);
        logDevice.setIpAddress(ipAddress);
        logDevice.setActionType(ActionType.LOGOUT);
        logDeviceRepository.save(logDevice);

        return ResponseEntity.ok().body(new ApiResponse(true, "User logged out successfully"));
    }


}
