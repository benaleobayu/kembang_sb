package com.kembang.controller;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.kembang.exception.BadRequestException;
import com.kembang.model.CompilerFilterRequest;
import com.kembang.model.OrderCreateUpdateRequest;
import com.kembang.model.OrderDetailResponse;
import com.kembang.model.OrderIndexResponse;
import com.kembang.response.ApiDataResponse;
import com.kembang.response.ApiResponse;
import com.kembang.response.PaginationCmsResponse;
import com.kembang.response.ResultPageResponseDTO;
import com.kembang.service.OrderService;
import com.kembang.service.cms.MasterDataExportService;
import com.kembang.util.helper.Formatter;
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
import java.time.LocalDate;

@Slf4j
@RestController
@AllArgsConstructor
@RequestMapping(OrderController.urlRoute)
@Tag(name = "Order API")
@SecurityRequirement(name = "Authorization")
public class OrderController {

    static final String urlRoute = "/cms/v1/order";

    private final MasterDataExportService exportService;
    private final OrderService service;

    @PreAuthorize("hasAuthority('order.view')")
    @Operation(summary = "Get list order", description = "Get list order")
    @GetMapping
    public ResponseEntity<?> listOrder(
            @RequestParam(name = "pages", required = false, defaultValue = "0") Integer pages,
            @RequestParam(name = "limit", required = false, defaultValue = "10") Integer limit,
            @RequestParam(name = "sortBy", required = false, defaultValue = "id") String sortBy,
            @RequestParam(name = "direction", required = false, defaultValue = "desc") String direction,
            @RequestParam(name = "keyword", required = false) String keyword,
            @RequestParam(name = "date", required = false) String date,
            @RequestParam(name = "location", required = false) String location,
            @RequestParam(name = "route", required = false) Integer route,
            @RequestParam(name = "export", required = false) Boolean export,
            HttpServletResponse response
    ) {
        log.info("GET " + urlRoute + " endpoint hit");

        if (Boolean.TRUE.equals(export)) {
            // Export logic
            response.setContentType("application/octet-stream");
            String headerKey = "Content-Disposition";
            String headerValue = "attachment; filename=order.xls";
            response.setHeader(headerKey, headerValue);

            try {
                exportService.exportOrder(response);
            } catch (IOException e) {
                log.error("Error exporting data", e);
                return ResponseEntity.internalServerError().body("Error exporting data");
            }
            return ResponseEntity.ok().build(); // Return an empty response as the file is handled in the export method
        } else {
            // response true
            LocalDate parseDate = Formatter.stringToLocalDate(date);
            CompilerFilterRequest f = new CompilerFilterRequest(pages, limit, sortBy, direction, keyword);
            ResultPageResponseDTO<OrderIndexResponse> data = service.listDataOrder(f, parseDate, location, route);
            return ResponseEntity.ok().body(new PaginationCmsResponse<>(true, "Success get list order", data));
        }
    }

    @PreAuthorize("hasAuthority('order.read')")
    @Operation(summary = "Get detail order", description = "Get detail order")
    @GetMapping("{id}")
    public ResponseEntity<?> getById(@PathVariable("id") String id) {
        log.info("GET " + urlRoute + "/{id} endpoint hit");
        try {
            OrderDetailResponse item = service.findDataBySecureId(id);
            return ResponseEntity.ok(new ApiDataResponse<>(true, "Successfully found order", item));
        } catch (BadRequestException e) {
            return ResponseEntity.badRequest().body(new ApiResponse(false, e.getMessage()));
        }
    }

    @PreAuthorize("hasAuthority('order.create')")
    @Operation(summary = "Create new order", description = "Create new order")
    @PostMapping
    public ResponseEntity<ApiResponse> create(@Valid @RequestBody OrderCreateUpdateRequest dto) {
        log.info("POST " + urlRoute + " endpoint hit");
        try {
            service.saveData(dto);
            return ResponseEntity.created(URI.create(urlRoute))
                    .body(new ApiResponse(true, "Successfully created order"));
        } catch (BadRequestException e) {
            throw new BadRequestException(e.getMessage());
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    @PreAuthorize("hasAuthority('order.update')")
    @Operation(summary = "Update order by id", description = "Update order by id")
    @PutMapping(value = "{id}")
    public ResponseEntity<ApiResponse> update(
            @PathVariable("id") String id,
            @RequestBody OrderCreateUpdateRequest dto,
            @RequestParam(value = "isRoute", required = false, defaultValue = "false") Boolean isRoute
    ) {
        log.info("PUT " + urlRoute + "/{id} endpoint hit");
        try {
            service.updateData(id, dto, isRoute);
            return ResponseEntity.ok(new ApiResponse(true, "Successfully updated order"));
        } catch (BadRequestException e) {
            throw new BadRequestException(e.getMessage());
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    @PreAuthorize("hasAuthority('order.delete')")
    @Operation(summary = "Delete order by id", description = "Delete order by id")
    @DeleteMapping("{id}")
    public ResponseEntity<ApiResponse> delete(@PathVariable("id") String id) {
        log.info("DELETE " + urlRoute + "/{id} endpoint hit");
        try {
            service.deleteData(id);
            return ResponseEntity.ok(new ApiResponse(true, "Successfully deleted order"));
        } catch (BadRequestException e) {
            return ResponseEntity.badRequest().body(new ApiResponse(false, e.getMessage()));
        }
    }

}
