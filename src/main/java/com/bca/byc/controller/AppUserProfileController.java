package com.bca.byc.controller;

import com.bca.byc.model.AppUserProfileRequest;
import com.bca.byc.model.UserInfoResponse;
import com.bca.byc.response.ApiDataResponse;
import com.bca.byc.response.ApiResponse;
import com.bca.byc.security.util.ContextPrincipal;
import com.bca.byc.service.AppUserProfileService;
import com.bca.byc.service.AppUserService;

import com.bca.byc.response.ChangePasswordRequest;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.tomcat.util.http.fileupload.impl.IOFileUploadException;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.security.Principal;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping(AppUserProfileController.urlRoute)
@Tag(name = "Apps User API", description = "User API")
@SecurityRequirement(name = "Authorization")
public class AppUserProfileController {

    static final String urlRoute = "/api/v1/users";
    private final AppUserService userService;
    private final AppUserProfileService profileService;

    @Operation(summary = "Get user detail", description = "Get user detail")
    @GetMapping("/info")
    public ResponseEntity<?> getUserDetail(Principal principal) {
        log.info("GET " + urlRoute + "/info endpoint hit");

        UserInfoResponse user = userService.getUserDetails(principal.getName());
        // response true
        try {
            return ResponseEntity.ok(new ApiDataResponse<>(true, "User found", user));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new ApiResponse(false, e.getMessage()));
        }
    }

    @Operation(summary = "Update user avatar", description = "Update user avatar")
    @PostMapping(value = "/avatar", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> updateUserAvatar(
            @RequestPart("avatar") MultipartFile avatar,
            Principal principal
    ) {
        log.info("POST " + urlRoute + "/avatar endpoint hit");

        String email = ContextPrincipal.getPrincipal();

        try {
            profileService.updateUserAvatar(email, avatar);
            return ResponseEntity.ok(new ApiDataResponse<>(true, "Avatar updated successfully", userService.getUserDetails(principal.getName())));
        } catch (IOFileUploadException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ApiResponse(false, e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new ApiResponse(false, e.getMessage()));
        }
    }

    @Operation(summary = "Update user cover", description = "Update user cover")
    @PostMapping(value = "/cover", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> updateUserCover(
            @RequestPart("cover") MultipartFile cover,
            Principal principal
    ) {
        log.info("POST " + urlRoute + "/cover endpoint hit");

        String email = ContextPrincipal.getPrincipal();

        try {
            profileService.updateUserCover(email, cover);
            return ResponseEntity.ok(new ApiDataResponse<>(true, "Cover updated successfully", userService.getUserDetails(principal.getName())));
        } catch (IOFileUploadException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ApiResponse(false, e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new ApiResponse(false, e.getMessage()));
        }
    }

    @Operation(summary = "Update user profile", description = "Update user profile")
    @PutMapping("/profile")
    public ResponseEntity<?> updateUserData(@RequestBody AppUserProfileRequest dto, Principal principal) {

        String email = ContextPrincipal.getPrincipal();
        try {
            userService.updateUserData(email, dto);
            return ResponseEntity.ok(new ApiDataResponse<>(true, "Profile updated successfully",userService.getUserDetails(principal.getName() )));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new ApiResponse(false, e.getMessage()));
        }
    }

    @GetMapping("/{userId}/posts")
    public ResponseEntity<?> getUserPosts(@PathVariable String userId) {
        log.info("GET " + urlRoute + "/{userId}/posts endpoint hit");
        return ResponseEntity.ok(new ApiDataResponse<>(true, "Posts found", userService.getUserPosts(userId)));
    }

    @Operation(summary = "Change user password", description = "Change user password")
    @PutMapping("/profile/change-password")
    public ResponseEntity<?> changePassword(@RequestBody ChangePasswordRequest dto, Principal principal) {

        String userSecureId = ContextPrincipal.getSecureUserId();
        try {
            // Check if newPassword and confirmNewPassword match
            if (!dto.getNewPassword().equals(dto.getConfirmNewPassword())) {
                return ResponseEntity.badRequest().body(new ApiResponse(false, "New password and confirmation password do not match"));
            }
    
            // Call a service method to change the password
            userService.changePassword(userSecureId, dto.getCurrentPassword(), dto.getNewPassword());
    
            // Return success response
            return ResponseEntity.ok(new ApiDataResponse<>(true, "Password updated successfully", null));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ApiResponse(false, "Invalid current password"));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new ApiResponse(false, e.getMessage()));
        }
    }
}
