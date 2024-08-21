package com.bca.byc.controller.api;


import com.bca.byc.exception.BadRequestException;
import com.bca.byc.model.cms.SettingsModelDTO;
import com.bca.byc.response.ApiListResponse;
import com.bca.byc.response.ApiResponse;
import com.bca.byc.service.SettingsService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@Slf4j
@RestController
@AllArgsConstructor
@RequestMapping("/api/v1/settings")
@Tag(name = "Settings API")
public class SettingResource {

    private SettingsService service;

    @Operation(hidden = true)
    @GetMapping
    public ResponseEntity<ApiListResponse> getAll() {
        log.info("GET /v1/settings endpoint hit");
        try {
            return ResponseEntity.ok(new ApiListResponse(true, "Successfully found settings", service.findAllData()));
        } catch (BadRequestException e) {
            return ResponseEntity.badRequest().body(new ApiListResponse(false, e.getMessage(), null));
        }
    }

    @Operation(hidden = true)
    @GetMapping("{id}")
    public ResponseEntity<ApiListResponse> getById(@PathVariable("id") Long id) {
        log.info("GET /v1/settings/{id} endpoint hit");
        try {
            SettingsModelDTO.SettingsDetailResponse item = service.findDataById(id);
            return ResponseEntity.ok(new ApiListResponse(true, "Successfully found settings", item));
        } catch (BadRequestException e) {
            return ResponseEntity.badRequest().body(new ApiListResponse(false, e.getMessage(), null));
        }
    }

    @Operation(hidden = true)
    @PostMapping
    public ResponseEntity<ApiResponse> create(@Valid @RequestBody SettingsModelDTO.SettingsCreateRequest item) {
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
    public ResponseEntity<ApiResponse> update(@PathVariable("id") Long id, @Valid @RequestBody SettingsModelDTO.SettingsUpdateRequest item) {
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
    public ResponseEntity<ApiListResponse> showTnc(@RequestParam("identity") String identity) {
        log.info("GET /v1/settings/search?identity={} endpoint hit", identity);
        try {
            return ResponseEntity.ok(new ApiListResponse(true, "Successfully found settings", service.showByIdentity(identity)));
        } catch (BadRequestException e) {
            return ResponseEntity.badRequest().body(new ApiListResponse(false, e.getMessage(), null));
        }
    }
}