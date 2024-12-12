package com.kembang.controller;

import com.kembang.model.UserInfoResponse;
import com.kembang.response.ApiDataResponse;
import com.kembang.response.ApiResponse;
import com.kembang.security.util.ContextPrincipal;
import com.kembang.service.AppUserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping(AppUserProfileController.urlRoute)
@Tag(name = "Apps User API", description = "User API")
@SecurityRequirement(name = "Authorization")
public class AppUserProfileController {

    static final String urlRoute = "/api/v1/users";
    private final AppUserService userService;

    @Operation(summary = "Get user detail", description = "Get user detail")
    @GetMapping("/info")
    public ResponseEntity<?> InfoDetailUser(@RequestParam(value = "userId", required = false) String userId) {
        log.info("GET " + urlRoute + "/info endpoint hit");
        try {
            if (userId == null || userId.isEmpty()) {
                userId = ContextPrincipal.getSecureUserId();
            }
            UserInfoResponse user = userService.getUserDetails(userId);
            return ResponseEntity.ok(new ApiDataResponse<>(true, "User found", user));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new ApiResponse(false, e.getMessage()));
        }
    }


}
