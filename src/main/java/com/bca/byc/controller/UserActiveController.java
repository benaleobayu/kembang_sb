package com.bca.byc.controller;


import com.bca.byc.exception.BadRequestException;
import com.bca.byc.model.BulkByIdRequest;
import com.bca.byc.model.Elastic.UserActiveElastic;
import com.bca.byc.model.LogUserManagementRequest;
import com.bca.byc.model.UserManagementDetailResponse;
import com.bca.byc.model.UserManagementListResponse;
import com.bca.byc.response.*;
import com.bca.byc.service.UserActiveService;
import com.bca.byc.service.UserActiveUpdateRequest;
import com.bca.byc.service.UserManagementExportService;
import com.bca.byc.service.UserManagementService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.time.LocalDate;

import static com.bca.byc.controller.UserActiveController.urlRoute;

@Slf4j
@RestController
@RequestMapping(urlRoute)
@AllArgsConstructor
@Tag(name = "User Active API")
@SecurityRequirement(name = "Authorization")
public class UserActiveController {

    static final String urlRoute = "/cms/v1/um/active";
    private final UserActiveService service;
    private final UserManagementExportService exportService;
    private final UserManagementService userManagementService;

    // elastic search
    @Operation(hidden = true)
    @GetMapping("/list")
    public ResponseEntity<ApiDataResponse<Page<UserActiveElastic>>> getAllActiveUser(
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "10") int size
    ) {
        log.info("GET " + urlRoute + "/list endpoint hit");
        try {
            return ResponseEntity.ok(new ApiDataResponse<>(true, "Successfully found user active", service.getAllActiveUser(page, size)));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new ApiDataResponse<>(false, e.getMessage(), null));
        }
    }

    @Operation(summary = "Get list user active", description = "Get list user active")
    @GetMapping
    public ResponseEntity<PaginationCmsResponse<ResultPageResponseDTO<UserManagementDetailResponse>>> listUserActive(
            @RequestParam(name = "pages", required = false, defaultValue = "0") Integer pages,
            @RequestParam(name = "limit", required = false, defaultValue = "10") Integer limit,
            @RequestParam(name = "sortBy", required = false, defaultValue = "updatedAt") String sortBy,
            @RequestParam(name = "direction", required = false, defaultValue = "desc") String direction,
            @RequestParam(name = "keyword", required = false) String keyword,
            @RequestParam(name = "location", required = false) Long locationId,
            @RequestParam(name = "startDate", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam(name = "endDate", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate
    ) {
        // response true
        return ResponseEntity.ok().body(new PaginationCmsResponse<>(true,
                "Success get list user",
                service.listData(pages, limit, sortBy, direction, keyword, locationId, startDate, endDate),
                userManagementService.listAttributeUserManagement()));
    }

    @GetMapping("{id}")
    public ResponseEntity<ApiDataResponse> getById(@PathVariable("id") String id) {
        log.info("GET " + urlRoute + "/{id} endpoint hit");
        try {
            UserManagementDetailResponse item = service.findBySecureId(id);
            return ResponseEntity.ok(new ApiDataResponse<>(true, "Successfully found user active", item));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new ApiDataResponse<>(false, e.getMessage(), null));
        }
    }

    @PutMapping("{id}")
    public ResponseEntity<ApiResponse> update(@PathVariable("id") String id, @Valid @RequestBody UserActiveUpdateRequest item) {
        log.info("PUT " + urlRoute + "/{id} endpoint hit");
        try {
            service.updateData(id, item);
            return ResponseEntity.ok(new ApiResponse(true, "Successfully updated user active"));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new ApiResponse(false, e.getMessage()));
        }
    }

    @DeleteMapping("{id}")
    public ResponseEntity<ApiResponse> delete(@PathVariable("id") String id) {
        log.info("DELETE " + urlRoute + "/{id} endpoint hit");
        try {
            service.deleteData(id);
            return ResponseEntity.ok(new ApiResponse(true, "Successfully deleted user active"));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new ApiResponse(false, e.getMessage()));
        }
    }

    @PutMapping("{id}/suspend")
    public ResponseEntity<ApiResponse> suspend(@PathVariable("id") String id, @Valid @RequestBody LogUserManagementRequest dto) {
        log.info("PUT " + urlRoute + "/{id}/suspend endpoint hit");
        try {
            service.suspendData(id, dto);
            return ResponseEntity.ok(new ApiResponse(true, "Successfully suspended user active"));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new ApiResponse(false, e.getMessage()));
        }
    }

    @Operation(summary = "Bulk Suspend user by id", description = "Bulk Suspend user by id")
    @PostMapping("/suspend")
    public ResponseEntity<?> suspend(@RequestBody BulkByIdRequest dto) {
        log.info("POST " + urlRoute + "/suspend endpoint hit");
        try {
            service.makeUserBulkSuspendedTrue(dto.getIds());
            return ResponseEntity.ok(new ApiResponse(true, "Successfully suspended user"));
        } catch (BadRequestException e) {
            return ResponseEntity.badRequest().body(new ApiResponse(false, e.getMessage()));
        }
    }

    @GetMapping("/export")
    public void exportExcel(HttpServletResponse response) throws IOException {
        log.info("GET " + urlRoute + "/export endpoint hit");
        response.setContentType("application/octet-stream");
        String headerKey = "Content-Disposition";
        String headerValue = "attachment; filename=user-active.xls";
        response.setHeader(headerKey, headerValue);
        exportService.exportExcelUserActive(response);
    }
}