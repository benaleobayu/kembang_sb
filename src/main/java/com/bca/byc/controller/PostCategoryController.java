package com.bca.byc.controller;

import com.bca.byc.entity.PostCategory;
import com.bca.byc.exception.BadRequestException;
import com.bca.byc.model.PostCategoryCreateUpdateRequest;
import com.bca.byc.model.PostCategoryDetailResponse;
import com.bca.byc.response.ApiResponse;
import com.bca.byc.response.PaginationAppsResponse;
import com.bca.byc.response.ResultPageResponseDTO;
import com.bca.byc.service.PostCategoryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@Slf4j
@RestController
@AllArgsConstructor
@RequestMapping(PostCategoryController.urlRoute)
@Tag(name = "Post Category API [Masterdata]")
@SecurityRequirement(name = "Authorization")
public class PostCategoryController {

    static final String urlRoute = "/cms/v1/ms/post-categories";
    private PostCategoryService service;

    @GetMapping
    public ResponseEntity<PaginationAppsResponse<ResultPageResponseDTO<PostCategory>>> listData(
            @RequestParam(name = "pages", required = false, defaultValue = "0") Integer pages,
            @RequestParam(name = "limit", required = false, defaultValue = "10") Integer limit,
            @RequestParam(name = "sortBy", required = false, defaultValue = "name") String sortBy,
            @RequestParam(name = "direction", required = false, defaultValue = "asc") String direction,
            @RequestParam(name = "keyword", required = false) String keyword) {
        // response true
        try{
            return ResponseEntity.ok().body(new PaginationAppsResponse<>(true, "Success get list post category", service.listData(pages, limit, sortBy, direction, keyword)));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new PaginationAppsResponse<>(false, e.getMessage(), null));
        }
    }

    @Operation(summary = "Get all post categories", description = "Get all post categories", hidden = true)
    @GetMapping("/all")
    public ResponseEntity<ApiResponse> getAll() {
        log.info("GET " + urlRoute + " endpoint hit");
        try {
            return ResponseEntity.ok(new ApiResponse(true, "Successfully found post categories", service.findAllData()));
        } catch (BadRequestException e) {
            return ResponseEntity.badRequest().body(new ApiResponse(false, e.getMessage(), null));
        }
    }

    @Operation(summary = "Get post category by id", description = "Get post category by id")
    @GetMapping("{id}")
    public ResponseEntity<ApiResponse> getById(@PathVariable("id") Long id) {
        log.info("GET " + urlRoute + "/{id} endpoint hit");
        try {
            PostCategoryDetailResponse item = service.findDataById(id);
            return ResponseEntity.ok(new ApiResponse(true, "Successfully found post categories", item));
        } catch (BadRequestException e) {
            return ResponseEntity.badRequest().body(new ApiResponse(false, e.getMessage(), null));
        }
    }

    @Operation(summary = "Create new post category", description = "Create new post category")
    @PostMapping
    public ResponseEntity<ApiResponse> create(@Valid @RequestBody PostCategoryCreateUpdateRequest item) {
        log.info("POST " + urlRoute + " endpoint hit");
        try {
            service.saveData(item);
            return ResponseEntity.created(URI.create(urlRoute))
                    .body(new ApiResponse(true, "Successfully created post categories"));
        } catch (BadRequestException e) {
            return ResponseEntity.badRequest().body(new ApiResponse(false, e.getMessage()));
        }
    }

    @Operation(summary = "Update post category by id", description = "Update post category by id")
    @PutMapping("{id}")
    public ResponseEntity<ApiResponse> update(@PathVariable("id") Long id, @Valid @RequestBody PostCategoryCreateUpdateRequest item) {
        log.info("PUT " + urlRoute + "/{id} endpoint hit");
        try {
            service.updateData(id, item);
            return ResponseEntity.ok(new ApiResponse(true, "Successfully updated post categories"));
        } catch (BadRequestException e) {
            return ResponseEntity.badRequest().body(new ApiResponse(false, e.getMessage()));
        }
    }

    @Operation(summary = "Delete post category by id", description = "Delete post category by id")
    @DeleteMapping("{id}")
    public ResponseEntity<ApiResponse> delete(@PathVariable("id") Long id) {
        log.info("DELETE " + urlRoute + "/{id} endpoint hit");
        try {
            service.deleteData(id);
            return ResponseEntity.ok(new ApiResponse(true, "Successfully deleted post categories"));
        } catch (BadRequestException e) {
            return ResponseEntity.badRequest().body(new ApiResponse(false, e.getMessage()));
        }
    }
}