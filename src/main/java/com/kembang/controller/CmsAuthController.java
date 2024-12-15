package com.kembang.controller;

import com.kembang.exception.BadRequestException;
import com.kembang.model.RevalidateTokenRequest;
import com.kembang.response.ApiResponse;
import com.kembang.security.util.JWTHeaderTokenExtractor;
import com.kembang.service.util.ClientInfoService;
import com.kembang.entity.AppAdmin;
import com.kembang.model.LoginRequestDTO;
import com.kembang.response.ApiDataResponse;
import com.kembang.response.DataAccessResponse;
import com.kembang.security.util.JWTTokenFactory;
import com.kembang.service.AdminService;
import com.kembang.service.impl.AppAdminServiceImpl;
import com.kembang.service.util.TokenBlacklistService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
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

    private final ClientInfoService clientInfoService;
    private final TokenBlacklistService tokenBlacklist;

    @Value("${app.restrictCode}")
    private String restrictCode;

    @PostMapping("/login")
    public ResponseEntity<?> authLogin(@RequestBody LoginRequestDTO dto, HttpServletRequest request) {

        AppAdmin admin = adminService.findByEmail(dto.email());

        if (admin == null || !passwordEncoder.matches(dto.password(), admin.getPassword()) || !admin.getIsActive() || admin.getIsDeleted()) {
            throw new BadRequestException("The email address and password you entered do not match. Please try again.");
        }

        final UserDetails userDetails = appAdminService.loadUserByUsername(dto.email());

        String restrictCode = admin.getRole().getName().equals("SUPERADMIN") ? this.restrictCode : null;

        final String tokens = jwtUtil.createAccessJWTToken(userDetails.getUsername(), new ArrayList<GrantedAuthority>(userDetails.getAuthorities())).getToken();
        final DataAccessResponse dataAccess = new DataAccessResponse(tokens, "Bearer", jwtUtil.getExpirationTime(), restrictCode);
        return ResponseEntity.ok().body(new ApiDataResponse(true, "success", dataAccess));
    }

    @SecurityRequirement(name = "Authorization")
    @GetMapping("/logout")
    public ResponseEntity<ApiResponse> logout(
            @RequestParam(name = "deviceId", required = false) String deviceId,
            @RequestParam(name = "version", required = false) String version,
            HttpServletRequest request) {
        String token = jwtHeaderTokenExtractor.extract(request.getHeader("Authorization"));
        tokenBlacklist.addToBlacklist(token);
        return ResponseEntity.ok().body(new ApiResponse(true, "User logged out successfully"));
    }

    @SecurityRequirement(name = "Authorization")
    @GetMapping("/revalidate-token")
    public ResponseEntity<ApiResponse> revalidateToken(
        @RequestParam("token") String dto
    ){
        adminService.revalidateToken(dto);

        return ResponseEntity.ok().body(new ApiResponse(true, "Token revalidated successfully"));
    }


}
