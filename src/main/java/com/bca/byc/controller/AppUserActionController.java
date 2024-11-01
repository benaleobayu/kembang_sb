package com.bca.byc.controller;

import com.bca.byc.exception.BadRequestException;
import com.bca.byc.model.PostShareResponse;
import com.bca.byc.model.attribute.SetLikeDislikeRequest;
import com.bca.byc.model.attribute.TotalCountResponse;
import com.bca.byc.model.returns.ReturnIsSavedResponse;
import com.bca.byc.response.ApiDataResponse;
import com.bca.byc.response.ApiResponse;
import com.bca.byc.security.util.ContextPrincipal;
import com.bca.byc.service.UserActionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

import static com.bca.byc.controller.AppUserActionController.urlRoute;

@Slf4j
@RestController
@RequestMapping(urlRoute)
@AllArgsConstructor
@Tag(name = "Apps Action")
@SecurityRequirement(name = "Authorization")
public class AppUserActionController {

    static final String urlRoute = "/api/v1";

    private UserActionService userActionService;

    @Operation(summary = "Follow user", description = "Follow user")
    @PostMapping("/users/{userId}/follow")
    public ResponseEntity<ApiResponse> followUser(@PathVariable("userId") String userId, Principal principal) {
        log.info("POST " + urlRoute + "/users/{}/follow endpoint hit", userId);
        userActionService.followUser(userId, principal.getName());
        return ResponseEntity.ok(new ApiResponse(true, "User followed successfully"));
    }

    @Operation(summary = "Unfollow user", description = "Unfollow user")
    @PostMapping("/users/{userId}/unfollow")
    public ResponseEntity<ApiResponse> unfollowUser(@PathVariable("userId") String userId, Principal principal) {
        log.info("POST " + urlRoute + "/users/{}//unfollow endpoint hit", userId);
        userActionService.unfollowUser(userId, principal.getName());
        return ResponseEntity.ok(new ApiResponse(true, "User unfollowed successfully"));
    }

    @Operation(summary = "Like or dislike post | comment | comment-reply", description = "Like or dislike  post | comment | comment-reply")
    @PostMapping("/action/like-dislike")
    public ResponseEntity<?> likeDislikePost(@Valid @RequestBody SetLikeDislikeRequest dto) {
        log.info("POST " + urlRoute + "/action/like-dislike endpoint hit with data: {}", dto);
        String email = ContextPrincipal.getPrincipal();

        try {
            TotalCountResponse message = userActionService.likeDislike(email, dto);
            log.info("Successfully processed like/dislike action for user: {}", email);
            return ResponseEntity.ok(new ApiDataResponse<>(true, "Success", message != null ? message : new TotalCountResponse(0)));
        } catch (BadRequestException e) {
            log.error("Bad request error: {}", e.getMessage());
            return ResponseEntity.badRequest().body(new ApiDataResponse<>(false, e.getMessage(), null));
        } catch (Exception e) {
            log.error("An unexpected error occurred: {}", e.getMessage(), e); // log stack trace
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiDataResponse<>(false, "An unexpected error occurred", null));
        }
    }

    @Operation(summary = "Save or unsave post", description = "Save or unsave post")
    @PostMapping("/post/{postId}/saved")
    public ResponseEntity<ApiDataResponse> savingPost(@PathVariable("postId") String postId) {
        log.info("POST " + urlRoute + "/post/{}/saved endpoint hit", postId);
        ReturnIsSavedResponse message = userActionService.saveUnsavePost(postId);

        return ResponseEntity.ok(new ApiDataResponse<>(true, "Succes" , message));
    }

    // API to share post
    @Operation(summary = "Share post", description = "Share post")
    @PostMapping("/post/share")
    public ResponseEntity<ApiResponse> sharePost(@RequestBody PostShareResponse dto) {
        log.info("POST " + urlRoute + "/post/share endpoint hit");
        String email = ContextPrincipal.getPrincipal();
        String message = userActionService.sharePost(dto, email);

        return ResponseEntity.ok(new ApiResponse(true, message));
    }


}
