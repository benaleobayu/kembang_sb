package com.bca.byc.controller.api;

import com.bca.byc.exception.BadRequestException;
import com.bca.byc.model.LocationModelDTO;
import com.bca.byc.response.ApiListResponse;
import com.bca.byc.response.ApiResponse;
import com.bca.byc.service.MsLocationService;
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
@RequestMapping("/api/v1/location")
@Tag(name = "Masterdata - Location")
public class LocationResource {

    private MsLocationService service;

    @GetMapping
    public ResponseEntity<ApiListResponse> getAll() {
        log.info("GET /api/v1/location endpoint hit");
        try {
            return ResponseEntity.ok(new ApiListResponse(true, "Successfully found location", service.findAllData()));
        } catch (BadRequestException e) {
            return ResponseEntity.badRequest().body(new ApiListResponse(false, e.getMessage(), null));
        }
    }

    @GetMapping("{id}")
    public ResponseEntity<ApiListResponse> getById(@PathVariable("id") Long id) {
        log.info("GET /api/v1/location/{id} endpoint hit");
        try {
            LocationModelDTO.LocationDetailResponse item = service.findDataById(id);
            return ResponseEntity.ok(new ApiListResponse(true, "Successfully found location", item));
        } catch (BadRequestException e) {
            return ResponseEntity.badRequest().body(new ApiListResponse(false, e.getMessage(), null));
        }
    }

    @PostMapping
    public ResponseEntity<ApiResponse> create(@Valid @ModelAttribute LocationModelDTO.LocationCreateRequest item) {
        log.info("POST /api/v1/location endpoint hit");
        try {
            service.saveData(item);
            return ResponseEntity.created(URI.create("/api/v1/location/"))
                    .body(new ApiResponse(true, "Successfully created location"));
        } catch (BadRequestException e) {
            return ResponseEntity.badRequest().body(new ApiResponse(false, e.getMessage()));
        }
    }

    @PutMapping("{id}")
    public ResponseEntity<ApiResponse> update(@PathVariable("id") Long id, @Valid @ModelAttribute LocationModelDTO.LocationUpdateRequest item) {
        log.info("PUT /api/v1/location/{id} endpoint hit");
        try {
            service.updateData(id, item);
            return ResponseEntity.ok(new ApiResponse(true, "Successfully updated location"));
        } catch (BadRequestException e) {
            return ResponseEntity.badRequest().body(new ApiResponse(false, e.getMessage()));
        }
    }

    @DeleteMapping("{id}")
    public ResponseEntity<ApiResponse> delete(@PathVariable("id") Long id) {
        log.info("DELETE /api/v1/location/{id} endpoint hit");
        try {
            service.deleteData(id);
            return ResponseEntity.ok(new ApiResponse(true, "Successfully deleted location"));
        } catch (BadRequestException e) {
            return ResponseEntity.badRequest().body(new ApiResponse(false, e.getMessage()));
        }
    }
}