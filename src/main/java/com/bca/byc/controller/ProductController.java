package com.bca.byc.controller;

import com.bca.byc.exception.BadRequestException;
import com.bca.byc.model.CompilerFilterRequest;
import com.bca.byc.model.ProductDetailResponse;
import com.bca.byc.model.ProductIndexResponse;
import com.bca.byc.response.ApiDataResponse;
import com.bca.byc.response.ApiResponse;
import com.bca.byc.response.PaginationCmsResponse;
import com.bca.byc.response.ResultPageResponseDTO;
import com.bca.byc.service.ProductService;
import com.bca.byc.service.cms.MasterDataExportService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;
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
@RequestMapping(ProductController.urlRoute)
@Tag(name = "Product API")
@SecurityRequirement(name = "Authorization")
public class ProductController {

    static final String urlRoute = "/cms/v1/ms/product";

    private final MasterDataExportService exportService;
    private final ProductService service;

    @PreAuthorize("hasAuthority('product.view')")
    @Operation(summary = "Get list product", description = "Get list product")
    @GetMapping
    public ResponseEntity<?> listProduct(
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
            String headerValue = "attachment; filename=product.xls";
            response.setHeader(headerKey, headerValue);

            try {
                exportService.exportProduct(response);
            } catch (IOException e) {
                log.error("Error exporting data", e);
                return ResponseEntity.internalServerError().body("Error exporting data");
            }
            return ResponseEntity.ok().build(); // Return an empty response as the file is handled in the export method
        } else {
            // response true
            CompilerFilterRequest f = new CompilerFilterRequest(pages, limit, sortBy, direction, keyword);
            ResultPageResponseDTO<ProductIndexResponse> data = service.listDataProduct(f);
            return ResponseEntity.ok().body(new PaginationCmsResponse<>(true, "Success get list product", data));
        }
    }

    @PreAuthorize("hasAuthority('product.read')")
    @Operation(summary = "Get detail product", description = "Get detail product")
    @GetMapping("{id}")
    public ResponseEntity<?> getById(@PathVariable("id") String id) {
        log.info("GET " + urlRoute + "/{id} endpoint hit");
        try {
            ProductDetailResponse item = service.findDataBySecureId(id);
            return ResponseEntity.ok(new ApiDataResponse<>(true, "Successfully found product", item));
        } catch (BadRequestException e) {
            return ResponseEntity.badRequest().body(new ApiResponse(false, e.getMessage()));
        }
    }

    @PreAuthorize("hasAuthority('product.create')")
    @Operation(summary = "Create new product", description = "Create new product")
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ApiResponse> create(
            @RequestPart("file") MultipartFile file,
            @RequestPart("name") String name,
            @RequestPart(value = "description", required = false) String description,
            @RequestPart("categoryId") String categoryId,
            @RequestPart(value = "price", required = false) Integer price,
            @RequestPart("isActive") Boolean isActive
    ) {
        log.info("POST " + urlRoute + " endpoint hit");
        try {
            service.saveData(file, name, description, categoryId, price, isActive);
            return ResponseEntity.created(URI.create(urlRoute))
                    .body(new ApiResponse(true, "Successfully created product"));
        } catch (BadRequestException e) {
            throw new BadRequestException(e.getMessage());
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    @PreAuthorize("hasAuthority('product.update')")
    @Operation(summary = "Update product by id", description = "Update product by id")
    @PutMapping(value = "{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ApiResponse> update(
            @PathVariable("id") String id,
            @RequestPart("file") MultipartFile file,
            @RequestPart("name") String name,
            @RequestPart(value = "description", required = false) String description,
            @RequestPart("categoryId") String categoryId,
            @RequestPart(value = "price", required = false) Integer price,
            @RequestPart("isActive") Boolean isActive
    ) {
        log.info("PUT " + urlRoute + "/{id} endpoint hit");
        try {
            service.updateData(id, file, name, description, categoryId, price, isActive);
            return ResponseEntity.ok(new ApiResponse(true, "Successfully updated product"));
        } catch (BadRequestException e) {
            throw new BadRequestException(e.getMessage());
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    @PreAuthorize("hasAuthority('product.delete')")
    @Operation(summary = "Delete product by id", description = "Delete product by id")
    @DeleteMapping("{id}")
    public ResponseEntity<ApiResponse> delete(@PathVariable("id") String id) {
        log.info("DELETE " + urlRoute + "/{id} endpoint hit");
        try {
            service.deleteData(id);
            return ResponseEntity.ok(new ApiResponse(true, "Successfully deleted product"));
        } catch (BadRequestException e) {
            return ResponseEntity.badRequest().body(new ApiResponse(false, e.getMessage()));
        }
    }

}
