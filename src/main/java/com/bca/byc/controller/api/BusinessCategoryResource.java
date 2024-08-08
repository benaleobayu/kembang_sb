package com.bca.byc.controller.api;

import com.bca.byc.model.cms.BusinessCategoryCreateRequest;
import com.bca.byc.model.cms.BusinessCategoryUpdateRequest;
import com.bca.byc.repository.BusinessCategoryRepository;
import com.bca.byc.response.ApiListResponse;
import com.bca.byc.response.ApiResponse;
import com.bca.byc.service.BusinessCategoryService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Slf4j
@AllArgsConstructor
@RestController
@RequestMapping("/api/v1/business_category")
@Tag(name = "Business_Category API")
public class BusinessCategoryResource {

    private final BusinessCategoryService service;

    private final BusinessCategoryRepository repository;

    @GetMapping
    public ResponseEntity<ApiListResponse> getBusinessesCategory() {
        log.info("GET /api/v1/business endpoint hit");

        try {
            // response true
            return ResponseEntity.ok(new ApiListResponse(true, "Businesses found", service.findAllData()));
        } catch (Exception e) {
            // response error
            return ResponseEntity.badRequest().body(new ApiListResponse(false, e.getMessage(), null));
        }
    }

    @PostMapping("/create")
    public ResponseEntity<ApiResponse> createBusinessCategory(@Valid @RequestBody BusinessCategoryCreateRequest dto) {
        log.info("POST /api/v1/business endpoint hit");
        try {
            // save
            service.saveData(dto);
            // response success
            return ResponseEntity.ok(new ApiResponse(true, "Business created successfully"));
        } catch (Exception e) {
            // response error
            return ResponseEntity.badRequest().body(new ApiResponse(false, e.getMessage()));
        }
    }

    @GetMapping("/{businessId}/details")
    public ResponseEntity<ApiListResponse> getBusinessCategory(@PathVariable("businessId") Long businessId) {
        log.info("GET /api/v1/business/{}/details endpoint hit", businessId);
        try {
            // response true
            return ResponseEntity.ok(new ApiListResponse(true, "Business found", service.findDataById(businessId)));
        } catch (Exception e) {
            // response error
            return ResponseEntity.badRequest().body(null);
        }
    }

    @PutMapping("/{businessId}/update")
    public ResponseEntity<ApiResponse> updateBusinessCategory(@PathVariable("businessId") Long businessId, @Valid @RequestBody BusinessCategoryUpdateRequest dto) {
        log.info("PUT /api/v1/business/{}/update endpoint hit", businessId);

        try {
            if (!repository.existsById(businessId)) {
                return ResponseEntity.badRequest().body(new ApiResponse(false, "Business not found"));
            }
            // update
            service.updateData(businessId, dto);
            // response success
            return ResponseEntity.ok(new ApiResponse(true, "Business updated successfully"));
        } catch (Exception e) {
            // response error
            return ResponseEntity.badRequest().body(new ApiResponse(false, e.getMessage()));
        }

    }

    @DeleteMapping("/{businessId}/delete")
    public ResponseEntity<ApiResponse> deleteBusinessCategory(@PathVariable("businessId") Long businessId) {
        log.info("DELETE /api/v1/business/{}/delete endpoint hit", businessId);
        try {
            if (!repository.existsById(businessId)) {
                return ResponseEntity.badRequest().body(new ApiResponse(false, "Business not found"));
            }
            // delete
            service.deleteData(businessId);
            // response success
            return ResponseEntity.ok(new ApiResponse(true, "Business deleted successfully"));
        } catch (Exception e) {
            // response error
            return ResponseEntity.badRequest().body(new ApiResponse(false, e.getMessage()));
        }
    }

}
