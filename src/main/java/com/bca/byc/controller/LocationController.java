package com.bca.byc.controller;


import com.bca.byc.exception.BadRequestException;
import com.bca.byc.model.LocationCreateUpdateRequest;
import com.bca.byc.model.LocationDetailResponse;
import com.bca.byc.model.LocationIndexResponse;
import com.bca.byc.response.*;
import com.bca.byc.response.ApiResponse;
import com.bca.byc.service.cms.LocationService;
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
@RequestMapping(LocationController.urlRoute)
@Tag(name = "Location API")
@SecurityRequirement(name = "Authorization")
public class LocationController {

    static final String urlRoute = "/cms/v1/ms/location";
    private LocationService service;

    @PreAuthorize("hasAuthority('location.view')")
    @Operation(summary = "Get list Location", description = "Get list Location")
    @GetMapping
    public ResponseEntity<PaginationCmsResponse<ResultPageResponseDTO<LocationIndexResponse>>> listDataLocation(
            @RequestParam(name = "pages", required = false, defaultValue = "0") Integer pages,
            @RequestParam(name = "limit", required = false, defaultValue = "10") Integer limit,
            @RequestParam(name = "sortBy", required = false, defaultValue = "updatedAt") String sortBy,
            @RequestParam(name = "direction", required = false, defaultValue = "asc") String direction,
            @RequestParam(name = "keyword", required = false) String keyword,
            @RequestParam(name = "export", required = false) Boolean export // TODO export location
    ) {
        // response true
        log.info("GET " + urlRoute + " endpoint hit");
        return ResponseEntity.ok().body(new PaginationCmsResponse<>(true, "Success get list location", service.listDataLocation(pages, limit, sortBy, direction, keyword)));
    }

    @PreAuthorize("hasAuthority('location.read')")
    @Operation(summary = "Get detail Location", description = "Get detail Location")
    @GetMapping("{id}")
    public ResponseEntity<?> getById(@PathVariable("id") String id) {
        log.info("GET " + urlRoute + "/{id} endpoint hit");
        try {
            LocationDetailResponse item = service.findDataById(id);
            return ResponseEntity.ok(new ApiDataResponse<>(true, "Successfully found location", item));
        } catch (BadRequestException e) {
            return ResponseEntity.badRequest().body(new ApiResponse(false, e.getMessage()));
        }
    }

    @PreAuthorize("hasAuthority('location.create')")
    @Operation(summary = "Create Location", description = "Create Location")
    @PostMapping
    public ResponseEntity<ApiResponse> create(@Valid @RequestBody LocationCreateUpdateRequest item) {
        log.info("POST " + urlRoute + " endpoint hit");
        try {
            service.saveData(item);
            return ResponseEntity.created(URI.create(urlRoute))
                    .body(new ApiResponse(true, "Successfully created location"));
        } catch (BadRequestException e) {
            return ResponseEntity.badRequest().body(new ApiResponse(false, e.getMessage()));
        }
    }

    @PreAuthorize("hasAuthority('location.update')")
    @Operation(summary = "Update Location", description = "Update Location")
    @PutMapping("{id}")
    public ResponseEntity<ApiResponse> update(@PathVariable("id") String id, @Valid @RequestBody LocationCreateUpdateRequest item) {
        log.info("PUT " + urlRoute + "/{id} endpoint hit");
        try {
            service.updateData(id, item);
            return ResponseEntity.ok(new ApiResponse(true, "Successfully updated location"));
        } catch (BadRequestException e) {
            return ResponseEntity.badRequest().body(new ApiResponse(false, e.getMessage()));
        }
    }

    @PreAuthorize("hasAuthority('location.delete')")
    @Operation(summary = "Delete Location", description = "Delete Location")
    @DeleteMapping("{id}")
    public ResponseEntity<ApiResponse> delete(@PathVariable("id") String id) {
        log.info("DELETE " + urlRoute + "/{id} endpoint hit");
        try {
            service.deleteData(id);
            return ResponseEntity.ok(new ApiResponse(true, "Successfully deleted location"));
        } catch (BadRequestException e) {
            return ResponseEntity.badRequest().body(new ApiResponse(false, e.getMessage()));
        }
    }
}