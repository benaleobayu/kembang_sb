package com.bca.byc.controller;

import com.bca.byc.model.LoginRequestDTO;
import com.bca.byc.response.ApiDataResponse;
import com.bca.byc.response.DataAccess;
import com.bca.byc.security.util.JWTTokenFactory;
import com.bca.byc.service.AdminService;
import com.bca.byc.service.impl.AppAdminServiceImpl;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;

@Slf4j
@RestController
@RequestMapping("/cms/auth")
@Tag(name = "CMS Authentication API")
@AllArgsConstructor
public class CmsAuthController {

    private final AppAdminServiceImpl appAdminService;
    private final AdminService adminService;
    private final JWTTokenFactory jwtUtil;

    @PostMapping("/login")
    public ResponseEntity<ApiDataResponse> authLogin(@RequestBody LoginRequestDTO dto) {

        final UserDetails userDetails = appAdminService.loadUserByUsername(dto.email());
        final String tokens = jwtUtil.createAccessJWTToken(userDetails.getUsername(), new ArrayList<GrantedAuthority>(userDetails.getAuthorities())).getToken();
        final DataAccess dataAccess = new DataAccess(tokens, "Bearer", jwtUtil.getExpirationTime());
        return ResponseEntity.ok().body(new ApiDataResponse(true, "success", dataAccess));
    }

}
