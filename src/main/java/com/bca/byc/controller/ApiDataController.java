package com.bca.byc.controller;

import com.bca.byc.entity.BusinessCategory;
import com.bca.byc.entity.PostCategory;
import com.bca.byc.exception.BadRequestException;
import com.bca.byc.model.SecureIdAndNameResponse;
import com.bca.byc.model.data.ListTagUserResponse;
import com.bca.byc.model.data.TagDetailResponse;
import com.bca.byc.response.ApiDataResponse;
import com.bca.byc.response.ApiResponse;
import com.bca.byc.response.PaginationAppsResponse;
import com.bca.byc.response.ResultPageResponseDTO;
import com.bca.byc.service.*;
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
    private final BusinessCategoryService businessCategoryService;
    private final ReasonReportService reasonReportService;

    // reason report category
    @GetMapping("/reason-report")
    public ResponseEntity<?> ReasonReportList() {
        log.info("GET /api/v1/data/reason-report endpoint hit");
        try {
            return ResponseEntity.ok(new ApiDataResponse<>(true, "Successfully found reason report", reasonReportService.findAllData()));
        } catch (BadRequestException e) {
            return ResponseEntity.badRequest().body(new ApiResponse(false, e.getMessage()));
        }
    }

    @PreAuthorize("isAuthenticated()")
    @Operation(summary = "Get all tags", description = "Get all tags")
    @GetMapping("/tag")
    public ResponseEntity<PaginationAppsResponse<ResultPageResponseDTO<TagDetailResponse>>> TagList(
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
    public ResponseEntity<PaginationAppsResponse<ResultPageResponseDTO<ListTagUserResponse>>> TagUserList(
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
    public ResponseEntity<PaginationAppsResponse<ResultPageResponseDTO<SecureIdAndNameResponse>>> PostCategoryList(
            @RequestParam(name = "pages", required = false, defaultValue = "0") Integer pages,
            @RequestParam(name = "limit", required = false, defaultValue = "10") Integer limit,
            @RequestParam(name = "sortBy", required = false, defaultValue = "name") String sortBy,
            @RequestParam(name = "direction", required = false, defaultValue = "asc") String direction,
            @RequestParam(name = "keyword", required = false) String keyword) {
        // response true
        try {
            return ResponseEntity.ok().body(new PaginationAppsResponse<>(true, "Success get list post category", businessCategoryService.listDataPostCategory(pages, limit, sortBy, direction, keyword)));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new PaginationAppsResponse<>(false, e.getMessage(), null));
        }
    }


}