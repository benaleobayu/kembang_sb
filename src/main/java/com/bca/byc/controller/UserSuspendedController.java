package com.bca.byc.controller;


import com.bca.byc.exception.BadRequestException;
import com.bca.byc.model.BulkByIdRequest;
import com.bca.byc.model.UserManagementDetailResponse;
import com.bca.byc.response.*;
import com.bca.byc.service.UserManagementExportService;
import com.bca.byc.service.UserManagementService;
import com.bca.byc.service.UserSuspendedService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.time.LocalDate;

import static com.bca.byc.controller.UserSuspendedController.urlRoute;

@Slf4j
@RestController
@AllArgsConstructor
@RequestMapping(urlRoute)
@Tag(name = "User Suspended API")
@SecurityRequirement(name = "Authorization")
public class UserSuspendedController {

    static final String urlRoute = "/cms/v1/um/suspended";
    private final UserSuspendedService service;
    private final UserManagementService userManagementService;
    private final UserManagementExportService exportService;

    @Operation(summary = "Get list user suspended", description = "Get list user suspended")
    @GetMapping
    public ResponseEntity<PaginationCmsResponse<ResultPageResponseDTO<UserManagementDetailResponse>>> listFollowUser(
            @RequestParam(name = "pages", required = false, defaultValue = "0") Integer pages,
            @RequestParam(name = "limit", required = false, defaultValue = "10") Integer limit,
            @RequestParam(name = "sortBy", required = false, defaultValue = "name") String sortBy,
            @RequestParam(name = "direction", required = false, defaultValue = "asc") String direction,
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

    @Operation(summary = "Get user suspended by id", description = "Get user suspended by id")
    @GetMapping("{id}")
    public ResponseEntity<?> getById(@PathVariable("id") Long id) {
        log.info("GET" + urlRoute + "/{id} endpoint hit");
        try {
            UserManagementDetailResponse item = service.findDataById(id);
            return ResponseEntity.ok(new ApiDataResponse<>(true, "Successfully found user", item));
        } catch (BadRequestException e) {
            return ResponseEntity.badRequest().body(new ApiResponse(false, e.getMessage()));
        }
    }

    @Operation(summary = "Delete user suspended by id", description = "Delete user suspended by id")
    @PatchMapping("/{id}/delete")
    public ResponseEntity<?> delete(@PathVariable("id") Long id) {
        log.info("PATCH " + urlRoute + "/{id}/delete endpoint hit");
        try {
            service.makeUserIsDeletedTrue(id);
            return ResponseEntity.ok(new ApiDataResponse(true, "Successfully deleted user", null));
        } catch (BadRequestException e) {
            return ResponseEntity.badRequest().body(new ApiResponse(false, e.getMessage()));
        }
    }

    @Operation(summary = "Bulk Delete user suspended by id", description = "Bulk Delete user suspended by id")
    @PostMapping("/delete")
    public ResponseEntity<?> delete(@RequestBody BulkByIdRequest dto) {
        log.info("POST " + urlRoute + "/delete endpoint hit");
        try {
            service.makeUserBulkDeleteTrue(dto.getIds());
            return ResponseEntity.ok(new ApiDataResponse(true, "Successfully deleted user", null));
        } catch (BadRequestException e) {
            return ResponseEntity.badRequest().body(new ApiResponse(false, e.getMessage()));
        }
    }

    @Operation(summary = "Restore user suspended by id", description = "Restore user suspended by id")
    @PatchMapping("/{id}/restore")
    public ResponseEntity<?> restore(@PathVariable("id") Long id) {
        log.info("PATCH " + urlRoute + "/{id}/restore endpoint hit");
        try {
            service.makeUserIsSuspendedFalse(id);
            return ResponseEntity.ok(new ApiDataResponse(true, "Successfully restored user", null));
        } catch (BadRequestException e) {
            return ResponseEntity.badRequest().body(new ApiResponse(false, e.getMessage()));
        }
    }

    @Operation(summary = "Bulk Restore user suspended by id", description = "Bulk Restore user suspended by id")
    @PostMapping("/restore")
    public ResponseEntity<?> restore(@RequestBody BulkByIdRequest dto) {
        log.info("POST " + urlRoute + "/restore endpoint hit");
        try {
            service.makeUserBulkRestoreTrue(dto.getIds());
            return ResponseEntity.ok(new ApiDataResponse(true, "Successfully restored user", null));
        } catch (BadRequestException e) {
            return ResponseEntity.badRequest().body(new ApiResponse(false, e.getMessage()));
        }
    }

    @Operation(summary = "Export Excel", description = "Export Excel")
    @GetMapping("/export")
    public void exportExcel(HttpServletResponse response) throws IOException {
        response.setContentType("application/octet-stream");
        String headerKey = "Content-Disposition";
        String headerValue = "attachment; filename=user-suspended.xls";
        response.setHeader(headerKey, headerValue);
        exportService.exportExcelUserSuspended(response);
    }

}