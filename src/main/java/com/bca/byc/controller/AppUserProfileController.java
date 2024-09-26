package com.bca.byc.controller;

import com.bca.byc.model.AppUserProfileRequest;
import com.bca.byc.model.UserInfoResponse;
import com.bca.byc.response.ApiDataResponse;
import com.bca.byc.response.ApiResponse;
import com.bca.byc.security.util.ContextPrincipal;
import com.bca.byc.service.AppUserProfileService;
import com.bca.byc.service.AppUserService;
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

    @PutMapping("/profile")
    public ResponseEntity<?> updateUserData(@RequestBody AppUserProfileRequest dto, Principal principal) {
        log.info("PUT " + urlRoute + "/profile endpoint hit");

        String email = ContextPrincipal.getPrincipal();
        try {
            userService.updateUserData(email, dto);
            return ResponseEntity.ok(new ApiDataResponse<>(true, "Profile updated successfully",userService.getUserDetails(principal.getName() )));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new ApiResponse(false, e.getMessage()));
        }
    }



}
