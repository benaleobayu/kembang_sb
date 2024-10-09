package com.bca.byc.controller;


import com.bca.byc.exception.BadRequestException;
import com.bca.byc.model.SettingDetailResponse;
import com.bca.byc.model.SettingIndexResponse;
import com.bca.byc.model.SettingsCreateRequest;
import com.bca.byc.model.SettingsUpdateRequest;
import com.bca.byc.response.ApiDataResponse;
import com.bca.byc.response.ApiResponse;
import com.bca.byc.response.PaginationCmsResponse;
import com.bca.byc.response.ResultPageResponseDTO;
import com.bca.byc.service.SettingService;
import com.bca.byc.validator.service.AgeRangeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@Slf4j
@RestController
@RequestMapping(SettingController.urlRoute)
@Tag(name = "Additional Config API")
@SecurityRequirement(name = "Authorization")
@AllArgsConstructor
public class SettingController {

    static final String urlRoute = "/cms/v1/settings";
    private final SettingService service;

    @Qualifier("ageRangeService")
    private AgeRangeService ageRangeService;

    @PreAuthorize("hasAuthority('setting.view')")
    @Operation(summary = "Get list Index Setting", description = "Get list Index Setting")
    @GetMapping
    public ResponseEntity<PaginationCmsResponse<ResultPageResponseDTO<SettingIndexResponse>>> listDataIndexSetting(
            @RequestParam(name = "pages", required = false, defaultValue = "0") Integer pages,
            @RequestParam(name = "limit", required = false, defaultValue = "10") Integer limit,
            @RequestParam(name = "sortBy", required = false, defaultValue = "name") String sortBy,
            @RequestParam(name = "direction", required = false, defaultValue = "asc") String direction,
            @RequestParam(name = "keyword", required = false) String keyword) {
        // response true
        log.info("GET " + urlRoute + " endpoint hit");
        return ResponseEntity.ok().body(new PaginationCmsResponse<>(true, "Success get list Index Setting", service.listDataSetting(pages, limit, sortBy, direction, keyword)));
    }

    @PreAuthorize("hasAuthority('setting.read')")
    @Operation(summary = "Get detail Setting", description = "Get detail Setting")
    @GetMapping("{id}")
    public ResponseEntity<ApiDataResponse> getById(@PathVariable("id") String id) {
        log.info("GET /v1/settings/{id} endpoint hit");
        try {
            SettingDetailResponse item = service.findDataById(id);
            return ResponseEntity.ok(new ApiDataResponse<>(true, "Successfully found settings", item));
        } catch (BadRequestException e) {
            return ResponseEntity.badRequest().body(new ApiDataResponse(false, e.getMessage(), null));
        }
    }

    @PreAuthorize("hasAuthority('setting.create')")
    @Operation(summary = "Create new Setting", description = "Create new Setting")
    @PostMapping
    public ResponseEntity<ApiResponse> create(@Valid @RequestBody SettingsCreateRequest item) {
        log.info("POST /v1/settings endpoint hit");
        try {
            service.saveData(item);
            return ResponseEntity.created(URI.create("/v1/settings/"))
                    .body(new ApiResponse(true, "Successfully created settings"));
        } catch (BadRequestException e) {
            return ResponseEntity.badRequest().body(new ApiResponse(false, e.getMessage()));
        }
    }

    @PreAuthorize("hasAuthority('setting.update')")
    @Operation(summary = "Update Setting", description = "Update Setting")
    @PutMapping("{id}")
    public ResponseEntity<ApiResponse> update(@PathVariable("id") String id, @Valid @RequestBody SettingsUpdateRequest item) {
        log.info("PUT /v1/settings/{id} endpoint hit");
        try {
            service.updateData(id, item);
            return ResponseEntity.ok(new ApiResponse(true, "Successfully updated settings"));
        } catch (BadRequestException e) {
            return ResponseEntity.badRequest().body(new ApiResponse(false, e.getMessage()));
        }
    }

    @PreAuthorize("hasAuthority('setting.delete')")
    @Operation(summary = "Delete Setting", description = "Delete Setting")
    @DeleteMapping("{id}")
    public ResponseEntity<ApiResponse> delete(@PathVariable("id") String id) {
        log.info("DELETE /v1/settings/{id} endpoint hit");
        try {
            service.deleteData(id);
            return ResponseEntity.ok(new ApiResponse(true, "Successfully deleted settings"));
        } catch (BadRequestException e) {
            return ResponseEntity.badRequest().body(new ApiResponse(false, e.getMessage()));
        }
    }

    // show by identity
    @Operation(summary = "Get settings by identity", hidden = true)
    @GetMapping("/search")
    public ResponseEntity<ApiDataResponse> showTnc(@RequestParam("identity") String identity) {
        log.info("GET /api/v1/settings/search?identity={} endpoint hit", identity);
        try {
            return ResponseEntity.ok(new ApiDataResponse(true, "Successfully found settings", service.showByIdentity(identity)));
        } catch (BadRequestException e) {
            return ResponseEntity.badRequest().body(new ApiDataResponse(false, e.getMessage(), null));
        }
    }

    // other config
    @Operation(summary = "Update age range", hidden = true)
    @PutMapping("/age-range")
    public ResponseEntity<ApiResponse> updateAgeRange(
            @RequestParam("min") Integer min,
            @RequestParam("max") Integer max
    ) {
        log.info("PUT /v1/settings/age-range endpoint hit");
        if (min < 0 || max < min) {
            return ResponseEntity.badRequest().body(new ApiResponse(false, "Invalid age range"));
        }

        ageRangeService.setMinAge(min);
        ageRangeService.setMaxAge(max);

        return ResponseEntity.ok(new ApiResponse(true, "Successfully updated age range"));
    }

    @Operation(summary = "Get age range", hidden = true)
    @GetMapping("/age-range")
    public ResponseEntity<ApiDataResponse> getAgeRange() {
        log.info("GET /v1/settings/age-range endpoint hit");
        AgeRangeResponse response = new AgeRangeResponse(ageRangeService.getMinAge(), ageRangeService.getMaxAge());
        return ResponseEntity.ok(new ApiDataResponse(true, "Successfully found age range", response));
    }

    @Data
    @AllArgsConstructor
    private static class AgeRangeResponse {
        private Integer min;
        private Integer max;
    }
}