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
@RequestMapping(PostCommentController.urlRoute)
@Tag(name = "Comment API")
@SecurityRequirement(name = "Authorization")
@PreAuthorize("isAuthenticated()")
public class PostCommentController {

    static final String urlRoute = "/api/v1/post";
    private CommentService service;

    @Operation(summary = "Get list comment", description = "Get list comment")
    @GetMapping("/{postId}/comments")
    public ResponseEntity<PaginationAppsResponse<ResultPageResponseDTO<ListCommentResponse>>> listDataComment(
            @RequestParam(name = "pages", required = false, defaultValue = "0") Integer pages,
            @RequestParam(name = "limit", required = false, defaultValue = "10") Integer limit,
            @RequestParam(name = "sortBy", required = false, defaultValue = "id") String sortBy,
            @RequestParam(name = "direction", required = false, defaultValue = "asc") String direction,
            @RequestParam(name = "keyword", required = false) String keyword,
            @PathVariable("postId") String postId) {
        // response true
        String email = ContextPrincipal.getPrincipal();
        log.info("GET " + urlRoute + " endpoint hit, email = {} ", email);

        return ResponseEntity.ok().body(new PaginationAppsResponse<>(true, "Success get list comment", service.listDataComment(pages, limit, sortBy, direction, keyword, postId)));
    }

    @Operation(summary = "Get comment by id", description = "Get comment by id")
    @GetMapping("/{postId}/comments/{id}")
    public ResponseEntity<?> getById(@PathVariable("postId") String postId, @PathVariable("id") String commentId) {
        log.info("GET " + urlRoute + "/{id} endpoint hit");
        try {
            CommentDetailResponse item = service.findDataById(postId, commentId);
            return ResponseEntity.ok(new ApiDataResponse(true, "Successfully found post comments", item));
        } catch (BadRequestException e) {
            return ResponseEntity.badRequest().body(new ApiResponse(false, e.getMessage()));
        }
    }

    @Operation(summary = "Create comment", description = "Create comment")
    @PostMapping("/{postId}/comments")
    public ResponseEntity<ApiResponse> create(@PathVariable("postId") String postId, @Valid @RequestBody CommentCreateUpdateRequest item) {
        log.info("POST " + urlRoute + " endpoint hit");
        String email = ContextPrincipal.getPrincipal();
        try {
            service.saveData(postId, item, email);
            return ResponseEntity.created(URI.create(urlRoute))
                    .body(new ApiResponse(true, "Successfully created post comments"));
        } catch (BadRequestException e) {
            return ResponseEntity.badRequest().body(new ApiResponse(false, e.getMessage()));
        }
    }

    @Operation(summary = "Update comment", description = "Update comment")
    @PutMapping("/{postId}/comments/{id}")
    public ResponseEntity<ApiResponse> update(@PathVariable("postId") String postId, @PathVariable("id") String commentId, @Valid @RequestBody CommentCreateUpdateRequest item) {
        String email = ContextPrincipal.getPrincipal();
        log.info("PUT " + urlRoute + "/{id} endpoint hit");
        try {
            service.updateData(postId, commentId, item, email);
            return ResponseEntity.ok(new ApiResponse(true, "Successfully updated post comments"));
        } catch (BadRequestException e) {
            return ResponseEntity.badRequest().body(new ApiResponse(false, e.getMessage()));
        }
    }

    @Operation(summary = "Delete comment", description = "Delete comment")
    @PreAuthorize("isAuthenticated()")
    @DeleteMapping("/{postId}/comments/{id}")
    public ResponseEntity<ApiResponse> delete(@PathVariable("postId") String postId, @PathVariable("id") String commentId) {
        String email = ContextPrincipal.getPrincipal();
        log.info("DELETE " + urlRoute + "/{}/comments/{} endpoint hit", postId, commentId);
        try {
            service.deleteData(postId, commentId, email);
            return ResponseEntity.ok(new ApiResponse(true, "Successfully deleted post comments"));
        } catch (BadRequestException e) {
            return ResponseEntity.badRequest().body(new ApiResponse(false, e.getMessage()));
        }
    }


}