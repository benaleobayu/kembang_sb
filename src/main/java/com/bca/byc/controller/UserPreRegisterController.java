package com.bca.byc.controller;

import com.bca.byc.entity.AppAdmin;
import com.bca.byc.enums.AdminApprovalStatus;
import com.bca.byc.exception.BadRequestException;
import com.bca.byc.model.*;
import com.bca.byc.model.export.ExportFilterRequest;
import com.bca.byc.response.*;
import com.bca.byc.service.PreRegisterService;
import com.bca.byc.service.UserManagementExportService;
import com.bca.byc.service.UserManagementService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.net.URI;
import java.time.LocalDate;

import static com.bca.byc.controller.UserPreRegisterController.urlRoute;

@Slf4j
@RestController
@AllArgsConstructor
@Validated
@RequestMapping(urlRoute)
@Tag(name = "User Pre-Register API")
@SecurityRequirement(name = "Authorization")
public class UserPreRegisterController {

    static final String urlRoute = "/cms/v1/um/pre-register";
    private PreRegisterService service;
    private UserManagementExportService exportService;
    private UserManagementService userManagementService;

    @PreAuthorize("hasAuthority('pre-registration.view')")
    @Operation(summary = "Create Pre-Register User", description = "Create Pre-Register User")
    @GetMapping
    public ResponseEntity<?> listData(
            @RequestParam(name = "pages", required = false, defaultValue = "0") Integer pages,
            @RequestParam(name = "limit", required = false, defaultValue = "10") Integer limit,
            @RequestParam(name = "sortBy", required = false, defaultValue = "id") String sortBy,
            @RequestParam(name = "direction", required = false, defaultValue = "asc") String direction,
            @RequestParam(name = "keyword", required = false) String keyword,
            @RequestParam(name = "status", required = false) AdminApprovalStatus status,
            @RequestParam(name = "startDate", required = false) LocalDate startDate,
            @RequestParam(name = "endDate", required = false) LocalDate endDate,
            @RequestParam(name = "export", required = false) Boolean export, // New parameter to trigger export
            HttpServletResponse response // Add HttpServletResponse as a parameter for export
    ) {
        log.info("GET " + urlRoute + " list endpoint hit");

        if (Boolean.TRUE.equals(export)) {
            // Export logic
            response.setContentType("application/octet-stream");
            String headerKey = "Content-Disposition";
            String headerValue = "attachment; filename=pre-register.xls";
            response.setHeader(headerKey, headerValue);

            ExportFilterRequest filter = new ExportFilterRequest(startDate, endDate, status);
            try {
                exportService.exportExcelPreRegister(response, filter);
            } catch (IOException e) {
                log.error("Error exporting data", e);
                return ResponseEntity.internalServerError().body("Error exporting data");
            }
            return ResponseEntity.ok().build(); // Return an empty response as the file is handled in the export method
        } else {
            // List data logic
            ResultPageResponseDTO<PreRegisterDetailResponse> result = service.listData(pages, limit, sortBy, direction, keyword, status, startDate, endDate);
            return ResponseEntity.ok().body(new PaginationCmsResponse<>(true, "Success get list pre-register", result, userManagementService.listAttributePreRegister()));
        }
    }


    @PreAuthorize("hasAuthority('pre-registration.export')")
    @GetMapping("/export")
    public void exportExcel(HttpServletResponse response,
                            @RequestParam(value = "start", required = false) LocalDate start,
                            @RequestParam(value = "end", required = false) LocalDate end,
                            @RequestParam(value = "status", required = false) AdminApprovalStatus status
    ) throws IOException {
        response.setContentType("application/octet-stream");
        String headerKey = "Content-Disposition";
        String headerValue = "attachment; filename=pre-register.xls";
        response.setHeader(headerKey, headerValue);
        ExportFilterRequest filter = new ExportFilterRequest(
                start,
                end,
                status
        );
        exportService.exportExcelPreRegister(response,filter);
    }

    @PreAuthorize("hasAuthority('pre-registration.read')")
    @Operation(summary = "Get detail Pre-Register User by id", description = "Get detail Pre-Register User by id")
    @GetMapping("{id}")
    public ResponseEntity<ApiWithAttributeResponse> getById(@PathVariable("id") String id) {
        log.info("GET " + urlRoute + "/{id} endpoint hit");
        try {
            PreRegisterDetailResponse item = service.findDataBySecureId(id);
            return ResponseEntity.ok(new ApiWithAttributeResponse(true, "Successfully found pre-register user", item, userManagementService.listAttributeCreateUpdatePreRegister()));
        } catch (BadRequestException e) {
            return ResponseEntity.badRequest().body(new ApiWithAttributeResponse(false, e.getMessage(), null));
        }
    }

    @PreAuthorize("hasAuthority('pre-registration.create')")
    @Operation(summary = "Create Pre-Register User", description = "Create Pre-Register User")
    @PostMapping
    public ResponseEntity<ApiResponse> create(@Valid @RequestBody PreRegisterCreateRequest item) {
        log.info("POST " + urlRoute + " endpoint hit");
        // principal
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Object principal = authentication.getPrincipal();
        String email;
        if (principal instanceof AppAdmin) {
            email = ((AppAdmin) principal).getEmail();
        } else if (principal instanceof UserDetails) {
            email = ((UserDetails) principal).getUsername();
        } else {
            return ResponseEntity.badRequest().body(new ApiResponse(false, "Invalid authentication principal."));
        }
        System.out.println(email);

        try {
            service.saveData(item, email);
            return ResponseEntity.created(URI.create("/cms/v1/pre-register/"))
                    .body(new ApiResponse(true, "Successfully created pre-register user"));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new ApiResponse(false, e.getMessage()));
        }
    }

    @PreAuthorize("hasAuthority('pre-registration.update')")
    @Operation(summary = "Update Pre-Register User", description = "Update Pre-Register User")
    @PutMapping("{id}")
    public ResponseEntity<ApiResponse> update(@PathVariable("id") String id, @Valid @RequestBody PreRegisterUpdateRequest item) {
        log.info("PUT " + urlRoute + "/{id} endpoint hit");
        try {
            service.updateData(id, item);
            return ResponseEntity.ok(new ApiResponse(true, "Successfully updated pre-register user"));
        } catch (BadRequestException e) {
            return ResponseEntity.badRequest().body(new ApiResponse(false, e.getMessage()));
        }
    }

    @PreAuthorize("hasAuthority('pre-registration.delete')")
    @Operation(summary = "Bulk Delete user by id", description = "Bulk Delete user by id")
    @PostMapping("/delete")
    public ResponseEntity<ApiResponse> bulkDelete(@RequestBody BulkByIdRequest dto) {
        log.info("POST " + urlRoute + "/delete endpoint hit");
        try {
            service.bulkDelete(dto.getIds());
            return ResponseEntity.ok(new ApiResponse(true, "Successfully deleted user"));
        } catch (BadRequestException e) {
            return ResponseEntity.badRequest().body(new ApiResponse(false, e.getMessage()));
        }
    }

    @PreAuthorize("hasAuthority('pre-registration.update')")
    @Operation(summary = "Approve Pre-Register User", description = "Approve Pre-Register User")
    @PutMapping("{id}/approve")
    public ResponseEntity<ApiResponse> approve(@PathVariable("id") String id, @AuthenticationPrincipal UserDetails userDetails) {
        log.info("PUT " + urlRoute + "/{id}/approve endpoint hit");
        try {
            String email = userDetails.getUsername();
            service.approveData(id, email);
            return ResponseEntity.ok(new ApiResponse(true, "Successfully approved pre-register user"));
        } catch (BadRequestException e) {
            return ResponseEntity.badRequest().body(new ApiResponse(false, e.getMessage()));
        }
    }
    // patch approve

    @PreAuthorize("hasAuthority('pre-registration.update')")
    @Operation(summary = "Reject Pre-Register User", description = "Reject Pre-Register User")
    @PutMapping("{id}/reject")
    public ResponseEntity<ApiResponse> reject(
            @PathVariable("id") String id,
            @RequestBody RejectRequest reason,
            @AuthenticationPrincipal UserDetails userDetails) {
        log.info("PUT " + urlRoute + "/{id}/reject endpoint hit");
        try {
            String email = userDetails.getUsername();
            service.rejectData(id, reason, email);
            return ResponseEntity.ok(new ApiResponse(true, "Successfully rejecting pre-register user"));
        } catch (BadRequestException e) {
            return ResponseEntity.badRequest().body(new ApiResponse(false, e.getMessage()));
        }
    }

    @GetMapping("/attribute")
    public ResponseEntity<ApiAttributeResponse> listAttribute() {
        log.info("GET " + urlRoute + "/attribute endpoint hit");
        return ResponseEntity.ok(new ApiAttributeResponse(true, "Successfully get list attribute", userManagementService.listAttributeCreateUpdatePreRegister()));
    }

}