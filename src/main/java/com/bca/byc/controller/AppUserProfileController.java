package com.bca.byc.controller;

import com.bca.byc.entity.Notification;
import com.bca.byc.model.*;
import com.bca.byc.model.apps.ProfileActivityPostCommentsResponse;
import com.bca.byc.model.apps.ProfilePostResponse;
import com.bca.byc.response.*;
import com.bca.byc.security.util.ContextPrincipal;
import com.bca.byc.service.AppUserProfileService;
import com.bca.byc.service.AppUserService;
import com.bca.byc.service.NotificationService;
import io.swagger.v3.oas.annotations.Operation;
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
            return ResponseEntity.ok(new ApiDataResponse<>(true, "Profile updated successfully", userService.getUserDetails(principal.getName())));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new ApiResponse(false, e.getMessage()));
        }
    }

    @Operation(summary = "Get list My Post", description = "Get list My Post")
    @GetMapping("/my-posts")
    public ResponseEntity<PaginationAppsResponse<ResultPageResponseDTO<PostDetailResponse>>> listDataMyPost(
            @RequestParam(name = "pages", required = false, defaultValue = "0") Integer pages,
            @RequestParam(name = "limit", required = false, defaultValue = "12") Integer limit,
            @RequestParam(name = "sortBy", required = false, defaultValue = "id") String sortBy,
            @RequestParam(name = "direction", required = false, defaultValue = "desc") String direction,
            @RequestParam(name = "keyword", required = false) String keyword) {
        // response true
        log.info("GET " + urlRoute + " endpoint hit");
        return ResponseEntity.ok().body(new PaginationAppsResponse<>(true, "Success get list My Post", userService.listDataMyPost(pages, limit, sortBy, direction, keyword)));
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


    @Operation(summary = "Update user notification settings", description = "Update notification preferences for the user")
    @PutMapping("/settings/notification")
    public ResponseEntity<?> updateNotificationSettings(@RequestBody NotificationSettingsRequest dto, Principal principal) {
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
    public ResponseEntity<?> getNotificationSettings(Principal principal) {
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
    public ResponseEntity<?> createRequestContact(@RequestBody AppUserRequestContactRequest requestContact, Principal principal) {
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
    public ResponseEntity<Page<NotificationResponse>> getNotifications(Pageable pageable) {
        // Assuming `ContextPrincipal.getId()` retrieves the current user's ID
        Long userId = ContextPrincipal.getId();

        // Fetch paginated notifications for the user
        Page<Notification> notifications = notificationService.getNotificationsByUserId(userId, pageable);

        // Map each notification to a response object
        Page<NotificationResponse> responseMessages = notifications.map(notification -> new NotificationResponse(
                notification.getSecureId(),                       // Secure ID or primary key
                notification.getType(),                     // Type of notification
                notification.getNotifiableType(),           // Polymorphic type of the notification
                notification.getNotifiableId(),             // Polymorphic ID of the related entity
                notification.getData(),                     // JSON data or any message
                notification.getReadAt(),                   // When the notification was read
                notification.getCreatedAt(),                // Creation timestamp
                notification.getUpdatedAt()                 // Last updated timestamp
        ));

        // Return the paginated response
        return ResponseEntity.ok(responseMessages);
    }

    @Operation(summary = "Get user activity counts", description = "Get user activity counts")
    @GetMapping("/activity-counts")
    public ResponseEntity<?> getActivityCounts() {
        try {
            // Fetch activity counts
            ProfileActivityCounts data = userService.getActivityCounts();
            // Return success response
            return ResponseEntity.ok(new ApiDataResponse<>(true, "Activity counts retrieved successfully", data));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new ApiResponse(false, e.getMessage()));
        }
    }

    @Operation(summary = "Get list Post Saved Activity", description = "Get list Post Saved Activity")
    @GetMapping("/post-saved-activity")
    public ResponseEntity<PaginationAppsResponse<ResultPageResponseDTO<ProfileActivityPostResponse>>> listDataPostSavedActivity(
            @RequestParam(name = "pages", required = false, defaultValue = "0") Integer pages,
            @RequestParam(name = "limit", required = false, defaultValue = "10") Integer limit,
            @RequestParam(name = "sortBy", required = false, defaultValue = "id") String sortBy,
            @RequestParam(name = "direction", required = false, defaultValue = "desc") String direction,
            @RequestParam(name = "keyword", required = false) String keyword) {
        // response true
        log.info("GET " + urlRoute + "/post-saved-activity endpoint hit");
        return ResponseEntity.ok().body(new PaginationAppsResponse<>(true, "Success get list PostActivity", profileService.listDataProfileSavedActivity(pages, limit, sortBy, direction, keyword)));
    }

    @Operation(summary = "Get list Post Likes Activity", description = "Get list Post Likes Activity")
    @GetMapping("/post-likes-activity")
    public ResponseEntity<PaginationAppsResponse<ResultPageResponseDTO<ProfileActivityPostResponse>>> listDataPostLikesActivity(
            @RequestParam(name = "pages", required = false, defaultValue = "0") Integer pages,
            @RequestParam(name = "limit", required = false, defaultValue = "10") Integer limit,
            @RequestParam(name = "sortBy", required = false, defaultValue = "id") String sortBy,
            @RequestParam(name = "direction", required = false, defaultValue = "desc") String direction,
            @RequestParam(name = "keyword", required = false) String keyword) {
        // response true
        log.info("GET " + urlRoute + "/post-likes-activity endpoint hit");
        return ResponseEntity.ok().body(new PaginationAppsResponse<>(true, "Success get list PostActivity", profileService.listDataProfileLikesActivity(pages, limit, sortBy, direction, keyword)));
    }

    @Operation(summary = "Get list Post Comments Activity", description = "Get list Post Comments Activity")
    @GetMapping("/post-comment-activity")
    public ResponseEntity<PaginationAppsResponse<ResultPageResponseDTO<ProfileActivityPostCommentsResponse>>> listDataPostCommentsActivity(
            @RequestParam(name = "pages", required = false, defaultValue = "0") Integer pages,
            @RequestParam(name = "limit", required = false, defaultValue = "10") Integer limit,
            @RequestParam(name = "sortBy", required = false, defaultValue = "id") String sortBy,
            @RequestParam(name = "direction", required = false, defaultValue = "desc") String direction,
            @RequestParam(name = "keyword", required = false) String keyword) {
        // response true
        log.info("GET " + urlRoute + "/post-likes-activity endpoint hit");
        return ResponseEntity.ok().body(new PaginationAppsResponse<>(true, "Success get list PostActivity", profileService.listDataPostCommentsActivity(pages, limit, sortBy, direction, keyword)));
    }




}
