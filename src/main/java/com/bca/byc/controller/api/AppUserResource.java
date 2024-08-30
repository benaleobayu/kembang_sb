package com.bca.byc.controller.api;

import com.bca.byc.response.ApiResponse;
import com.bca.byc.service.AppUserService;
import com.bca.byc.service.UserService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import javassist.tools.rmi.AppletServer;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

@Slf4j
@RestController
@RequestMapping("/api/v1/app-user")
@AllArgsConstructor
@Tag(name = "Apps")
@SecurityRequirement(name = "Authorization")
public class AppUserResource {

    private AppUserService appUserService;

    @PostMapping("/{userId}/follow")
    public ResponseEntity<ApiResponse> followUser(@PathVariable("userId") Long userId, Principal principal) {
        log.info("POST /api/v1/users/{}/follow endpoint hit", userId);
        appUserService.followUser(userId, principal.getName());
        return ResponseEntity.ok(new ApiResponse(true, "User followed successfully"));
    }

    @PostMapping("/{userId}/unfollow")
    public ResponseEntity<ApiResponse> unfollowUser(@PathVariable("userId") Long userId, Principal principal) {
        log.info("POST /api/v1/users/{}/unfollow endpoint hit", userId);
        appUserService.unfollowUser(userId, principal.getName());
        return ResponseEntity.ok(new ApiResponse(true, "User unfollowed successfully"));
    }

}
