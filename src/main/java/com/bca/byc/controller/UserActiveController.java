package com.bca.byc.controller;


import com.bca.byc.model.Elastic.UserActiveElastic;
import com.bca.byc.model.UserManagementDetailResponse;
import com.bca.byc.response.*;
import com.bca.byc.service.UserActiveService;
import com.bca.byc.service.UserActiveUpdateRequest;
import com.bca.byc.service.UserManagementExportService;
import io.jsonwebtoken.ExpiredJwtException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.time.LocalDate;

import static com.bca.byc.controller.UserActiveController.urlRoute;

@Slf4j
@RestController
@RequestMapping(urlRoute)
@AllArgsConstructor
@Tag(name = "CMS User Active API")
@SecurityRequirement(name = "Authorization")
public class UserActiveController {

    static final String urlRoute = "/cms/v1/um/active";
    private final UserActiveService service;
    private UserManagementExportService exportService;

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

    @GetMapping
    public ResponseEntity<PaginationResponse<ResultPageResponseDTO<UserManagementDetailResponse>>> listFollowUser(
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
        try {
            return ResponseEntity.ok().body(new PaginationResponse<>(true, "Success get list user", service.listData(pages, limit, sortBy, direction, keyword, locationId, startDate, endDate)));
        } catch (ExpiredJwtException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new PaginationResponse<>(false, "Unauthorized", null));
        }
    }


    @GetMapping("/all")
    public ResponseEntity<ApiDataResponse> getAll() {
        log.info("GET " + urlRoute + "/all endpoint hit");
        try {
            return ResponseEntity.ok(new ApiDataResponse<>(true, "Successfully found user active", service.findAllData()));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new ApiDataResponse<>(false, e.getMessage(), null));
        }
    }

    @GetMapping("{id}")
    public ResponseEntity<ApiDataResponse> getById(@PathVariable("id") Long id) {
        log.info("GET " + urlRoute + "/{id} endpoint hit");
        try {
            UserManagementDetailResponse item = service.findDataById(id);
            return ResponseEntity.ok(new ApiDataResponse<>(true, "Successfully found user active", item));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new ApiDataResponse<>(false, e.getMessage(), null));
        }
    }

    @PutMapping("{id}")
    public ResponseEntity<ApiResponse> update(@PathVariable("id") Long id, @Valid @RequestBody UserActiveUpdateRequest item) {
        log.info("PUT " + urlRoute + "/{id} endpoint hit");
        try {
            service.updateData(id, item);
            return ResponseEntity.ok(new ApiResponse(true, "Successfully updated user active"));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new ApiResponse(false, e.getMessage()));
        }
    }

    @DeleteMapping("{id}")
    public ResponseEntity<ApiResponse> delete(@PathVariable("id") Long id) {
        log.info("DELETE " + urlRoute + "/{id} endpoint hit");
        try {
            service.deleteData(id);
            return ResponseEntity.ok(new ApiResponse(true, "Successfully deleted user active"));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new ApiResponse(false, e.getMessage()));
        }
    }

    @PatchMapping("{id}/suspend")
    public ResponseEntity<ApiResponse> suspend(@PathVariable("id") Long id) {
        log.info("PATCH " + urlRoute + "/{id}/suspend endpoint hit");
        try {
            service.suspendData(id);
            return ResponseEntity.ok(new ApiResponse(true, "Successfully suspended user active"));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new ApiResponse(false, e.getMessage()));
        }
    }

    @GetMapping("/export")
    public void exportExcel(HttpServletResponse response) throws IOException {
        log.info("GET " + urlRoute + "/export endpoint hit");
        response.setContentType("application/octet-stream");
        String headerKey = "Content-Disposition";
        String headerValue = "attachment; filename=pre-register.xls";
        response.setHeader(headerKey, headerValue);
        exportService.exportExcelPreRegister(response);
    }
}