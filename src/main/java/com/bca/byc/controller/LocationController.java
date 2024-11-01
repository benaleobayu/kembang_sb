package com.bca.byc.controller;


import com.bca.byc.exception.BadRequestException;
import com.bca.byc.model.LocationCreateUpdateRequest;
import com.bca.byc.model.LocationDetailResponse;
import com.bca.byc.model.LocationIndexResponse;
import com.bca.byc.model.export.ExportFilterRequest;
import com.bca.byc.response.ApiDataResponse;
import com.bca.byc.response.ApiResponse;
import com.bca.byc.response.PaginationCmsResponse;
import com.bca.byc.response.ResultPageResponseDTO;
import com.bca.byc.service.MasterDataImportService;
import com.bca.byc.service.cms.LocationService;
import com.bca.byc.service.cms.MasterDataExportService;
import com.bca.byc.util.FileUploadHelper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.URI;

@Slf4j
@RestController
@AllArgsConstructor
@RequestMapping(LocationController.urlRoute)
@Tag(name = "Location API")
@SecurityRequirement(name = "Authorization")
public class LocationController {

    static final String urlRoute = "/cms/v1/ms/location";
    private final MasterDataImportService importService;
    private final MasterDataExportService exportService;
    private LocationService service;

    @PreAuthorize("hasAuthority('location.view')")
    @Operation(summary = "Get list Location", description = "Get list Location")
    @GetMapping
    public ResponseEntity<?> listDataLocation(
            @RequestParam(name = "pages", required = false, defaultValue = "0") Integer pages,
            @RequestParam(name = "limit", required = false, defaultValue = "10") Integer limit,
            @RequestParam(name = "sortBy", required = false, defaultValue = "updatedAt") String sortBy,
            @RequestParam(name = "direction", required = false, defaultValue = "asc") String direction,
            @RequestParam(name = "keyword", required = false) String keyword,
            @RequestParam(name = "export", required = false) Boolean export,
            HttpServletResponse response
    ) {
        if (Boolean.TRUE.equals(export)) {
            // Export logic
            response.setContentType("application/octet-stream");
            String headerKey = "Content-Disposition";
            String headerValue = "attachment; filename=pre-register.xls";
            response.setHeader(headerKey, headerValue);

            try {
                exportService.exportLocation(response);
            } catch (IOException e) {
                log.error("Error exporting data", e);
                return ResponseEntity.internalServerError().body("Error exporting data");
            }
            return ResponseEntity.ok().build(); // Return an empty response as the file is handled in the export method
        } else {
            // response true
            log.info("GET " + urlRoute + " endpoint hit");
            return ResponseEntity.ok().body(new PaginationCmsResponse<>(true, "Success get list location", service.listDataLocation(pages, limit, sortBy, direction, keyword)));
        }
    }

    @PreAuthorize("hasAuthority('location.read')")
    @Operation(summary = "Get detail Location", description = "Get detail Location")
    @GetMapping("{id}")
    public ResponseEntity<?> getById(@PathVariable("id") String id) {
        log.info("GET " + urlRoute + "/{id} endpoint hit");
        try {
            LocationDetailResponse item = service.findDataById(id);
            return ResponseEntity.ok(new ApiDataResponse<>(true, "Successfully found location", item));
        } catch (BadRequestException e) {
            return ResponseEntity.badRequest().body(new ApiResponse(false, e.getMessage()));
        }
    }

    @PreAuthorize("hasAuthority('location.create')")
    @Operation(summary = "Create Location", description = "Create Location")
    @PostMapping
    public ResponseEntity<ApiResponse> create(@Valid @RequestBody LocationCreateUpdateRequest item) {
        log.info("POST " + urlRoute + " endpoint hit");
        try {
            service.saveData(item);
            return ResponseEntity.created(URI.create(urlRoute))
                    .body(new ApiResponse(true, "Successfully created location"));
        } catch (BadRequestException e) {
            return ResponseEntity.badRequest().body(new ApiResponse(false, e.getMessage()));
        }
    }

    @PreAuthorize("hasAuthority('location.update')")
    @Operation(summary = "Update Location", description = "Update Location")
    @PutMapping("{id}")
    public ResponseEntity<ApiResponse> update(@PathVariable("id") String id, @Valid @RequestBody LocationCreateUpdateRequest item) {
        log.info("PUT " + urlRoute + "/{id} endpoint hit");
        try {
            service.updateData(id, item);
            return ResponseEntity.ok(new ApiResponse(true, "Successfully updated location"));
        } catch (BadRequestException e) {
            return ResponseEntity.badRequest().body(new ApiResponse(false, e.getMessage()));
        }
    }

    @PreAuthorize("hasAuthority('location.delete')")
    @Operation(summary = "Delete Location", description = "Delete Location")
    @DeleteMapping("{id}")
    public ResponseEntity<ApiResponse> delete(@PathVariable("id") String id) {
        log.info("DELETE " + urlRoute + "/{id} endpoint hit");
        try {
            service.deleteData(id);
            return ResponseEntity.ok(new ApiResponse(true, "Successfully deleted location"));
        } catch (BadRequestException e) {
            return ResponseEntity.badRequest().body(new ApiResponse(false, e.getMessage()));
        }
    }

    @PreAuthorize("hasAuthority('location.import')")
    @Operation(summary = "Import Location", description = "Import Location")
    @PostMapping(value = "/import", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> importData(@RequestPart(value = "file", required = false) MultipartFile file, @RequestParam(value = "isDownloadSample", required = false, defaultValue = "false" ) Boolean isDownloadSample) {
        if (Boolean.TRUE.equals(isDownloadSample)) {
            try {
                String filename = "sample-location.xlsx";
                String urlFile = "http://localhost:8090/uploads/documents/02e852da-11a0-42e9-afe7-8a0105bd0594.xlsx";
                FileUploadHelper.downloadFileFromUrl(urlFile, filename);
                return ResponseEntity.ok(new ApiDataResponse<>(true, "Successfully get link download", urlFile));
            } catch (IOException e) {
                throw new BadRequestException(e.getMessage());
            }
        } else {
            if (file.isEmpty() || file == null) {
                return ResponseEntity.badRequest().body(new ApiResponse(false, "File is empty"));
            }
            try {
                importService.importLocation(file);
                return ResponseEntity.ok(new ApiResponse(true, "Successfully imported location"));
            } catch (BadRequestException | IOException e) {
                throw new BadRequestException(e.getMessage());
            }
        }

    }
}