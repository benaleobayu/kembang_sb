package com.bca.byc.controller;


import com.bca.byc.enums.UserType;
import com.bca.byc.exception.BadRequestException;
import com.bca.byc.model.BulkByIdRequest;
import com.bca.byc.model.UserManagementDetailResponse;
import com.bca.byc.model.UserManagementFilterList;
import com.bca.byc.model.UserManagementListResponse;
import com.bca.byc.model.export.ExportFilterRequest;
import com.bca.byc.response.*;
import com.bca.byc.service.UserDeletedService;
import com.bca.byc.service.UserManagementExportService;
import com.bca.byc.service.UserManagementService;
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

import static com.bca.byc.controller.UserDeletedController.urlRoute;

@Slf4j
@RestController
@AllArgsConstructor
@RequestMapping(urlRoute)
@Tag(name = "User Deleted API")
@SecurityRequirement(name = "Authorization")
public class UserDeletedController {

    static final String urlRoute = "/cms/v1/um/deleted";
    private final UserDeletedService service;
    private final UserManagementService userManagementService;
    private final UserManagementExportService exportService;

    @Operation(summary = "Get list user deleted", description = "Get list user deleted")
    @GetMapping
    public ResponseEntity<?> listFollowUser(
            @RequestParam(name = "pages", required = false, defaultValue = "0") Integer pages,
            @RequestParam(name = "limit", required = false, defaultValue = "10") Integer limit,
            @RequestParam(name = "sortBy", required = false, defaultValue = "updatedAt") String sortBy,
            @RequestParam(name = "direction", required = false, defaultValue = "desc") String direction,
            @RequestParam(name = "keyword", required = false) String keyword,
            @RequestParam(name = "location", required = false) Long locationId,
            @RequestParam(name = "segmentation", required = false) UserType segmentation,
            @RequestParam(name = "isSenior", required = false) Boolean isSenior,
            @RequestParam(name = "startDate", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam(name = "endDate", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate,
            @RequestParam(name = "export", required = false) Boolean export,
            HttpServletResponse response
    ) {
        if (Boolean.TRUE.equals(export)) {
            // Export logic
            response.setContentType("application/octet-stream");
            String headerKey = "Content-Disposition";
            String headerValue = "attachment; filename=deleted-user.xls";
            response.setHeader(headerKey, headerValue);

            ExportFilterRequest filter = new ExportFilterRequest(startDate, endDate, locationId, segmentation);
            try {
                exportService.exportExcelPreRegister(response, filter);
            } catch (IOException e) {
                log.error("Error exporting data", e);
                return ResponseEntity.internalServerError().body("Error exporting data");
            }
            return ResponseEntity.ok().build(); // Return an empty response as the file is handled in the export method
        } else {
            // response true
            UserManagementFilterList filter = new UserManagementFilterList(startDate, endDate, locationId, segmentation, isSenior);
            ResultPageResponseDTO<UserManagementListResponse> result = service.listData(pages, limit, sortBy, direction, keyword, filter);
            return ResponseEntity.ok().body(new PaginationCmsResponse<>(true, "Success get list deleted user", result, userManagementService.listAttributeUserManagement()));
        }
    }

    @Operation(summary = "Get detail user deleted", description = "Get detail user deleted")
    @GetMapping("{id}")
    public ResponseEntity<?> getById(@PathVariable("id") String id) {
        log.info("GET" + urlRoute + "/{id} endpoint hit");
        try {
            UserManagementDetailResponse item = service.findDataById(id);
            return ResponseEntity.ok(new ApiDataResponse<>(true, "Successfully found user", item));
        } catch (BadRequestException e) {
            return ResponseEntity.badRequest().body(new ApiResponse(false, e.getMessage()));
        }
    }

    @Operation(summary = "Restore user deleted by id", description = "Restore user deleted by id")
    @PutMapping("/{id}/restore")
    public ResponseEntity<?> restore(@PathVariable("id") String id) {
        log.info("PUT " + urlRoute + "/{id}/restore endpoint hit");
        try {
            service.makeUserIsDeletedFalse(id);
            return ResponseEntity.ok(new ApiDataResponse<>(true, "Successfully restored user", null));
        } catch (BadRequestException e) {
            return ResponseEntity.badRequest().body(new ApiResponse(false, e.getMessage()));
        }
    }

    @Operation(summary = "Bulk Restore user deleted by id", description = "Bulk Restore user deleted by id")
    @PostMapping("/restore")
    public ResponseEntity<?> restore(@RequestBody BulkByIdRequest dto) {
        log.info("POST " + urlRoute + "/restore endpoint hit");
        try {
            service.makeUserBulkRestoreTrue(dto.getIds());
            return ResponseEntity.ok(new ApiDataResponse<>(true, "Successfully restored user", null));
        } catch (BadRequestException e) {
            return ResponseEntity.badRequest().body(new ApiResponse(false, e.getMessage()));
        }
    }

    @Operation(summary = "Bulk Delete user delete by id", description = "Bulk Delete user delete by id")
    @PostMapping("/delete")
    public ResponseEntity<?> bulkDelete(@RequestBody BulkByIdRequest dto) {
        log.info("POST " + urlRoute + "/delete endpoint hit");
        try {
            userManagementService.makeUserBulkHardDeleteTrue(dto.getIds());
            return ResponseEntity.ok(new ApiDataResponse(true, "Successfully deleted user", null));
        } catch (BadRequestException e) {
            return ResponseEntity.badRequest().body(new ApiResponse(false, e.getMessage()));
        }
    }

    @Operation(summary = "Export user deleted", description = "Export user deleted", hidden = true)
    @GetMapping("/export")
    public void exportExcel(HttpServletResponse response) throws IOException {
        response.setContentType("application/octet-stream");
        String headerKey = "Content-Disposition";
        String headerValue = "attachment; filename=user-deleted.xls";
        response.setHeader(headerKey, headerValue);
        exportService.exportExcelUserDeleted(response);
    }

}