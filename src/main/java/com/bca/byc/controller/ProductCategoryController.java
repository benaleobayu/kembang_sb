package com.bca.byc.controller;

import com.bca.byc.exception.BadRequestException;
import com.bca.byc.model.CompilerFilterRequest;
import com.bca.byc.model.SimpleCmsResponse;
import com.bca.byc.model.SimpleCreateUpdateRequest;
import com.bca.byc.response.ApiDataResponse;
import com.bca.byc.response.ApiResponse;
import com.bca.byc.response.PaginationCmsResponse;
import com.bca.byc.response.ResultPageResponseDTO;
import com.bca.byc.service.ProductCategoryService;
import com.bca.byc.service.cms.MasterDataExportService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.net.URI;

@Slf4j
@RestController
@AllArgsConstructor
@RequestMapping(ProductCategoryController.urlRoute)
@Tag(name = "Product Category API")
@SecurityRequirement(name = "Authorization")
public class ProductCategoryController {

    static final String urlRoute = "/cms/v1/ms/product_category";

    private final MasterDataExportService exportService;
    private final ProductCategoryService service;

    @PreAuthorize("hasAuthority('product_category.view')")
    @Operation(summary = "Get list product_category", description = "Get list product_category")
    @GetMapping
    public ResponseEntity<?> listProductCategory(
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
            String headerValue = "attachment; filename=product_category.xls";
            response.setHeader(headerKey, headerValue);

            try {
                exportService.exportProductCategory(response);
            } catch (IOException e) {
                log.error("Error exporting data", e);
                return ResponseEntity.internalServerError().body("Error exporting data");
            }
            return ResponseEntity.ok().build(); // Return an empty response as the file is handled in the export method
        } else {
            // response true
            CompilerFilterRequest f = new CompilerFilterRequest(pages, limit, sortBy, direction, keyword);
            ResultPageResponseDTO<SimpleCmsResponse> data = service.listDataProductCategory(f);
            return ResponseEntity.ok().body(new PaginationCmsResponse<>(true, "Success get list product_category", data));
        }
    }

    @PreAuthorize("hasAuthority('product_category.read')")
    @Operation(summary = "Get detail product_category", description = "Get detail product_category")
    @GetMapping("{id}")
    public ResponseEntity<?> getById(@PathVariable("id") String id) {
        log.info("GET " + urlRoute + "/{id} endpoint hit");
        try {
            SimpleCmsResponse item = service.findDataBySecureId(id);
            return ResponseEntity.ok(new ApiDataResponse<>(true, "Successfully found product_category", item));
        } catch (BadRequestException e) {
            return ResponseEntity.badRequest().body(new ApiResponse(false, e.getMessage()));
        }
    }

    @PreAuthorize("hasAuthority('product_category.create')")
    @Operation(summary = "Create new product_category", description = "Create new product_category")
    @PostMapping
    public ResponseEntity<ApiResponse> create(@Valid @RequestBody SimpleCreateUpdateRequest dto) {
        log.info("POST " + urlRoute + " endpoint hit");
        try {
            service.saveData(dto);
            return ResponseEntity.created(URI.create(urlRoute))
                    .body(new ApiResponse(true, "Successfully created product_category"));
        } catch (BadRequestException e) {
            return ResponseEntity.badRequest().body(new ApiResponse(false, e.getMessage()));
        }
    }

    @PreAuthorize("hasAuthority('product_category.update')")
    @Operation(summary = "Update product_category by id", description = "Update product_category by id")
    @PutMapping("{id}")
    public ResponseEntity<ApiResponse> update(@PathVariable("id") String id, @Valid @RequestBody SimpleCreateUpdateRequest dto) {
        log.info("PUT " + urlRoute + "/{id} endpoint hit");
        try {
            service.updateData(id, dto);
            return ResponseEntity.ok(new ApiResponse(true, "Successfully updated product_category"));
        } catch (BadRequestException e) {
            return ResponseEntity.badRequest().body(new ApiResponse(false, e.getMessage()));
        }
    }

    @PreAuthorize("hasAuthority('product_category.delete')")
    @Operation(summary = "Delete product_category by id", description = "Delete product_category by id")
    @DeleteMapping("{id}")
    public ResponseEntity<ApiResponse> delete(@PathVariable("id") String id) {
        log.info("DELETE " + urlRoute + "/{id} endpoint hit");
        try {
            service.deleteData(id);
            return ResponseEntity.ok(new ApiResponse(true, "Successfully deleted product_category"));
        } catch (BadRequestException e) {
            return ResponseEntity.badRequest().body(new ApiResponse(false, e.getMessage()));
        }
    }

}
