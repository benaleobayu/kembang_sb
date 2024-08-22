package com.bca.byc.controller.api;

import com.bca.byc.exception.BadRequestException;
import com.bca.byc.model.ExpectItemModelDTO;
import com.bca.byc.response.ApiListResponse;
import com.bca.byc.response.ApiResponse;
import com.bca.byc.service.ExpectItemService;
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
@RequestMapping("/api/v1/expect-item")
@Tag(name = "Expect Item")
public class ExpectItemResource {

    private ExpectItemService service;

    @GetMapping
    public ResponseEntity<ApiListResponse> getAll() {
        log.info("GET /api/v1/expect-item endpoint hit");
        try {
            return ResponseEntity.ok(new ApiListResponse(true, "Successfully found expect item", service.findAllData()));
        } catch (BadRequestException e) {
            return ResponseEntity.badRequest().body(new ApiListResponse(false, e.getMessage(), null));
        }
    }

    @GetMapping("{id}")
    public ResponseEntity<ApiListResponse> getById(@PathVariable("id") Long id) {
        log.info("GET /api/v1/expect-item/{id} endpoint hit");
        try {
            ExpectItemModelDTO.ExpectItemDetailResponse item = service.findDataById(id);
            return ResponseEntity.ok(new ApiListResponse(true, "Successfully found expect item", item));
        } catch (BadRequestException e) {
            return ResponseEntity.badRequest().body(new ApiListResponse(false, e.getMessage(), null));
        }
    }

    @PostMapping
    public ResponseEntity<ApiResponse> create(@Valid @RequestBody ExpectItemModelDTO.ExpectItemCreateRequest item) {
        log.info("POST /api/v1/expect-item endpoint hit");
        try {
            service.saveData(item);
            return ResponseEntity.created(URI.create("/api/v1/expect-item/"))
                    .body(new ApiResponse(true, "Successfully created expect item"));
        } catch (BadRequestException e) {
            return ResponseEntity.badRequest().body(new ApiResponse(false, e.getMessage()));
        }
    }

    @PutMapping("{id}")
    public ResponseEntity<ApiResponse> update(@PathVariable("id") Long id, @Valid @RequestBody ExpectItemModelDTO.ExpectItemUpdateRequest item) {
        log.info("PUT /api/v1/expect-item/{id} endpoint hit");
        try {
            service.updateData(id, item);
            return ResponseEntity.ok(new ApiResponse(true, "Successfully updated expect item"));
        } catch (BadRequestException e) {
            return ResponseEntity.badRequest().body(new ApiResponse(false, e.getMessage()));
        }
    }

    @DeleteMapping("{id}")
    public ResponseEntity<ApiResponse> delete(@PathVariable("id") Long id) {
        log.info("DELETE /api/v1/expect-item/{id} endpoint hit");
        try {
            service.deleteData(id);
            return ResponseEntity.ok(new ApiResponse(true, "Successfully deleted expect item"));
        } catch (BadRequestException e) {
            return ResponseEntity.badRequest().body(new ApiResponse(false, e.getMessage()));
        }
    }
}