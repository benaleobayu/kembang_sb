package com.bca.byc.controller;


import com.bca.byc.exception.BadRequestException;
import com.bca.byc.model.BulkByIdRequest;
import com.bca.byc.model.UserManagementDetailResponse;
import com.bca.byc.response.ApiResponse;
import com.bca.byc.response.PaginationResponse;
import com.bca.byc.response.ResultPageResponseDTO;
import com.bca.byc.service.UserDeletedService;
import com.bca.byc.service.UserManagementExportService;
import io.jsonwebtoken.ExpiredJwtException;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.time.LocalDate;

import static com.bca.byc.controller.UserDeletedController.urlRoute;

@Slf4j
@RestController
@AllArgsConstructor
@RequestMapping(urlRoute)
@Tag(name = "CMS User Deleted API")
@SecurityRequirement(name = "Authorization")
public class UserDeletedController {

    private UserDeletedService service;
    private UserManagementExportService exportService;

    static final String urlRoute = "/cms/v1/um/deleted";

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

    @GetMapping("{id}")
    public ResponseEntity<ApiResponse> getById(@PathVariable("id") Long id) {
        log.info("GET" + urlRoute + "/{id} endpoint hit");
        try {
            UserManagementDetailResponse item = service.findDataById(id);
            return ResponseEntity.ok(new ApiResponse(true, "Successfully found user", item));
        } catch (BadRequestException e) {
            return ResponseEntity.badRequest().body(new ApiResponse(false, e.getMessage(), null));
        }
    }

    @PatchMapping("/{id}/restore")
    public ResponseEntity<ApiResponse> restore(@PathVariable("id") Long id) {
        log.info("PATCH " + urlRoute + "/{id}/restore endpoint hit");
        try {
            service.makeUserIsDeletedFalse(id);
            return ResponseEntity.ok(new ApiResponse(true, "Successfully restored user", null));
        } catch (BadRequestException e) {
            return ResponseEntity.badRequest().body(new ApiResponse(false, e.getMessage(), null));
        }
    }

    @PostMapping("/restore")
    public ResponseEntity<ApiResponse> restore(@RequestBody BulkByIdRequest dto) {
        log.info("POST " + urlRoute + "/restore endpoint hit");
        try {
            service.makeUserBulkRestoreTrue(dto.getIds());
            return ResponseEntity.ok(new ApiResponse(true, "Successfully restored user", null));
        } catch (BadRequestException e) {
            return ResponseEntity.badRequest().body(new ApiResponse(false, e.getMessage(), null));
        }
    }

    @GetMapping("/export")
    public void exportExcel(HttpServletResponse response) throws IOException {
        response.setContentType("application/octet-stream");
        String headerKey = "Content-Disposition";
        String headerValue = "attachment; filename=pre-register.xls";
        response.setHeader(headerKey, headerValue);
        exportService.exportExcelPreRegister(response);
    }

}