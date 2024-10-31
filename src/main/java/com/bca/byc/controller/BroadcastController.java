package com.bca.byc.controller;

import com.bca.byc.exception.BadRequestException;
import com.bca.byc.model.BroadcastCreateUpdateRequest;
import com.bca.byc.model.BroadcastDetailResponse;
import com.bca.byc.model.BroadcastIndexResponse;
import com.bca.byc.response.ApiDataResponse;
import com.bca.byc.response.ApiResponse;
import com.bca.byc.response.PaginationCmsResponse;
import com.bca.byc.response.ResultPageResponseDTO;
import com.bca.byc.service.GlobalAttributeService;
import com.bca.byc.service.cms.BroadcastService;
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
import java.time.LocalDate;

@Slf4j
@RestController
@AllArgsConstructor
@RequestMapping(BroadcastController.urlRoute)
@Tag(name = "Broadcast API")
@SecurityRequirement(name = "Authorization")
public class BroadcastController {

    static final String urlRoute = "/cms/v1/broadcast";
    private BroadcastService service;
    private final GlobalAttributeService globalAttributeService;

    @PreAuthorize("hasAuthority('broadcast.view')")
    @Operation(summary = "Get list broadcast", description = "Get list broadcast")
    @GetMapping
    public ResponseEntity<PaginationCmsResponse<ResultPageResponseDTO<BroadcastIndexResponse>>> listBroadcast(
            @RequestParam(name = "pages", required = false, defaultValue = "0") Integer pages,
            @RequestParam(name = "limit", required = false, defaultValue = "10") Integer limit,
            @RequestParam(name = "sortBy", required = false, defaultValue = "id") String sortBy,
            @RequestParam(name = "direction", required = false, defaultValue = "desc") String direction,
            @RequestParam(name = "keyword", required = false) String keyword,
            @RequestParam(name = "status", required = false) String status,
            @RequestParam(name = "postAt", required = false) LocalDate postAt,
            @RequestParam(name = "export", required = false) Boolean export // TODO export broadcast
    ) {
        // response true
        log.info("GET " + urlRoute + " endpoint hit");
        ResultPageResponseDTO<BroadcastIndexResponse> result = service.listDataBroadcast(pages, limit, sortBy, direction, keyword, status, postAt);
        return ResponseEntity.ok().body(new PaginationCmsResponse<>(true, "Success get list broadcast", result, globalAttributeService.listAttributeBroadcast()));
    }

    @PreAuthorize("hasAuthority('broadcast.read')")
    @Operation(summary = "Get detail broadcast", description = "Get detail broadcast")
    @GetMapping("{id}")
    public ResponseEntity<?> getById(@PathVariable("id") String id) {
        log.info("GET " + urlRoute + "/{id} endpoint hit");
        try {
            BroadcastDetailResponse item = service.findDataBySecureId(id);
            return ResponseEntity.ok(new ApiDataResponse<>(true, "Successfully found broadcast", item));
        } catch (BadRequestException e) {
            return ResponseEntity.badRequest().body(new ApiResponse(false, e.getMessage()));
        }
    }

    @PreAuthorize("hasAuthority('broadcast.create')")
    @Operation(summary = "Create new broadcast", description = "Create new broadcast")
    @PostMapping
    public ResponseEntity<ApiResponse> create(@Valid @RequestBody BroadcastCreateUpdateRequest item) {
        log.info("POST " + urlRoute + " endpoint hit");
        try {
            service.saveData(item);
            return ResponseEntity.created(URI.create(urlRoute))
                    .body(new ApiResponse(true, "Successfully created broadcast"));
        } catch (BadRequestException e) {
            return ResponseEntity.badRequest().body(new ApiResponse(false, e.getMessage()));
        }
    }

    @PreAuthorize("hasAuthority('broadcast.update')")
    @Operation(summary = "Update broadcast by id", description = "Update broadcast by id")
    @PutMapping("{id}")
    public ResponseEntity<ApiResponse> update(@PathVariable("id") String id, @Valid @RequestBody BroadcastCreateUpdateRequest item) {
        log.info("PUT " + urlRoute + "/{id} endpoint hit");
        try {
            service.updateData(id, item);
            return ResponseEntity.ok(new ApiResponse(true, "Successfully updated broadcast"));
        } catch (BadRequestException e) {
            return ResponseEntity.badRequest().body(new ApiResponse(false, e.getMessage()));
        }
    }

    @PreAuthorize("hasAuthority('broadcast.delete')")
    @Operation(summary = "Delete broadcast by id", description = "Delete broadcast by id")
    @DeleteMapping("{id}")
    public ResponseEntity<ApiResponse> delete(@PathVariable("id") String id) {
        log.info("DELETE " + urlRoute + "/{id} endpoint hit");
        try {
            service.deleteData(id);
            return ResponseEntity.ok(new ApiResponse(true, "Successfully deleted broadcast"));
        } catch (BadRequestException e) {
            return ResponseEntity.badRequest().body(new ApiResponse(false, e.getMessage()));
        }
    }
}