package com.kembang.controller;

import com.kembang.exception.BadRequestException;
import com.kembang.model.DocumentsDetailResponse;
import com.kembang.model.DocumentsIndexResponse;
import com.kembang.response.ApiDataResponse;
import com.kembang.response.ApiResponse;
import com.kembang.response.PaginationCmsResponse;
import com.kembang.response.ResultPageResponseDTO;
import com.kembang.service.DocumentsService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@RequestMapping(DocumentsController.urlRoute)
@Tag(name = "Documents API")
@SecurityRequirement(name = "Authorization")
public class DocumentsController {

    static final String urlRoute = "/cms/v1/ms/documents";
    private DocumentsService service;

    @PreAuthorize("hasAuthority('document.view')")
    @Operation(summary = "Get list document", description = "Get list document")
    @GetMapping
    public ResponseEntity<PaginationCmsResponse<ResultPageResponseDTO<DocumentsIndexResponse>>> listIndex(
            @RequestParam(name = "pages", required = false, defaultValue = "0") Integer pages,
            @RequestParam(name = "limit", required = false, defaultValue = "10") Integer limit,
            @RequestParam(name = "sortBy", required = false, defaultValue = "id") String sortBy,
            @RequestParam(name = "direction", required = false, defaultValue = "desc") String direction,
            @RequestParam(name = "keyword", required = false) String keyword,
            @RequestParam(name = "export", required = false) Boolean export // TODO export document
    ) {
        // response true
        log.info("GET " + urlRoute + " endpoint hit");
        ResultPageResponseDTO<DocumentsIndexResponse> data = service.listIndex(pages, limit, sortBy, direction, keyword);
        return ResponseEntity.ok().body(new PaginationCmsResponse<>(true, "Success get list document", data));
    }

    @PreAuthorize("hasAuthority('document.read')")
    @Operation(summary = "Get detail document", description = "Get detail document")
    @GetMapping("{id}")
    public ResponseEntity<?> getById(@PathVariable("id") String id) {
        log.info("GET " + urlRoute + "/{id} endpoint hit");
        try {
            DocumentsDetailResponse item = service.findDataBySecureId(id);
            return ResponseEntity.ok(new ApiDataResponse<>(true, "Successfully found document", item));
        } catch (BadRequestException e) {
            return ResponseEntity.badRequest().body(new ApiResponse(false, e.getMessage()));
        }
    }

    @PreAuthorize("hasAuthority('document.create')")
    @Operation(summary = "Create new document", description = "Create new document")
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ApiResponse> create(
            @RequestPart(name = "file", required = false) MultipartFile file,
            @RequestParam(name = "name", required = false) String name,
            @RequestParam(name = "identity", required = false) String identity,
            @RequestParam(name = "folder", required = false, defaultValue = "documents") String folder
    ) {
        log.info("POST " + urlRoute + " endpoint hit");
        try {
            service.saveData(file, name, identity, folder);
            return ResponseEntity.created(URI.create(urlRoute))
                    .body(new ApiResponse(true, "Successfully created document"));
        } catch (BadRequestException | IOException e) {
            return ResponseEntity.badRequest().body(new ApiResponse(false, e.getMessage()));
        }
    }

    @PreAuthorize("hasAuthority('document.update')")
    @Operation(summary = "Update document by id", description = "Update document by id")
    @PutMapping("{id}")
    public ResponseEntity<ApiResponse> update(
            @PathVariable("id") String id,
            @RequestPart(name = "file", required = false) MultipartFile file,
            @RequestParam(name = "name", required = false) String name,
            @RequestParam(name = "identity", required = false) String identity,
            @RequestParam(name = "folder", required = false, defaultValue = "documents") String folder
    ) {
        log.info("PUT " + urlRoute + "/{id} endpoint hit");
        try {
            service.updateData(id, file, name, identity, folder);
            return ResponseEntity.ok(new ApiResponse(true, "Successfully updated document"));
        } catch (BadRequestException | IOException e) {
            return ResponseEntity.badRequest().body(new ApiResponse(false, e.getMessage()));
        }
    }

    @PreAuthorize("hasAuthority('document.delete')")
    @Operation(summary = "Delete document by id", description = "Delete document by id")
    @DeleteMapping("{id}")
    public ResponseEntity<ApiResponse> delete(@PathVariable("id") String id) {
        log.info("DELETE " + urlRoute + "/{id} endpoint hit");
        try {
            service.deleteData(id);
            return ResponseEntity.ok(new ApiResponse(true, "Successfully deleted document"));
        } catch (BadRequestException e) {
            return ResponseEntity.badRequest().body(new ApiResponse(false, e.getMessage()));
        }
    }
}
