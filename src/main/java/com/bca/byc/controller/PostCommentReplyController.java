package com.bca.byc.controller;


import com.bca.byc.exception.BadRequestException;
import com.bca.byc.model.apps.CommentCreateUpdateRequest;
import com.bca.byc.model.apps.CommentDetailResponse;
import com.bca.byc.model.apps.ListCommentResponse;
import com.bca.byc.response.ApiDataResponse;
import com.bca.byc.response.ApiResponse;
import com.bca.byc.response.PaginationAppsResponse;
import com.bca.byc.response.ResultPageResponseDTO;
import com.bca.byc.security.util.ContextPrincipal;
import com.bca.byc.service.CommentService;
import com.bca.byc.service.CommentServiceReply;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@Slf4j
@RestController
@AllArgsConstructor
@RequestMapping(PostCommentReplyController.urlRoute)
@Tag(name = "Comment API")
@SecurityRequirement(name = "Authorization")
@PreAuthorize("isAuthenticated()")
public class PostCommentReplyController {

    static final String urlRoute = "/api/v1/post";
    private CommentServiceReply service;

    @Operation(summary = "Create comment reply", description = "Create comment reply")
    @PostMapping("/{postId}/comments/{parentCommentId}/replies")
    public ResponseEntity<ApiResponse> createReply(
            @PathVariable("postId") String postId,
            @PathVariable("parentCommentId") String commentId,
            @Valid @RequestBody CommentCreateUpdateRequest dto) {
        log.info("POST " + urlRoute + "/{}/comments/{}/replies", postId, commentId);
        try {
            service.saveDataCommentReply(postId, dto, commentId);
            return ResponseEntity.created(URI.create(urlRoute))
                    .body(new ApiResponse(true, "Successfully created comments"));
        } catch (BadRequestException e) {
            return ResponseEntity.badRequest().body(new ApiResponse(false, e.getMessage()));
        }
    }

    @Operation(summary = "Update comment reply", description = "Update comment reply")
    @PutMapping("/{postId}/comments/{parentCommentId}/replies/{id}")
    public ResponseEntity<ApiResponse> updateReply(
            @PathVariable("postId") String postId,
            @PathVariable("parentCommentId") String parentCommentId,
            @PathVariable("id") String id,
            @Valid @RequestBody CommentCreateUpdateRequest dto) {
        log.info("PUT " + urlRoute + "/{}/comments/{}/replies/{} endpoint hit", postId, parentCommentId, id);
        try {
            service.updateDataCommentReply(postId, parentCommentId, dto, id);
            return ResponseEntity.ok(new ApiResponse(true, "Successfully updated comments"));
        } catch (BadRequestException e) {
            return ResponseEntity.badRequest().body(new ApiResponse(false, e.getMessage()));
        }
    }

    @Operation(summary = "Delete comment reply", description = "Delete comment reply")
    @PreAuthorize("isAuthenticated()")
    @DeleteMapping("/{postId}/comments/{parentCommentId}/replies/{id}")
    public ResponseEntity<ApiResponse> deleteReply(
            @PathVariable("postId") String postId,
            @PathVariable("parentCommentId") String commentId,
            @PathVariable("id") String id) {
        log.info("DELETE " + urlRoute + "/{}/comments/{}/replies/{} endpoint hit", postId, commentId, id);
        try {
            service.deleteDataCommentReply(postId, commentId, id);
            return ResponseEntity.ok(new ApiResponse(true, "Successfully deleted comments"));
        } catch (BadRequestException e) {
            return ResponseEntity.badRequest().body(new ApiResponse(false, e.getMessage()));
        }
    }


}