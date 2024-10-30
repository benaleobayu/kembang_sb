package com.bca.byc.controller;

import com.bca.byc.entity.Notification;
import com.bca.byc.model.AppUserProfileRequest;
import com.bca.byc.model.PostHomeResponse;
import com.bca.byc.model.ProfileActivityCounts;
import com.bca.byc.model.UserInfoResponse;
import com.bca.byc.model.apps.ProfileActivityPostCommentsResponse;
import com.bca.byc.model.data.UserProfileActivityCounts;
import com.bca.byc.response.*;
import com.bca.byc.security.util.ContextPrincipal;
import com.bca.byc.service.AppUserProfileService;
import com.bca.byc.service.AppUserService;
import com.bca.byc.service.NotificationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.tomcat.util.http.fileupload.impl.IOFileUploadException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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
    private final NotificationService notificationService;

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

    @Operation(summary = "Update user avatar", description = "Update user avatar")
    @PostMapping(value = "/avatar", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> AvatarUpdate(
            @RequestPart("avatar") MultipartFile avatar
    ) {
        log.info("POST " + urlRoute + "/avatar endpoint hit");

        try {
            return ResponseEntity.ok(new ApiDataResponse<>(true, "Avatar updated successfully", profileService.updateUserAvatar(avatar)));
        } catch (IOFileUploadException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ApiResponse(false, e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new ApiResponse(false, e.getMessage()));
        }
    }

    @Operation(summary = "Update user cover", description = "Update user cover")
    @PostMapping(value = "/cover", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> CoverUpdate(
            @RequestPart("cover") MultipartFile cover
    ) {
        log.info("POST " + urlRoute + "/cover endpoint hit");

        try {
            return ResponseEntity.ok(new ApiDataResponse<>(true, "Cover updated successfully", profileService.updateUserCover(cover)));
        } catch (IOFileUploadException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ApiResponse(false, e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new ApiResponse(false, e.getMessage()));
        }
    }

    @Operation(summary = "Update user profile", description = "Update user profile")
    @PutMapping("/profile")
    public ResponseEntity<?> UpdateDataProfile(@RequestBody AppUserProfileRequest dto, Principal principal) {
        try {
            Long userId = userService.updateUserData(dto);
            return ResponseEntity.ok(new ApiDataResponse<>(true, "Profile updated successfully", userService.getUserDetailFromId(userId)));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new ApiResponse(false, e.getMessage()));
        }
    }

    @Operation(summary = "Get list My Post", description = "Get list My Post")
    @GetMapping("/my-posts")
    public ResponseEntity<PaginationAppsResponse<ResultPageResponseDTO<PostHomeResponse>>> PostUserOnProfile(
            @RequestParam(name = "pages", required = false, defaultValue = "0") Integer pages,
            @RequestParam(name = "limit", required = false, defaultValue = "12") Integer limit,
            @RequestParam(name = "sortBy", required = false, defaultValue = "id") String sortBy,
            @RequestParam(name = "direction", required = false, defaultValue = "desc") String direction,
            @RequestParam(name = "keyword", required = false) String keyword,
            @RequestParam(name = "userId", required = false) String userId
    ) {
        if (userId == null || userId.isEmpty()) {
            userId = ContextPrincipal.getSecureUserId();
        }
        // response true
        log.info("GET " + urlRoute + " endpoint hit");
        return ResponseEntity.ok().body(new PaginationAppsResponse<>(true, "Success get list My Post", userService.listDataMyPost(pages, limit, sortBy, direction, keyword, userId)));
    }

    @Operation(summary = "Get list Tag Post", description = "Get list Tag Post")
    @GetMapping("/my-tags")
    public ResponseEntity<PaginationAppsResponse<ResultPageResponseDTO<PostHomeResponse>>> TagPostUserOnProfile(
            @RequestParam(name = "pages", required = false, defaultValue = "0") Integer pages,
            @RequestParam(name = "limit", required = false, defaultValue = "12") Integer limit,
            @RequestParam(name = "sortBy", required = false, defaultValue = "id") String sortBy,
            @RequestParam(name = "direction", required = false, defaultValue = "desc") String direction,
            @RequestParam(name = "keyword", required = false) String keyword,
            @RequestParam(name = "userId", required = false) String userId
    ) {
        if (userId == null || userId.isEmpty()) {
            userId = ContextPrincipal.getSecureUserId();
        }
        // response true
        log.info("GET " + urlRoute + " endpoint hit");
        return ResponseEntity.ok().body(new PaginationAppsResponse<>(true, "Success get list Tagged Post", userService.listDataTagPost(pages, limit, sortBy, direction, keyword, userId)));
    }


    @Operation(summary = "Change user password", description = "Change user password")
    @PutMapping("/profile/change-password")
    public ResponseEntity<?> ChangePassword(@RequestBody ChangePasswordRequest dto, Principal principal) {

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


    @Operation(summary = "Update user notification settings", description = "Update notification preferences for the user")
    @PutMapping("/settings/notification")
    public ResponseEntity<?> UpdateNotificationSettings(@RequestBody NotificationSettingsRequest dto, Principal principal) {
        String userSecureId = ContextPrincipal.getSecureUserId();
        try {
            // Call service to save notification settings
            userService.saveNotificationSettings(userSecureId, dto);
            // Return success response
            return ResponseEntity.ok(new ApiDataResponse<>(true, "Notification settings updated successfully", dto));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new ApiResponse(false, e.getMessage()));
        }
    }

    @Operation(summary = "Get user notification settings", description = "Retrieve the notification preferences for the user")
    @GetMapping("/settings/notification")
    public ResponseEntity<?> GetNotificationSettings(Principal principal) {
        String userSecureId = ContextPrincipal.getSecureUserId();
        try {
            // Fetch notification settings for the user
            NotificationSettingsResponse notificationSettings = userService.getNotificationSettings(userSecureId);

            // Return success response
            return ResponseEntity.ok(new ApiDataResponse<>(true, "Notification settings retrieved successfully", notificationSettings));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new ApiResponse(false, e.getMessage()));
        }
    }

    @Operation(summary = "Create new request contact", description = "Creates a new user request contact and stores it in the database")
    @PostMapping("/request-contact/create")
    public ResponseEntity<?> FaqRequestContact(@RequestBody AppUserRequestContactRequest requestContact, Principal principal) {
        try {
            String userSecureId = ContextPrincipal.getSecureUserId();
            // Create new request contact
            AppUserRequestContactResponse createdContact = userService.createRequestContact(userSecureId, requestContact.getMessages());
            // Return success response
            return ResponseEntity.ok(new ApiDataResponse<>(true, "Request contact created successfully", createdContact));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new ApiResponse(false, e.getMessage()));
        }
    }

    @GetMapping("/my-notifications")
    @Operation(summary = "Notifications", description = "List Notifications")
    public ResponseEntity<PaginationAppsResponse<ResultPageResponseDTO<NotificationResponse>>> GetNotifiation(
            @RequestParam(name = "pages", required = false, defaultValue = "0") Integer pages,
            @RequestParam(name = "limit", required = false, defaultValue = "12") Integer limit,
            @RequestParam(name = "sortBy", required = false, defaultValue = "updatedAt") String sortBy,
            @RequestParam(name = "direction", required = false, defaultValue = "desc") String direction,
            @RequestParam(name = "keyword", required = false) String keyword
    ) {
        ResultPageResponseDTO<NotificationResponse> notifications = notificationService.getNotificationsByUserId(pages, limit, sortBy, direction, keyword);
        return ResponseEntity.ok().body(new PaginationAppsResponse<>(true, "Notifications retrieved successfully", notifications));
    }

    @Operation(summary = "Get user activity counts", description = "Get user activity counts")
    @GetMapping("/activity-counts")
    public ResponseEntity<?> MyActivityCounts(@RequestParam(name = "userId", required = false) String userId) {
        try {
            if (userId == null || userId.isEmpty()) {
                userId = ContextPrincipal.getSecureUserId();
            }
            ProfileActivityCounts data = userService.getActivityCounts(userId);
            return ResponseEntity.ok(new ApiDataResponse<>(true, "Activity counts retrieved successfully", data));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new ApiResponse(false, e.getMessage()));
        }
    }

    @GetMapping("/following-followers")
    public ResponseEntity<PaginationAppsResponse<ResultPageResponseDTO<PostHomeResponse>>> UserFollowAndFollowing(
            @RequestParam(name = "pages", required = false, defaultValue = "0") Integer pages,
            @RequestParam(name = "limit", required = false, defaultValue = "12") Integer limit,
            @RequestParam(name = "sortBy", required = false, defaultValue = "id") String sortBy,
            @RequestParam(name = "direction", required = false, defaultValue = "desc") String direction,
            @RequestParam(name = "keyword", required = false) String keyword,
            @RequestParam(name = "userId", required = false) String userId,
            @Schema(example = "FOLLOWING | FOLLOWERS")
            @RequestParam(name = "type", required = false) String type
            ) {
        // response true
        log.info("GET " + urlRoute + "/post-saved-activity endpoint hit");
        return ResponseEntity.ok().body(new PaginationAppsResponse<>(true, "Success get list PostActivity", profileService.listDataUserFollowAndFollowing(pages, limit, sortBy, direction, keyword, type, userId)));
    }


    @Operation(summary = "Get list Post Saved Activity", description = "Get list Post Saved Activity")
    @GetMapping("/post-saved-activity")
    public ResponseEntity<PaginationAppsResponse<ResultPageResponseDTO<PostHomeResponse>>> MySavedPostActivity(
            @RequestParam(name = "pages", required = false, defaultValue = "0") Integer pages,
            @RequestParam(name = "limit", required = false, defaultValue = "12") Integer limit,
            @RequestParam(name = "sortBy", required = false, defaultValue = "id") String sortBy,
            @RequestParam(name = "direction", required = false, defaultValue = "desc") String direction,
            @RequestParam(name = "keyword", required = false) String keyword) {
        // response true
        log.info("GET " + urlRoute + "/post-saved-activity endpoint hit");
        return ResponseEntity.ok().body(new PaginationAppsResponse<>(true, "Success get list PostActivity", profileService.listDataProfileSavedActivity(pages, limit, sortBy, direction, keyword)));
    }

    @Operation(summary = "Get list Post Likes Activity", description = "Get list Post Likes Activity")
    @GetMapping("/post-likes-activity")
    public ResponseEntity<PaginationAppsResponse<ResultPageResponseDTO<PostHomeResponse>>> MyLikePostActivity(
            @RequestParam(name = "pages", required = false, defaultValue = "0") Integer pages,
            @RequestParam(name = "limit", required = false, defaultValue = "12") Integer limit,
            @RequestParam(name = "sortBy", required = false, defaultValue = "id") String sortBy,
            @RequestParam(name = "direction", required = false, defaultValue = "desc") String direction,
            @RequestParam(name = "keyword", required = false) String keyword) {
        // response true
        log.info("GET " + urlRoute + "/post-likes-activity endpoint hit");
        return ResponseEntity.ok().body(new PaginationAppsResponse<>(true, "Success get list PostActivity", profileService.listDataProfileLikesActivity(pages, limit, sortBy, direction, keyword)));
    }

    @Operation(summary = "Get list Post Comments Activity", description = "Get list Post Comments Activity")
    @GetMapping("/post-comment-activity")
    public ResponseEntity<PaginationAppsResponse<ResultPageResponseDTO<ProfileActivityPostCommentsResponse>>> MyCommentsActivity(
            @RequestParam(name = "pages", required = false, defaultValue = "0") Integer pages,
            @RequestParam(name = "limit", required = false, defaultValue = "20") Integer limit,
            @RequestParam(name = "sortBy", required = false, defaultValue = "id") String sortBy,
            @RequestParam(name = "direction", required = false, defaultValue = "desc") String direction,
            @RequestParam(name = "keyword", required = false) String keyword) {
        // response true
        log.info("GET " + urlRoute + "/post-likes-activity endpoint hit");
        return ResponseEntity.ok().body(new PaginationAppsResponse<>(true, "Success get list PostActivity", profileService.listDataPostCommentsActivity(pages, limit, sortBy, direction, keyword)));
    }

    // Fetch to get profile activity
    @GetMapping("/profile-activity-counts")
    public ResponseEntity<?> ProfileActivityCounts(@RequestParam(name = "userId", required = false) String userId) {
        try {
            if (userId == null || userId.isEmpty()) {
                userId = ContextPrincipal.getSecureUserId();
            }
            // Fetch activity counts
            UserProfileActivityCounts data = userService.getProfileActivityCounts(userId);
            // Return success response
            return ResponseEntity.ok(new ApiDataResponse<>(true, "Profile activity counts retrieved successfully", data));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new ApiResponse(false, e.getMessage()));
        }
    }


}
