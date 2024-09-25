package com.bca.byc.controller;

import com.bca.byc.exception.BadRequestException;
import com.bca.byc.model.AdminCmsDetailResponse;
import com.bca.byc.model.AdminCreateRequest;
import com.bca.byc.model.AdminDetailResponse;
import com.bca.byc.model.AdminUpdateRequest;
import com.bca.byc.response.*;
import com.bca.byc.service.AdminService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

import static com.bca.byc.controller.AdminController.urlRoute;

@Slf4j
@RestController
@AllArgsConstructor
@RequestMapping(urlRoute)
@Tag(name = "Admin API")
@SecurityRequirement(name = "Authorization")
public class AdminController {

    static final String urlRoute = "/cms/v1/am/admin";

    private AdminService service;

    @PreAuthorize("hasAuthority('admin.view')")
    @Operation(summary = "Get List Admin", description = "Get List Admin")
    @GetMapping
    public ResponseEntity<PaginationAppsResponse<ResultPageResponseDTO<AdminDetailResponse>>> listFollowUser(
            @RequestParam(name = "pages", required = false, defaultValue = "0") Integer pages,
            @RequestParam(name = "limit", required = false, defaultValue = "10") Integer limit,
            @RequestParam(name = "sortBy", required = false, defaultValue = "id") String sortBy,
            @RequestParam(name = "direction", required = false, defaultValue = "asc") String direction,
            @RequestParam(name = "keyword", required = false) String keyword) {
        // response true
        log.info("GET " + urlRoute + " endpoint hit");
        return ResponseEntity.ok().body(new PaginationAppsResponse<>(true, "Success get list user", service.listData(pages, limit, sortBy, direction, keyword)));

    }

    @PreAuthorize("hasAuthority('admin.view')")
    @Operation(summary = "Get Admin Detail", description = "Get Admin Detail")
    @GetMapping("{id}")
    public ResponseEntity<?> getById(@PathVariable("id") Long id) {
        log.info("GET " + urlRoute + "/{id} endpoint hit");
        try {
            AdminDetailResponse item = service.findDataById(id);
            return ResponseEntity.ok(new ApiDataResponse<>(true, "Successfully found admin", item));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new ApiResponse(false, e.getMessage()));
        }
    }

    @PreAuthorize("hasAuthority('admin.create')")
    @Operation(summary = "Create Admin", description = "Create Admin")
    @PostMapping
    public ResponseEntity<ApiResponse> create(@Valid @RequestBody AdminCreateRequest item) {
        log.info("POST " + urlRoute + " endpoint hit");
        try {
            service.saveData(item);
            return ResponseEntity.created(URI.create("/cms/v1/am/admin/"))
                    .body(new ApiResponse(true, "Successfully created admin"));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new ApiResponse(false, e.getMessage()));
        }
    }

    @PreAuthorize("hasAuthority('admin.update')")
    @Operation(summary = "Update Admin", description = "Update Admin")
    @PutMapping("{id}")
    public ResponseEntity<ApiResponse> update(@PathVariable("id") Long id, @Valid @RequestBody AdminUpdateRequest item) {
        log.info("PUT " + urlRoute + "/{id} endpoint hit");
        try {
            service.updateData(id, item);
            return ResponseEntity.ok(new ApiResponse(true, "Successfully updated admin"));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new ApiResponse(false, e.getMessage()));
        }
    }

    @PreAuthorize("hasAuthority('admin.delete')")
    @Operation(summary = "Delete Admin", description = "Delete Admin")
    @DeleteMapping("{id}")
    public ResponseEntity<ApiResponse> delete(@PathVariable("id") Long id) {
        log.info("DELETE " + urlRoute + "/{id} endpoint hit");
        try {
            service.deleteData(id);
            return ResponseEntity.ok(new ApiResponse(true, "Successfully deleted admin"));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new ApiResponse(false, e.getMessage()));
        }
    }

    @PreAuthorize("isAuthenticated()")
    @Operation(summary = "Get Admin Detail", description = "Get Admin Detail")
    @GetMapping("/info")
    public ResponseEntity<?> getUserDetail(@AuthenticationPrincipal UserDetails userDetails) {
        if (userDetails != null) {
            String email = userDetails.getUsername(); // Assuming email is used as the username
            log.info("GET " + urlRoute + "/info endpoint hit on email : {}", userDetails.getUsername());
            AdminCmsDetailResponse data = service.getAdminDetail(email);
            return ResponseEntity.ok(new ApiDataResponse<>(true, "User found", data));
        } else {
            log.error("GET " + urlRoute + "/info endpoint hit on email : {}", userDetails.getUsername());
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(new ApiResponse(false, "Unauthorized access"));
        }
    }

    @PreAuthorize("isAuthenticated()")
    @Operation(summary = "Get Admin Detail", description = "Get Admin Detail")
    @GetMapping("/detail")
    public ResponseEntity<AdminPermissionResponse> getAdminDetail(@AuthenticationPrincipal UserDetails userDetails) {
        if (userDetails != null) {
            String email = userDetails.getUsername(); // Assuming email is used as the username
            AdminPermissionResponse data = service.getPermissionDetail(email);
            return ResponseEntity.ok().body(data);
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(new AdminPermissionResponse());
        }
    }
}