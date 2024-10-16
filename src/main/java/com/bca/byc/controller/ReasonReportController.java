package com.bca.byc.controller;

import com.bca.byc.exception.BadRequestException;
import com.bca.byc.model.ReasonReportCreateUpdateRequest;
import com.bca.byc.model.ReasonReportDetailResponse;
import com.bca.byc.model.ReasonReportIndexResponse;
import com.bca.byc.response.ApiDataResponse;
import com.bca.byc.response.ApiResponse;
import com.bca.byc.response.PaginationCmsResponse;
import com.bca.byc.response.ResultPageResponseDTO;
import com.bca.byc.service.ReasonReportService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.URI;

@Slf4j
@RestController
@AllArgsConstructor
@RequestMapping(ReasonReportController.urlRoute)
@Tag(name = "Reason Report API [Masterdata]")
@SecurityRequirement(name = "Authorization")
public class ReasonReportController {

    static final String urlRoute = "/cms/v1/ms/reason-report";
    private ReasonReportService service;

    @Operation(summary = "Get list reason report", description = "Get list reason report")
    @GetMapping
    public ResponseEntity<PaginationCmsResponse<ResultPageResponseDTO<ReasonReportIndexResponse>>> listDataReasonReportIndex(
            @RequestParam(name = "pages", required = false, defaultValue = "0") Integer pages,
            @RequestParam(name = "limit", required = false, defaultValue = "10") Integer limit,
            @RequestParam(name = "sortBy", required = false, defaultValue = "name") String sortBy,
            @RequestParam(name = "direction", required = false, defaultValue = "asc") String direction,
            @RequestParam(name = "keyword", required = false) String keyword) {
        // response true
        log.info("GET " + urlRoute + " endpoint hit");
        return ResponseEntity.ok().body(new PaginationCmsResponse<>(true, "Success get list reason report", service.listDataReasonReportIndex(pages, limit, sortBy, direction, keyword)));
    }

    @Operation(summary = "Get detail reason report", description = "Get detail reason report")
    @GetMapping("{id}")
    public ResponseEntity<?> getById(@PathVariable("id") String id) {
        log.info("GET " + urlRoute + "/{id} endpoint hit");
        try {
            ReasonReportDetailResponse item = service.findDataById(id);
            return ResponseEntity.ok(new ApiDataResponse<>(true, "Successfully found reason report", item));
        } catch (BadRequestException e) {
            return ResponseEntity.badRequest().body(new ApiResponse(false, e.getMessage()));
        }
    }

    @Operation(summary = "Create new reason report", description = "Create new reason report")
    @PostMapping(consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResponseEntity<ApiResponse> create(
            @ModelAttribute(value = "icon") MultipartFile icon,
            @ModelAttribute(value = "name") String name,
            @ModelAttribute(value = "orders") Integer orders,
            @ModelAttribute(value = "status") Boolean status
    ) {
        log.info("POST " + urlRoute + " endpoint hit");
        try {
            ReasonReportCreateUpdateRequest item = new ReasonReportCreateUpdateRequest(icon, name, orders, status);
            service.saveData(item);
            return ResponseEntity.created(URI.create(urlRoute))
                    .body(new ApiResponse(true, "Successfully created reason report"));
        } catch (BadRequestException | IOException e ) {
            return ResponseEntity.badRequest().body(new ApiResponse(false, e.getMessage()));
        }
    }

    @Operation(summary = "Update reason report", description = "Update reason report")
    @PutMapping(value = "{id}", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResponseEntity<ApiResponse> update(
            @PathVariable("id") String id,
            @ModelAttribute(value = "icon") MultipartFile icon,
            @ModelAttribute(value = "name") String name,
            @ModelAttribute(value = "orders") Integer orders,
            @ModelAttribute(value = "status") Boolean status
    ) {
        log.info("PUT " + urlRoute + "/{id} endpoint hit");
        try {
            ReasonReportCreateUpdateRequest item = new ReasonReportCreateUpdateRequest(icon, name, orders, status);
            service.updateData(id, item);
            return ResponseEntity.ok(new ApiResponse(true, "Successfully updated reason report"));
        } catch (BadRequestException | IOException e) {
            return ResponseEntity.badRequest().body(new ApiResponse(false, e.getMessage()));
        }
    }

    @Operation(summary = "Delete reason report", description = "Delete reason report")
    @DeleteMapping("{id}")
    public ResponseEntity<ApiResponse> delete(@PathVariable("id") String id) {
        log.info("DELETE " + urlRoute + "/{id} endpoint hit");
        try {
            service.deleteData(id);
            return ResponseEntity.ok(new ApiResponse(true, "Successfully deleted reason report"));
        } catch (BadRequestException e) {
            return ResponseEntity.badRequest().body(new ApiResponse(false, e.getMessage()));
        }
    }
}