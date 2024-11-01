package com.bca.byc.controller;


import com.bca.byc.exception.BadRequestException;
import com.bca.byc.model.BranchCreateUpdateRequest;
import com.bca.byc.model.BranchDetailResponse;
import com.bca.byc.response.ApiDataResponse;
import com.bca.byc.response.ApiResponse;
import com.bca.byc.response.PaginationCmsResponse;
import com.bca.byc.response.ResultPageResponseDTO;
import com.bca.byc.service.MasterDataImportService;
import com.bca.byc.service.cms.BranchService;
import com.bca.byc.service.cms.MasterDataExportService;
import com.bca.byc.util.FileUploadHelper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
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
@RequestMapping(BranchController.urlRoute)
@Tag(name = "Branch API")
@SecurityRequirement(name = "Authorization")
public class BranchController {

    static final String urlRoute = "/cms/v1/ms/branch";
    private BranchService service;
    private final MasterDataImportService importService;
    private final MasterDataExportService exportService;

    @PreAuthorize("hasAuthority('branch.view')")
    @Operation(summary = "Get list branch", description = "Get list branch")
    @GetMapping
    public ResponseEntity<?> listBranch(
            @RequestParam(name = "pages", required = false, defaultValue = "0") Integer pages,
            @RequestParam(name = "limit", required = false, defaultValue = "10") Integer limit,
            @RequestParam(name = "sortBy", required = false, defaultValue = "id") String sortBy,
            @RequestParam(name = "direction", required = false, defaultValue = "desc") String direction,
            @RequestParam(name = "keyword", required = false) String keyword,
            @RequestParam(name = "export", required = false) Boolean export,
            HttpServletResponse response
    ) {
        log.info("GET " + urlRoute + " endpoint hit");

        if (Boolean.TRUE.equals(export)) {
            // Export logic
            response.setContentType("application/octet-stream");
            String headerKey = "Content-Disposition";
            String headerValue = "attachment; filename=branch.xls";
            response.setHeader(headerKey, headerValue);

            try {
                exportService.exportBranch(response);
            } catch (IOException e) {
                log.error("Error exporting data", e);
                return ResponseEntity.internalServerError().body("Error exporting data");
            }
            return ResponseEntity.ok().build(); // Return an empty response as the file is handled in the export method
        } else {
            // response true
            return ResponseEntity.ok().body(new PaginationCmsResponse<>(true, "Success get list branch", service.listDataBranch(pages, limit, sortBy, direction, keyword)));
        }
    }

    @PreAuthorize("hasAuthority('branch.read')")
    @Operation(summary = "Get detail branch", description = "Get detail branch")
    @GetMapping("{id}")
    public ResponseEntity<?> getById(@PathVariable("id") String id) {
        log.info("GET " + urlRoute + "/{id} endpoint hit");
        try {
            BranchDetailResponse item = service.findDataBySecureId(id);
            return ResponseEntity.ok(new ApiDataResponse<>(true, "Successfully found branch", item));
        } catch (BadRequestException e) {
            return ResponseEntity.badRequest().body(new ApiResponse(false, e.getMessage()));
        }
    }

    @PreAuthorize("hasAuthority('branch.create')")
    @Operation(summary = "Create new branch", description = "Create new branch")
    @PostMapping
    public ResponseEntity<ApiResponse> create(@Valid @RequestBody BranchCreateUpdateRequest item) {
        log.info("POST " + urlRoute + " endpoint hit");
        try {
            service.saveData(item);
            return ResponseEntity.created(URI.create(urlRoute))
                    .body(new ApiResponse(true, "Successfully created branch"));
        } catch (BadRequestException e) {
            return ResponseEntity.badRequest().body(new ApiResponse(false, e.getMessage()));
        }
    }

    @PreAuthorize("hasAuthority('branch.update')")
    @Operation(summary = "Update branch by id", description = "Update branch by id")
    @PutMapping("{id}")
    public ResponseEntity<ApiResponse> update(@PathVariable("id") String id, @Valid @RequestBody BranchCreateUpdateRequest item) {
        log.info("PUT " + urlRoute + "/{id} endpoint hit");
        try {
            service.updateData(id, item);
            return ResponseEntity.ok(new ApiResponse(true, "Successfully updated branch"));
        } catch (BadRequestException e) {
            return ResponseEntity.badRequest().body(new ApiResponse(false, e.getMessage()));
        }
    }

    @PreAuthorize("hasAuthority('branch.delete')")
    @Operation(summary = "Delete branch by id", description = "Delete branch by id")
    @DeleteMapping("{id}")
    public ResponseEntity<ApiResponse> delete(@PathVariable("id") String id) {
        log.info("DELETE " + urlRoute + "/{id} endpoint hit");
        try {
            service.deleteData(id);
            return ResponseEntity.ok(new ApiResponse(true, "Successfully deleted branch"));
        } catch (BadRequestException e) {
            return ResponseEntity.badRequest().body(new ApiResponse(false, e.getMessage()));
        }
    }

    @PreAuthorize("hasAuthority('branch.import')")
    @Operation(summary = "Import Branch Keyword", description = "Import Branch Keyword")
    @PostMapping(value = "/import", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> importData(@RequestPart(value = "file", required = false) MultipartFile file, @RequestParam(value = "isDownloadSample", required = false, defaultValue = "false" ) Boolean isDownloadSample) {
        if (Boolean.TRUE.equals(isDownloadSample)) {
            try {
                String filename = "sample-branch.xlsx";
                String urlFile = "http://localhost:8090/uploads/documents/c7fbb5e7-d6a1-4adb-a4f5-b8d392ba5f96.xlsx";
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
                importService.importBranch(file);
                return ResponseEntity.ok(new ApiResponse(true, "Successfully imported branch"));
            } catch (BadRequestException | IOException e) {
                throw new BadRequestException(e.getMessage());
            }
        }
    }
}
