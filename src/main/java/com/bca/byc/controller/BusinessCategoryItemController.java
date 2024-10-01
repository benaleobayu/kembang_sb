package com.bca.byc.controller;


import com.bca.byc.exception.BadRequestException;
import com.bca.byc.model.BusinessCategoryItemCreateRequest;
import com.bca.byc.model.BusinessCategoryItemListResponse;
import com.bca.byc.model.BusinessCategoryUpdateRequest;
import com.bca.byc.response.ApiDataResponse;
import com.bca.byc.response.ApiResponse;
import com.bca.byc.response.PaginationCmsResponse;
import com.bca.byc.response.ResultPageResponseDTO;
import com.bca.byc.service.BusinessCategoryItemService;
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
@RequestMapping(BusinessCategoryItemController.urlRoute)
@Tag(name = "Business Category Item API [Masterdata]")
@SecurityRequirement(name = "Authorization")
public class BusinessCategoryItemController {

    static final String urlRoute = "/cms/v1/business-category";
    private BusinessCategoryItemService service;

    @PreAuthorize("hasAuthority('business_category_item.view')")
    @Operation(summary = "Get list Business Category item", description = "Get list Business Category", hidden = true)
    @GetMapping("/{parentId}/items")
    public ResponseEntity<PaginationCmsResponse<ResultPageResponseDTO<BusinessCategoryItemListResponse>>> listDataBusinessCategoryItem(
            @RequestParam(name = "pages", required = false, defaultValue = "0") Integer pages,
            @RequestParam(name = "limit", required = false, defaultValue = "10") Integer limit,
            @RequestParam(name = "sortBy", required = false, defaultValue = "id") String sortBy,
            @RequestParam(name = "direction", required = false, defaultValue = "desc") String direction,
            @RequestParam(name = "keyword", required = false) String keyword) {
        // response true
        log.info("GET " + urlRoute + " endpoint hit");
        return ResponseEntity.ok().body(new PaginationCmsResponse<>(true, "Success get list BusinessCategory", service.listDataBusinessCategoryItem(pages, limit, sortBy, direction, keyword)));
    }

    @PreAuthorize("hasAuthority('business_category_item.read')")
    @Operation(summary = "Get Business Category item", description = "Get Business Category", hidden = true)
    @GetMapping("/{parentId}/items/{id}")
    public ResponseEntity<?> getById(
            @PathVariable("parentId") String parentId,
            @PathVariable("id") String id) {
        log.info("GET " + urlRoute + "/{id} endpoint hit");
        try {
            BusinessCategoryItemListResponse item = service.findDataBySecureId(parentId,id);
            return ResponseEntity.ok(new ApiDataResponse(true, "Successfully found business category item", item));
        } catch (BadRequestException e) {
            return ResponseEntity.badRequest().body(new ApiResponse(false, e.getMessage()));
        }
    }

    @PreAuthorize("hasAuthority('business_category_item.create')")
    @Operation(summary = "Create new Business Category item", description = "Create new Business Category", hidden = true)
    @PostMapping("/{parentId}/items")
    public ResponseEntity<ApiResponse> create(
            @PathVariable("parentId") String parentid,
            @Valid @RequestBody BusinessCategoryItemCreateRequest item) {
        log.info("POST " + urlRoute + " endpoint hit");
        try {
            service.saveData(parentid, item);
            return ResponseEntity.created(URI.create(urlRoute))
                    .body(new ApiResponse(true, "Successfully created business category item"));
        } catch (BadRequestException e) {
            return ResponseEntity.badRequest().body(new ApiResponse(false, e.getMessage()));
        }
    }

    @PreAuthorize("hasAuthority('business_category_item.update')")
    @Operation(summary = "Update Business Category item", description = "Update Business Category", hidden = true)
    @PutMapping("/{parentId}/items/{id}")
    public ResponseEntity<ApiResponse> update(
            @PathVariable("parentId") String parentId,
            @PathVariable("id") String id,
            @Valid @RequestBody BusinessCategoryUpdateRequest item) {
        log.info("PUT " + urlRoute + "/{id} endpoint hit");
        try {
            service.updateData(parentId, id, item);
            return ResponseEntity.ok(new ApiResponse(true, "Successfully updated business category item"));
        } catch (BadRequestException e) {
            return ResponseEntity.badRequest().body(new ApiResponse(false, e.getMessage()));
        }
    }

    @PreAuthorize("hasAuthority('business_category_item.delete')")
    @Operation(summary = "Delete Business Category item", description = "Delete Business Category", hidden = true)
    @DeleteMapping("/{parentId}/items/{id}")
    public ResponseEntity<ApiResponse> delete(
            @PathVariable("parentId") String parentId,
            @PathVariable("id") String id) {
        log.info("DELETE " + urlRoute + "/{id} endpoint hit");
        try {
            service.deleteData(parentId, id);
            return ResponseEntity.ok(new ApiResponse(true, "Successfully deleted business category item"));
        } catch (BadRequestException e) {
            return ResponseEntity.badRequest().body(new ApiResponse(false, e.getMessage()));
        }
    }
}