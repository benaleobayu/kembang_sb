package com.bca.byc.controller;

import com.bca.byc.entity.PostCategory;
import com.bca.byc.model.data.ListTagUserResponse;
import com.bca.byc.model.data.TagDetailResponse;
import com.bca.byc.response.PaginationAppsResponse;
import com.bca.byc.response.ResultPageResponseDTO;
import com.bca.byc.service.PostCategoryService;
import com.bca.byc.service.TagService;
import com.bca.byc.service.UserActiveService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@AllArgsConstructor
@RequestMapping(ApiDataController.urlRoute)
@Tag(name = "Apps Data API")
@SecurityRequirement(name = "Authorization")
public class ApiDataController {

    static final String urlRoute = "/api/v1/data";

    private final TagService tagService;
    private final UserActiveService tagUserService;
    private final PostCategoryService postCategoryService;

    @PreAuthorize("isAuthenticated()")
    @Operation(summary = "Get all tags", description = "Get all tags")
    @GetMapping("/tag")
    public ResponseEntity<PaginationAppsResponse<ResultPageResponseDTO<TagDetailResponse>>> listDataTag(
            @RequestParam(name = "pages", required = false, defaultValue = "0") Integer pages,
            @RequestParam(name = "limit", required = false, defaultValue = "10") Integer limit,
            @RequestParam(name = "sortBy", required = false, defaultValue = "name") String sortBy,
            @RequestParam(name = "direction", required = false, defaultValue = "asc") String direction,
            @RequestParam(name = "keyword", required = false) String keyword) {
        // response true
        try {
            return ResponseEntity.ok().body(new PaginationAppsResponse<>(true, "Success get list tag detail", tagService.listDataTagApps(pages, limit, sortBy, direction, keyword)));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new PaginationAppsResponse<>(false, e.getMessage(), null));
        }
    }

    @PreAuthorize("isAuthenticated()")
    @Operation(summary = "Get all tags user", description = "Get all tags user")
    @GetMapping("/tag-user")
    public ResponseEntity<PaginationAppsResponse<ResultPageResponseDTO<ListTagUserResponse>>> listDataTagUser(
            @RequestParam(name = "pages", required = false, defaultValue = "0") Integer pages,
            @RequestParam(name = "limit", required = false, defaultValue = "10") Integer limit,
            @RequestParam(name = "sortBy", required = false, defaultValue = "name") String sortBy,
            @RequestParam(name = "direction", required = false, defaultValue = "asc") String direction,
            @RequestParam(name = "keyword", required = false) String keyword) {
        // response true
        try {
            return ResponseEntity.ok().body(new PaginationAppsResponse<>(true, "Success get list tag detail", tagUserService.listDataTagUser(pages, limit, sortBy, direction, keyword)));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new PaginationAppsResponse<>(false, e.getMessage(), null));
        }
    }

    @PreAuthorize("isAuthenticated()")
    @Operation(summary = "Get all post categories", description = "Get all post categories")
    @GetMapping("/post-category")
    public ResponseEntity<PaginationAppsResponse<ResultPageResponseDTO<PostCategory>>> listData(
            @RequestParam(name = "pages", required = false, defaultValue = "0") Integer pages,
            @RequestParam(name = "limit", required = false, defaultValue = "10") Integer limit,
            @RequestParam(name = "sortBy", required = false, defaultValue = "name") String sortBy,
            @RequestParam(name = "direction", required = false, defaultValue = "asc") String direction,
            @RequestParam(name = "keyword", required = false) String keyword) {
        // response true
        try {
            return ResponseEntity.ok().body(new PaginationAppsResponse<>(true, "Success get list post category", postCategoryService.listData(pages, limit, sortBy, direction, keyword)));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new PaginationAppsResponse<>(false, e.getMessage(), null));
        }
    }


}