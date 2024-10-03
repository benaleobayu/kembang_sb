package com.bca.byc.controller;

import com.bca.byc.model.attribute.SetLikeDislikeRequest;
import com.bca.byc.response.ApiResponse;
import com.bca.byc.security.util.ContextPrincipal;
import com.bca.byc.service.UserActionService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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

    @PostMapping("/users/{userId}/follow")
    public ResponseEntity<ApiResponse> followUser(@PathVariable("userId") String userId, Principal principal) {
        log.info("POST " + urlRoute + "/users/{}/follow endpoint hit", userId);
        userActionService.followUser(userId, principal.getName());
        return ResponseEntity.ok(new ApiResponse(true, "User followed successfully"));
    }

    @PostMapping("/users/{userId}/unfollow")
    public ResponseEntity<ApiResponse> unfollowUser(@PathVariable("userId") String userId, Principal principal) {
        log.info("POST " + urlRoute + "/users/{}//unfollow endpoint hit", userId);
        userActionService.unfollowUser(userId, principal.getName());
        return ResponseEntity.ok(new ApiResponse(true, "User unfollowed successfully"));
    }

    @PostMapping("/post/{id}/like-dislike")
    public ResponseEntity<ApiResponse> likeDislikePost(@PathVariable("id") String postId, @RequestBody SetLikeDislikeRequest isLike) {
        log.info("POST " + urlRoute + "/post/{}/like-post endpoint hit", postId);
        String email = ContextPrincipal.getPrincipal();
        userActionService.likeDislikePost(postId, email, isLike);
        String success = isLike.getIsLike() ? "success like" : "success dislike";
        return ResponseEntity.ok(new ApiResponse(true, "Liked " + success));
    }

    @PostMapping("/comment/{id}/like-dislike")
    public ResponseEntity<ApiResponse> likeDislikeComment(@PathVariable("id") String commentId, @RequestBody SetLikeDislikeRequest isLike) {
        log.info("POST " + urlRoute + "/post/{}/like-post endpoint hit", commentId);
        String email = ContextPrincipal.getPrincipal();
        userActionService.likeDislikeComment(commentId, email, isLike);
        String success = isLike.getIsLike() ? "success like" : "success dislike";
        return ResponseEntity.ok(new ApiResponse(true, "Liked " + success));
    }

    @PostMapping("/comment-reply/{id}/like-dislike")
    public ResponseEntity<ApiResponse> likeDislikeCommentReply(@PathVariable("id") String commentId, @RequestBody SetLikeDislikeRequest isLike) {
        log.info("POST " + urlRoute + "/post/{}/like-post endpoint hit", commentId);
        String email = ContextPrincipal.getPrincipal();
        userActionService.likeDislikeCommentReply(commentId, email, isLike);
        String success = isLike.getIsLike() ? "success like" : "success dislike";
        return ResponseEntity.ok(new ApiResponse(true, "Liked " + success));
    }



}
