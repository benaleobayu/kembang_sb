package com.bca.byc.controller.api;


import com.bca.byc.exception.BadRequestException;
import com.bca.byc.model.FaqCreateRequest;
import com.bca.byc.model.FaqDetailResponse;
import com.bca.byc.model.FaqUpdateRequest;
import com.bca.byc.response.ApiResponse;
import com.bca.byc.service.FaqService;
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
@RequestMapping("/api/v1/faq")
@Tag(name = "FAQ")
@SecurityRequirement(name = "Authorization")
public class FaqResource {

    private FaqService service;

    @GetMapping
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<ApiResponse> getAll() {
        log.info("GET /api/v1/faq endpoint hit");
        try {
            return ResponseEntity.ok(new ApiResponse(true, "Successfully found faq", service.findAllData()));
        } catch (BadRequestException e) {
            return ResponseEntity.badRequest().body(new ApiResponse(false, e.getMessage(), null));
        }
    }

    @GetMapping("{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<ApiResponse> getById(@PathVariable("id") Long id) {
        log.info("GET /api/v1/faq/{id} endpoint hit");
        try {
            FaqDetailResponse item = service.findDataById(id);
            return ResponseEntity.ok(new ApiResponse(true, "Successfully found faq", item));
        } catch (BadRequestException e) {
            return ResponseEntity.badRequest().body(new ApiResponse(false, e.getMessage(), null));
        }
    }

    @PostMapping
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<ApiResponse> create(@Valid @RequestBody FaqCreateRequest item) {
        log.info("POST /api/v1/faq endpoint hit");
        try {
            service.saveData(item);
            return ResponseEntity.created(URI.create("/api/v1/faq/"))
                    .body(new ApiResponse(true, "Successfully created faq"));
        } catch (BadRequestException e) {
            return ResponseEntity.badRequest().body(new ApiResponse(false, e.getMessage()));
        }
    }

    @PutMapping("{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<ApiResponse> update(@PathVariable("id") Long id, @Valid @RequestBody FaqUpdateRequest item) {
        log.info("PUT /api/v1/faq/{id} endpoint hit");
        try {
            service.updateData(id, item);
            return ResponseEntity.ok(new ApiResponse(true, "Successfully updated faq"));
        } catch (BadRequestException e) {
            return ResponseEntity.badRequest().body(new ApiResponse(false, e.getMessage()));
        }
    }

    @DeleteMapping("{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<ApiResponse> delete(@PathVariable("id") Long id) {
        log.info("DELETE /api/v1/faq/{id} endpoint hit");
        try {
            service.deleteData(id);
            return ResponseEntity.ok(new ApiResponse(true, "Successfully deleted faq"));
        } catch (BadRequestException e) {
            return ResponseEntity.badRequest().body(new ApiResponse(false, e.getMessage()));
        }
    }
}