package com.kembang.controller;

import com.kembang.exception.BadRequestException;
import com.kembang.model.CompilerFilterRequest;
import com.kembang.model.OrderRouteCreateUpdateRequest;
import com.kembang.model.OrderRouteDetailResponse;
import com.kembang.model.OrderRouteIndexResponse;
import com.kembang.response.ApiDataResponse;
import com.kembang.response.ApiResponse;
import com.kembang.response.PaginationCmsResponse;
import com.kembang.response.ResultPageResponseDTO;
import com.kembang.service.MasterDataImportService;
import com.kembang.service.OrderRouteService;
import com.kembang.service.cms.MasterDataExportService;
import com.kembang.util.helper.Formatter;
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
import java.time.LocalDate;

@Slf4j
@RestController
@AllArgsConstructor
@RequestMapping(OrderRouteController.urlRoute)
@Tag(name = "Order Route API")
@SecurityRequirement(name = "Authorization")
public class OrderRouteController {

    static final String urlRoute = "/cms/v1/order-route";
    private final MasterDataImportService importService;
    private final MasterDataExportService exportService;
    private final OrderRouteService service;

    @PreAuthorize("hasAuthority('order_route.view')")
    @Operation(summary = "Get list OrderRoute", description = "Get list OrderRoute")
    @GetMapping
    public ResponseEntity<?> listDataOrderRoute(
            @RequestParam(name = "pages", required = false, defaultValue = "0") Integer pages,
            @RequestParam(name = "limit", required = false, defaultValue = "10") Integer limit,
            @RequestParam(name = "sortBy", required = false, defaultValue = "updatedAt") String sortBy,
            @RequestParam(name = "direction", required = false, defaultValue = "asc") String direction,
            @RequestParam(name = "keyword", required = false) String keyword,
            @RequestParam(name = "date", required = false) String date,
            @RequestParam(name = "export", required = false) Boolean export,
            HttpServletResponse response
    ) {
        if (Boolean.TRUE.equals(export)) {
            // Export logic
            response.setContentType("application/octet-stream");
            String headerKey = "Content-Disposition";
            String headerValue = "attachment; filename=order_route.xls";
            response.setHeader(headerKey, headerValue);

            try {
                exportService.exportOrderRoute(response);
            } catch (IOException e) {
                log.error("Error exporting data", e);
                return ResponseEntity.internalServerError().body("Error exporting data");
            }
            return ResponseEntity.ok().build(); // Return an empty response as the file is handled in the export method
        } else {
            // response true
            log.info("GET " + urlRoute + " endpoint hit");
            LocalDate parseDate = Formatter.stringToLocalDate(date);
            CompilerFilterRequest f = new CompilerFilterRequest(pages, limit, sortBy, direction, keyword);
            ResultPageResponseDTO<OrderRouteIndexResponse> data = service.listDataOrderRoute(f, parseDate);
            return ResponseEntity.ok().body(new PaginationCmsResponse<>(true, "Success get list order-route", data));
        }
    }

    @PreAuthorize("hasAuthority('order_route.read')")
    @Operation(summary = "Get detail OrderRoute", description = "Get detail OrderRoute")
    @GetMapping("{id}")
    public ResponseEntity<?> getById(@PathVariable("id") String id) {
        log.info("GET " + urlRoute + "/{id} endpoint hit");
        try {
            OrderRouteDetailResponse item = service.findDataById(id);
            return ResponseEntity.ok(new ApiDataResponse<>(true, "Successfully found order-route", item));
        } catch (BadRequestException e) {
            return ResponseEntity.badRequest().body(new ApiResponse(false, e.getMessage()));
        }
    }

    @PreAuthorize("hasAuthority('order_route.create')")
    @Operation(summary = "Create OrderRoute", description = "Create OrderRoute")
    @PostMapping
    public ResponseEntity<ApiResponse> create(@Valid @RequestBody OrderRouteCreateUpdateRequest item) {
        log.info("POST " + urlRoute + " endpoint hit");
        try {
            service.saveData(item);
            return ResponseEntity.created(URI.create(urlRoute))
                    .body(new ApiResponse(true, "Successfully created order-route"));
        } catch (BadRequestException e) {
            return ResponseEntity.badRequest().body(new ApiResponse(false, e.getMessage()));
        }
    }

    @PreAuthorize("hasAuthority('order_route.update')")
    @Operation(summary = "Update OrderRoute", description = "Update OrderRoute")
    @PutMapping("{id}")
    public ResponseEntity<ApiResponse> update(@PathVariable("id") String id, @Valid @RequestBody OrderRouteCreateUpdateRequest item) {
        log.info("PUT " + urlRoute + "/{id} endpoint hit");
        try {
            service.updateData(id, item);
            return ResponseEntity.ok(new ApiResponse(true, "Successfully updated order-route"));
        } catch (BadRequestException e) {
            return ResponseEntity.badRequest().body(new ApiResponse(false, e.getMessage()));
        }
    }

    @PreAuthorize("hasAuthority('order_route.delete')")
    @Operation(summary = "Delete OrderRoute", description = "Delete OrderRoute")
    @DeleteMapping("{id}")
    public ResponseEntity<ApiResponse> delete(@PathVariable("id") String id) {
        log.info("DELETE " + urlRoute + "/{id} endpoint hit");
        try {
            service.deleteData(id);
            return ResponseEntity.ok(new ApiResponse(true, "Successfully deleted order-route"));
        } catch (BadRequestException e) {
            return ResponseEntity.badRequest().body(new ApiResponse(false, e.getMessage()));
        }
    }

}
