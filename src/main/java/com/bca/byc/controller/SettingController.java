package com.bca.byc.controller;


import com.bca.byc.exception.BadRequestException;
import com.bca.byc.model.SettingsCreateRequest;
import com.bca.byc.model.SettingsDetailResponse;
import com.bca.byc.model.SettingsUpdateRequest;
import com.bca.byc.response.ApiDataResponse;
import com.bca.byc.response.ApiResponse;
import com.bca.byc.service.SettingService;
import com.bca.byc.validator.service.AgeRangeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@Slf4j
@RestController
@RequestMapping("/cms/v1/settings")
@Tag(name = "Additional Config API")
@SecurityRequirement(name = "Authorization")
public class SettingController {

    @Autowired
    private SettingService service;

    @Qualifier("ageRangeService")
    private AgeRangeService ageRangeService;

    @Operation(hidden = true)
    @GetMapping
    public ResponseEntity<ApiDataResponse> getAll() {
        log.info("GET /v1/settings endpoint hit");
        try {
            return ResponseEntity.ok(new ApiDataResponse(true, "Successfully found settings", service.findAllData()));
        } catch (BadRequestException e) {
            return ResponseEntity.badRequest().body(new ApiDataResponse(false, e.getMessage(), null));
        }
    }

    @Operation(hidden = true)
    @GetMapping("{id}")
    public ResponseEntity<ApiDataResponse> getById(@PathVariable("id") Long id) {
        log.info("GET /v1/settings/{id} endpoint hit");
        try {
            SettingsDetailResponse item = service.findDataById(id);
            return ResponseEntity.ok(new ApiDataResponse<>(true, "Successfully found settings", item));
        } catch (BadRequestException e) {
            return ResponseEntity.badRequest().body(new ApiDataResponse(false, e.getMessage(), null));
        }
    }

    @Operation(hidden = true)
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

    @Operation(hidden = true)
    @PutMapping("{id}")
    public ResponseEntity<ApiResponse> update(@PathVariable("id") Long id, @Valid @RequestBody SettingsUpdateRequest item) {
        log.info("PUT /v1/settings/{id} endpoint hit");
        try {
            service.updateData(id, item);
            return ResponseEntity.ok(new ApiResponse(true, "Successfully updated settings"));
        } catch (BadRequestException e) {
            return ResponseEntity.badRequest().body(new ApiResponse(false, e.getMessage()));
        }
    }

    @Operation(hidden = true)
    @DeleteMapping("{id}")
    public ResponseEntity<ApiResponse> delete(@PathVariable("id") Long id) {
        log.info("DELETE /v1/settings/{id} endpoint hit");
        try {
            service.deleteData(id);
            return ResponseEntity.ok(new ApiResponse(true, "Successfully deleted settings"));
        } catch (BadRequestException e) {
            return ResponseEntity.badRequest().body(new ApiResponse(false, e.getMessage()));
        }
    }

    // show by identity
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
    @PutMapping("/age-range")
    public ResponseEntity<ApiResponse> updateAgeRange(
            @RequestParam("min") Integer min,
            @RequestParam("max") Integer max
    ){
        log.info("PUT /v1/settings/age-range endpoint hit");
        if (min < 0 || max < min) {
            return ResponseEntity.badRequest().body(new ApiResponse(false, "Invalid age range"));
        }

        ageRangeService.setMinAge(min);
        ageRangeService.setMaxAge(max);

        return ResponseEntity.ok(new ApiResponse(true, "Successfully updated age range"));
    }

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