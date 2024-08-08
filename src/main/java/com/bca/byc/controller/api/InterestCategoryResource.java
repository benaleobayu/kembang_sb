package com.bca.byc.controller.api;

import com.bca.byc.model.cms.InterestCategoryCreateRequest;
import com.bca.byc.model.cms.InterestCategoryUpdateRequest;
import com.bca.byc.repository.InterestCategoryRepository;
import com.bca.byc.response.ApiListResponse;
import com.bca.byc.response.ApiResponse;
import com.bca.byc.service.InterestCategoryService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Slf4j
@AllArgsConstructor
@RestController
@RequestMapping("/api/v1/interest_category")
@Tag(name = "Interest_Category API")
public class InterestCategoryResource {

    private final InterestCategoryService service;

    private final InterestCategoryRepository repository;

    @GetMapping
    public ResponseEntity<ApiListResponse> getInterestesCategory() {
        log.info("GET /api/v1/interest endpoint hit");

        try {
            // response true
            return ResponseEntity.ok(new ApiListResponse(true, "Interestes found", service.findAllData()));
        } catch (Exception e) {
            // response error
            return ResponseEntity.badRequest().body(new ApiListResponse(false, e.getMessage(), null));
        }
    }

    @PostMapping("/create")
    public ResponseEntity<ApiResponse> createInterestCategory(@Valid @RequestBody InterestCategoryCreateRequest dto) {
        log.info("POST /api/v1/interest endpoint hit");
        try {
            // save
            service.saveData(dto);
            // response success
            return ResponseEntity.ok(new ApiResponse(true, "Interest created successfully"));
        } catch (Exception e) {
            // response error
            return ResponseEntity.badRequest().body(new ApiResponse(false, e.getMessage()));
        }
    }

    @GetMapping("/{interestId}/details")
    public ResponseEntity<ApiListResponse> getInterestCategory(@PathVariable("interestId") Long interestId) {
        log.info("GET /api/v1/interest/{}/details endpoint hit", interestId);
        try {
            // response true
            return ResponseEntity.ok(new ApiListResponse(true, "Interest found", service.findDataById(interestId)));
        } catch (Exception e) {
            // response error
            return ResponseEntity.badRequest().body(null);
        }
    }

    @PutMapping("/{interestId}/update")
    public ResponseEntity<ApiResponse> updateInterestCategory(@PathVariable("interestId") Long interestId, @Valid @RequestBody InterestCategoryUpdateRequest dto) {
        log.info("PUT /api/v1/interest/{}/update endpoint hit", interestId);

        try {
            if (!repository.existsById(interestId)) {
                return ResponseEntity.badRequest().body(new ApiResponse(false, "Interest not found"));
            }
            // update
            service.updateData(interestId, dto);
            // response success
            return ResponseEntity.ok(new ApiResponse(true, "Interest updated successfully"));
        } catch (Exception e) {
            // response error
            return ResponseEntity.badRequest().body(new ApiResponse(false, e.getMessage()));
        }

    }

    @DeleteMapping("/{interestId}/delete")
    public ResponseEntity<ApiResponse> deleteInterestCategory(@PathVariable("interestId") Long interestId) {
        log.info("DELETE /api/v1/interest/{}/delete endpoint hit", interestId);
        try {
            if (!repository.existsById(interestId)) {
                return ResponseEntity.badRequest().body(new ApiResponse(false, "Interest not found"));
            }
            // delete
            service.deleteData(interestId);
            // response success
            return ResponseEntity.ok(new ApiResponse(true, "Interest deleted successfully"));
        } catch (Exception e) {
            // response error
            return ResponseEntity.badRequest().body(new ApiResponse(false, e.getMessage()));
        }
    }

}
