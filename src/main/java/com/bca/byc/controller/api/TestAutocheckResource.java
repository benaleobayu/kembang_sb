package com.bca.byc.controller.api;


import com.bca.byc.exception.BadRequestException;
import com.bca.byc.model.test.TestAutocheckModelDTO;
import com.bca.byc.response.ApiListResponse;
import com.bca.byc.response.ApiResponse;
import com.bca.byc.service.TestAutocheckService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
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
@RequestMapping("/api/v1/autocheck")
@Tag(name = "Masterdata - Simulasi Autocheck Approval")
@SecurityRequirement(name = "Authorization")
public class TestAutocheckResource {

    private TestAutocheckService service;

    @GetMapping
    public ResponseEntity<ApiListResponse> getAll() {
        log.info("GET /v1/autocheck endpoint hit");
        try {
            return ResponseEntity.ok(new ApiListResponse(true, "Successfully found autocheck", service.findAllData()));
        } catch (BadRequestException e) {
            return ResponseEntity.badRequest().body(new ApiListResponse(false, e.getMessage(), null));
        }
    }

    @GetMapping("{id}")
    public ResponseEntity<ApiListResponse> getById(@PathVariable("id") Long id) {
        log.info("GET /v1/autocheck/{id} endpoint hit");
        try {
            TestAutocheckModelDTO.TestAutocheckDetailResponse item = service.findDataById(id);
            return ResponseEntity.ok(new ApiListResponse(true, "Successfully found autocheck", item));
        } catch (BadRequestException e) {
            return ResponseEntity.badRequest().body(new ApiListResponse(false, e.getMessage(), null));
        }
    }

    @PostMapping
    public ResponseEntity<ApiResponse> create(
            @Valid @ModelAttribute TestAutocheckModelDTO.TestAutocheckCreateRequest item) {
        log.info("POST /v1/autocheck endpoint hit");



        try {
            service.saveData(item);
            return ResponseEntity.created(URI.create("/v1/autocheck/"))
                    .body(new ApiResponse(true, "Successfully created autocheck"));
        } catch (BadRequestException e) {
            return ResponseEntity.badRequest().body(new ApiResponse(false, e.getMessage()));
        }
    }

    @PutMapping("{id}")
    public ResponseEntity<ApiResponse> update(@PathVariable("id") Long id, @Valid @ModelAttribute TestAutocheckModelDTO.TestAutocheckUpdateRequest item) {
        log.info("PUT /v1/autocheck/{id} endpoint hit");
        try {
            service.updateData(id, item);
            return ResponseEntity.ok(new ApiResponse(true, "Successfully updated autocheck"));
        } catch (BadRequestException e) {
            return ResponseEntity.badRequest().body(new ApiResponse(false, e.getMessage()));
        }
    }

    @DeleteMapping("{id}")
    public ResponseEntity<ApiResponse> delete(@PathVariable("id") Long id) {
        log.info("DELETE /v1/autocheck/{id} endpoint hit");
        try {
            service.deleteData(id);
            return ResponseEntity.ok(new ApiResponse(true, "Successfully deleted autocheck"));
        } catch (BadRequestException e) {
            return ResponseEntity.badRequest().body(new ApiResponse(false, e.getMessage()));
        }
    }
}
