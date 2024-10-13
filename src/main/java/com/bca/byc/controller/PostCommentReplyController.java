package com.bca.byc.controller;


import com.bca.byc.exception.BadRequestException;
import com.bca.byc.model.apps.CommentCreateUpdateRequest;
import com.bca.byc.model.apps.ListCommentReplyResponse;
import com.bca.byc.model.returns.ReturnCommentResponse;
import com.bca.byc.response.ApiDataResponse;
import com.bca.byc.response.ApiResponse;
import com.bca.byc.response.PaginationAppsResponse;
import com.bca.byc.response.ResultPageResponseDTO;
import com.bca.byc.security.util.ContextPrincipal;
import com.bca.byc.service.CommentReplyService;
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
    private CommentReplyService service;


    @Operation(summary = "Get list comment", description = "Get list comment")
    @GetMapping("/{postId}/comments/{parentCommentId}/replies")
    public ResponseEntity<PaginationAppsResponse<ResultPageResponseDTO<ListCommentReplyResponse>>> listDataComment(
            @RequestParam(name = "pages", required = false, defaultValue = "0") Integer pages,
            @RequestParam(name = "limit", required = false, defaultValue = "10") Integer limit,
            @RequestParam(name = "sortBy", required = false, defaultValue = "id") String sortBy,
            @RequestParam(name = "direction", required = false, defaultValue = "asc") String direction,
            @RequestParam(name = "keyword", required = false) String keyword,
            @PathVariable("postId") String postId,
            @PathVariable("parentCommentId") String parentCommentId) {
        // response true
        String email = ContextPrincipal.getPrincipal();
        log.info("GET " + urlRoute + " endpoint hit, email = {} ", email);

        return ResponseEntity.ok().body(new PaginationAppsResponse<>(true, "Success get list comment", service.listDataCommentReplies(pages, limit, sortBy, direction, keyword, postId, parentCommentId)));
    }


    @Operation(summary = "Create comment reply", description = "Create comment reply")
    @PostMapping("/{postId}/comments/{parentCommentId}/replies")
    public ResponseEntity<?> createReply(
            @PathVariable("postId") String postId,
            @PathVariable("parentCommentId") String commentId,
            @Valid @RequestBody CommentCreateUpdateRequest dto) {
        log.info("POST " + urlRoute + "/{}/comments/{}/replies", postId, commentId);
        try {
            ReturnCommentResponse total = service.saveDataCommentReply(postId, dto, commentId);
            return ResponseEntity.created(URI.create(urlRoute))
                    .body(new ApiDataResponse<>(true, "Successfully created comments", total));
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