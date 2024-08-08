package com.bca.byc.controller.api;

import com.bca.byc.model.api.BusinessCreateRequest;
import com.bca.byc.model.api.BusinessUpdateRequest;
import com.bca.byc.repository.BusinessRepository;
import com.bca.byc.response.ApiListResponse;
import com.bca.byc.response.ApiResponse;
import com.bca.byc.service.BusinessService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Slf4j
@AllArgsConstructor
@RestController
@RequestMapping("/api/v1/business")
@Tag(name = "Business API")
public class BusinessResource {

    private final BusinessService businessService;

    private final BusinessRepository repository;

    @GetMapping
    public ResponseEntity<ApiListResponse> getBusinesses(){
        log.info("GET /api/v1/business endpoint hit");

        try{
            // response true
            return ResponseEntity.ok(new ApiListResponse(true, "Businesses found", businessService.getAllData()));
        }catch (Exception e){
            // response error
            return ResponseEntity.badRequest().body(new ApiListResponse(false, e.getMessage(), null));
        }
    }

    @PostMapping("/create")
    public ResponseEntity<ApiResponse> createBusiness(@RequestBody BusinessCreateRequest dto){
        log.info("POST /api/v1/business endpoint hit");
        try{
            // save
            businessService.saveData(dto);
            // response success
            return ResponseEntity.ok(new ApiResponse(true, "Business created successfully"));
        }catch (Exception e){
            // response error
            return ResponseEntity.badRequest().body(new ApiResponse(false, e.getMessage()));
        }
    }

    @GetMapping("/{businessId}/details")
    public ResponseEntity<ApiListResponse> getBusiness(@PathVariable("businessId") Long businessId) {
        log.info("GET /api/v1/business/{}/details endpoint hit", businessId);
        try{
            // response true
            return ResponseEntity.ok(new ApiListResponse(true, "Business found", businessService.findDataById(businessId)));
        }catch (Exception e){
            // response error
            return ResponseEntity.badRequest().body(new ApiListResponse(false, e.getMessage(), null));
        }
    }

    @PutMapping("/{businessId}/update")
    public ResponseEntity<ApiResponse> updateBusiness(@PathVariable("businessId") Long businessId, @Valid @RequestBody BusinessUpdateRequest dto) {
        log.info("PUT /api/v1/business/{}/update endpoint hit", businessId);

        try{
            if (!repository.existsById(businessId)){
                return ResponseEntity.badRequest().body(new ApiResponse(false, "Business not found"));
            }
            // update
            businessService.updateData(businessId, dto);
            // response success
            return ResponseEntity.ok(new ApiResponse(true, "Business updated successfully"));
        }catch (Exception e){
            // response error
            return ResponseEntity.badRequest().body(new ApiResponse(false, e.getMessage()));
        }

    }

    @DeleteMapping("/{businessId}/delete")
    public ResponseEntity<ApiResponse> deleteBusiness(@PathVariable("businessId") Long businessId) {
        log.info("DELETE /api/v1/business/{}/delete endpoint hit", businessId);
        try {
            if (!repository.existsById(businessId)) {
                return ResponseEntity.badRequest().body(new ApiResponse(false, "Business not found"));
            }
            // delete
            businessService.deleteData(businessId);
            // response success
            return ResponseEntity.ok(new ApiResponse(true, "Business deleted successfully"));
        } catch (Exception e) {
            // response error
            return ResponseEntity.badRequest().body(new ApiResponse(false, e.getMessage()));
        }
    }

}
