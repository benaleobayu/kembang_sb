package com.bca.byc.controller;


import com.bca.byc.exception.BadRequestException;
import com.bca.byc.model.BlacklistKeywordCreateUpdateRequest;
import com.bca.byc.model.BlacklistKeywordDetailResponse;
import com.bca.byc.response.ApiDataResponse;
import com.bca.byc.response.ApiResponse;
import com.bca.byc.response.PaginationCmsResponse;
import com.bca.byc.service.BlacklistKeywordService;
import com.bca.byc.service.MasterDataImportService;
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
@RequestMapping(BlacklistKeywordController.urlRoute)
@Tag(name = "Blacklist Keyword API")
@SecurityRequirement(name = "Authorization")
public class BlacklistKeywordController {

    static final String urlRoute = "/cms/v1/cm/blacklist-keyword";
    private BlacklistKeywordService service;

    private final MasterDataExportService exportService;
    private final MasterDataImportService importService;

    @PreAuthorize("hasAuthority('blacklist_keyword.view')")
    @Operation(summary = "Get list Blacklist Keyword and Export", description = "Get list Blacklist Keyword and Export")
    @GetMapping
    public ResponseEntity<?> listDataBlacklist(
            @RequestParam(name = "pages", required = false, defaultValue = "0") Integer pages,
            @RequestParam(name = "limit", required = false, defaultValue = "10") Integer limit,
            @RequestParam(name = "sortBy", required = false, defaultValue = "updatedAt") String sortBy,
            @RequestParam(name = "direction", required = false, defaultValue = "desc") String direction,
            @RequestParam(name = "keyword", required = false) String keyword,
            @RequestParam(name = "export", required = false) Boolean export,
            HttpServletResponse response
    ) {
        if (Boolean.TRUE.equals(export)) {
            // Export logic
            response.setContentType("application/octet-stream");
            String headerKey = "Content-Disposition";
            String headerValue = "attachment; filename=blacklist.xls";
            response.setHeader(headerKey, headerValue);
//            ExportFilterRequest filter = new ExportFilterRequest(startDate, endDate, status);
            try {
                exportService.exportBlacklistKeyword(response);
            } catch (IOException e) {
                log.error("Error exporting data", e);
                return ResponseEntity.internalServerError().body("Error exporting data");
            }
            return ResponseEntity.ok().build(); // Return an empty response as the file is handled in the export method
        } else {
            // List data logic
            log.info("GET " + urlRoute + " endpoint hit");
            return ResponseEntity.ok().body(new PaginationCmsResponse<>(true, "Success get list blacklist keyword", service.listDataBlacklist(pages, limit, sortBy, direction, keyword)));
        }
    }

    @PreAuthorize("hasAuthority('blacklist_keyword.read')")
    @Operation(summary = "Get detail Blacklist Keyword", description = "Get detail Blacklist Keyword")
    @GetMapping("{id}")
    public ResponseEntity<?> getById(@PathVariable("id") String id) {
        log.info("GET " + urlRoute + "/{id} endpoint hit");
        try {
            BlacklistKeywordDetailResponse item = service.findDataById(id);
            return ResponseEntity.ok(new ApiDataResponse<>(true, "Successfully found blacklist keyword", item));
        } catch (BadRequestException e) {
            return ResponseEntity.badRequest().body(new ApiResponse(false, e.getMessage()));
        }
    }

    @PreAuthorize("hasAuthority('blacklist_keyword.create')")
    @Operation(summary = "Create Blacklist Keyword", description = "Create Blacklist Keyword")
    @PostMapping
    public ResponseEntity<ApiResponse> create(@Valid @RequestBody BlacklistKeywordCreateUpdateRequest item) {
        log.info("POST " + urlRoute + " endpoint hit");
        try {
            service.saveData(item);
            return ResponseEntity.created(URI.create(urlRoute))
                    .body(new ApiResponse(true, "Successfully created blacklist keyword"));
        } catch (BadRequestException e) {
            return ResponseEntity.badRequest().body(new ApiResponse(false, e.getMessage()));
        }
    }

    @PreAuthorize("hasAuthority('blacklist_keyword.update')")
    @Operation(summary = "Update Blacklist Keyword", description = "Update Blacklist Keyword")
    @PutMapping("{id}")
    public ResponseEntity<ApiResponse> update(@PathVariable("id") String id, @Valid @RequestBody BlacklistKeywordCreateUpdateRequest item) {
        log.info("PUT " + urlRoute + "/{id} endpoint hit");
        try {
            service.updateData(id, item);
            return ResponseEntity.ok(new ApiResponse(true, "Successfully updated blacklist keyword"));
        } catch (BadRequestException e) {
            return ResponseEntity.badRequest().body(new ApiResponse(false, e.getMessage()));
        }
    }

    @PreAuthorize("hasAuthority('blacklist_keyword.delete')")
    @Operation(summary = "Delete Blacklist Keyword", description = "Delete Blacklist Keyword")
    @DeleteMapping("{id}")
    public ResponseEntity<ApiResponse> delete(@PathVariable("id") String id) {
        log.info("DELETE " + urlRoute + "/{id} endpoint hit");
        try {
            service.deleteData(id);
            return ResponseEntity.ok(new ApiResponse(true, "Successfully deleted blacklist keyword"));
        } catch (BadRequestException e) {
            return ResponseEntity.badRequest().body(new ApiResponse(false, e.getMessage()));
        }
    }


    @PreAuthorize("hasAuthority('blacklist_keyword.import')")
    @Operation(summary = "Import Blacklist Keyword", description = "Import Blacklist Keyword")
    @PostMapping(value = "/import", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> importData(@RequestPart(value = "file", required = false) MultipartFile file, @RequestParam(value = "isDownloadSample", required = false, defaultValue = "false" ) Boolean isDownloadSample) {
        if (Boolean.TRUE.equals(isDownloadSample)) {
            try {
                String filename = "sample-blacklist.xlsx";
                String urlFile = "http://localhost:8090/uploads/documents/a3910bd3-bd5b-4ae1-a60b-5f156787ef4b.xlsx";
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
                importService.importBlacklistKeyword(file);
                return ResponseEntity.ok(new ApiResponse(true, "Successfully imported blacklist keyword"));
            } catch (BadRequestException | IOException e) {
                throw new BadRequestException(e.getMessage());
            }
        }
    }

}