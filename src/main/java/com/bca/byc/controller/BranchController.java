package com.bca.byc.controller;


import com.bca.byc.exception.BadRequestException;
import com.bca.byc.model.BranchCreateUpdateRequest;
import com.bca.byc.model.BranchDetailResponse;
import com.bca.byc.response.ApiDataResponse;
import com.bca.byc.response.ApiResponse;
import com.bca.byc.response.PaginationCmsResponse;
import com.bca.byc.response.ResultPageResponseDTO;
import com.bca.byc.service.cms.BranchService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

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

    @PreAuthorize("hasAuthority('branch.view')")
    @Operation(summary = "Get list branch", description = "Get list branch")
    @GetMapping
    public ResponseEntity<PaginationCmsResponse<ResultPageResponseDTO<BranchDetailResponse>>> listBranch(
            @RequestParam(name = "pages", required = false, defaultValue = "0") Integer pages,
            @RequestParam(name = "limit", required = false, defaultValue = "10") Integer limit,
            @RequestParam(name = "sortBy", required = false, defaultValue = "id") String sortBy,
            @RequestParam(name = "direction", required = false, defaultValue = "desc") String direction,
            @RequestParam(name = "keyword", required = false) String keyword,
            @RequestParam(name = "export", required = false) Boolean export // TODO export branch
    ) {
        // response true
        log.info("GET " + urlRoute + " endpoint hit");
        return ResponseEntity.ok().body(new PaginationCmsResponse<>(true, "Success get list branch", service.listDataBranch(pages, limit, sortBy, direction, keyword)));
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
}
