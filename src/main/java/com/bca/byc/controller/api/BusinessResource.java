package com.bca.byc.controller.api;

import com.bca.byc.model.api.BusinessCreateRequest;
import com.bca.byc.repository.BusinessRepository;
import com.bca.byc.response.ApiListResponse;
import com.bca.byc.response.ApiResponse;
import com.bca.byc.service.BusinessService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
            return ResponseEntity.ok(new ApiListResponse(true, "Businesses found", businessService.findAllBusiness()));
        }catch (Exception e){
            return ResponseEntity.badRequest().body(new ApiListResponse(false, e.getMessage(), null));
        }
    }

    @PostMapping
    public ResponseEntity<ApiResponse> createBusiness(@RequestBody BusinessCreateRequest dto){
        log.info("POST /api/v1/business endpoint hit");
        try{
            businessService.saveBusiness(dto);
            return ResponseEntity.ok(new ApiResponse(true, "Business created successfully"));
        }catch (Exception e){
            return ResponseEntity.badRequest().body(new ApiResponse(false, e.getMessage()));
        }
    }

}
