package com.bca.byc.controller.api;


import com.bca.byc.exception.BadRequestException;
import com.bca.byc.model.cms.BusinessCategoryModelDTO;
import com.bca.byc.response.ApiListResponse;
import com.bca.byc.response.ApiResponse;
import com.bca.byc.service.MsBusinessCategoryService;
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
@RequestMapping("/api/v1/business_category")
@Tag(name = "Masterdata - Business Category")
public class BusinessCategoryResource {

    private MsBusinessCategoryService service;

    @GetMapping
    public ResponseEntity<ApiListResponse> getAll() {
        log.info("GET /api/v1/business_category endpoint hit");
        try {
            return ResponseEntity.ok(new ApiListResponse(true, "Successfully found business category", service.findByParentIdIsNull()));
        } catch (BadRequestException e) {
            return ResponseEntity.badRequest().body(new ApiListResponse(false, e.getMessage(), null));
        }
    }

    @GetMapping("{id}")
    public ResponseEntity<ApiListResponse> getById(@PathVariable("id") Long id) {
        log.info("GET /api/v1/business_category/{id} endpoint hit");
        try {
            BusinessCategoryModelDTO.DetailResponse item = service.findDataById(id);
            return ResponseEntity.ok(new ApiListResponse(true, "Successfully found business category", item));
        } catch (BadRequestException e) {
            return ResponseEntity.badRequest().body(new ApiListResponse(false, e.getMessage(), null));
        }
    }

    @PostMapping
    public ResponseEntity<ApiResponse> create(@Valid @RequestBody BusinessCategoryModelDTO.CreateRequest item) {
        log.info("POST /api/v1/business_category endpoint hit");
        try {
            service.saveData(item);
            return ResponseEntity.created(URI.create("/api/v1/business_category/"))
                    .body(new ApiResponse(true, "Successfully created parent business category"));
        } catch (BadRequestException e) {
            return ResponseEntity.badRequest().body(new ApiResponse(false, e.getMessage()));
        }
    }

     @PostMapping("/{id}/child")
    public ResponseEntity<ApiResponse> createChild(@PathVariable("id") Long id, @Valid @RequestBody BusinessCategoryModelDTO.CreateRequest item) {
        log.info("POST /api/v1/business_category" + id + "/child endpoint hit");
        try {
            item.setCheckParentId(id);
            service.saveDataChild(id, item);
            return ResponseEntity.created(URI.create("/api/v1/business_category/"))
                    .body(new ApiResponse(true, "Successfully created child of business category"));
        } catch (BadRequestException e) {
            return ResponseEntity.badRequest().body(new ApiResponse(false, e.getMessage()));
        }
    }



    @PutMapping("{id}")
    public ResponseEntity<ApiResponse> update(@PathVariable("id") Long id, @Valid @RequestBody BusinessCategoryModelDTO.UpdateRequest item) {
        log.info("PUT /api/v1/business_category/{id} endpoint hit");
        try {
            service.updateData(id, item);
            return ResponseEntity.ok(new ApiResponse(true, "Successfully updated business category"));
        } catch (BadRequestException e) {
            return ResponseEntity.badRequest().body(new ApiResponse(false, e.getMessage()));
        }
    }

    @DeleteMapping("{id}")
    public ResponseEntity<ApiResponse> delete(@PathVariable("id") Long id) {
        log.info("DELETE /api/v1/business_category/{id} endpoint hit");
        try {
            service.deleteData(id);
            return ResponseEntity.ok(new ApiResponse(true, "Successfully deleted business category"));
        } catch (BadRequestException e) {
            return ResponseEntity.badRequest().body(new ApiResponse(false, e.getMessage()));
        }
    }
}