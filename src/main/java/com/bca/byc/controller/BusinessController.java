package com.bca.byc.controller;

import com.bca.byc.model.BusinessDetailResponse;
import com.bca.byc.model.BusinessTreeRequest;
import com.bca.byc.model.data.BusinessListResponse;
import com.bca.byc.response.ApiDataResponse;
import com.bca.byc.response.ApiResponse;
import com.bca.byc.response.PaginationAppsResponse;
import com.bca.byc.response.ResultPageResponseDTO;
import com.bca.byc.service.BusinessService;
import com.bca.byc.service.impl.AppUserServiceImpl;
import com.bca.byc.service.impl.BusinessCategoryServiceImpl;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(BusinessController.urlRoute)
@Slf4j
@AllArgsConstructor
@Tag(name = "Business API", description = "API for managing businesses")
@SecurityRequirement(name = "Authorization")
public class BusinessController {

    static final String urlRoute = "/api/v1/business";

    private final BusinessService businessService;

    @Operation(summary = "Get all businesses")
    @GetMapping("/list")
    public ResponseEntity<PaginationAppsResponse<ResultPageResponseDTO<BusinessListResponse>>> listDataBusinessList(
            @RequestParam(name = "pages", required = false, defaultValue = "0") Integer pages,
            @RequestParam(name = "limit", required = false, defaultValue = "10") Integer limit,
            @RequestParam(name = "sortBy", required = false, defaultValue = "id") String sortBy,
            @RequestParam(name = "direction", required = false, defaultValue = "asc") String direction,
            @RequestParam(name = "keyword", required = false) String keyword) {
        // response true
        log.info("GET " + urlRoute + " endpoint hit");
        return ResponseEntity.ok().body(new PaginationAppsResponse<>(true, "Success get list BusinessList", businessService.listDataBusiness(pages, limit, sortBy, direction, keyword)));
    }

    @Operation(summary = "Get details of a business")
    @GetMapping("/{businessId}")
    public ResponseEntity<?> getBusinessDetail(@PathVariable("businessId") String businessId) {
        try {
            BusinessDetailResponse business = businessService.getBusinessBySecureId(businessId);
            return ResponseEntity.ok(new ApiDataResponse<>(true, "Success get business detail", business));
        } catch (Exception e) {
            return ResponseEntity.ok(new ApiResponse(false, e.getMessage()));
        }
    }

    @Operation(summary = "Create a new business with categories and locations")
    @PostMapping
    public ResponseEntity<ApiResponse> createBusiness(
            @RequestBody BusinessTreeRequest dto) {
        try {
            businessService.createNewBusiness(dto);
            return new ResponseEntity<>(new ApiResponse(true, "Business created successfully"), HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(new ApiResponse(false, e.getMessage()), HttpStatus.BAD_REQUEST);
        }
    }

    @Operation(summary = "Update an existing business with categories and locations")
    @PutMapping("/{businessId}")
    public ResponseEntity<?> updateBusiness(
            @PathVariable("businessId") String businessId,
            @RequestBody BusinessTreeRequest dto) {

        try {
            businessService.updateBusiness(businessId, dto);
            return ResponseEntity.ok(new ApiResponse(true, "Business updated successfully"));
        } catch (Exception e) {
            return new ResponseEntity<>(new ApiResponse(false, e.getMessage()), HttpStatus.BAD_REQUEST);
        }
    }

    @Operation(summary = "Delete a business and its related categories and locations")
    @DeleteMapping("/delete/{businessId}")
    public ResponseEntity<?> deleteBusiness(@PathVariable String businessId) {
        try {
            businessService.deleteBusiness(businessId);
            return ResponseEntity.ok(new ApiResponse(true, "Business deleted successfully"));
        } catch (Exception e) {
            return new ResponseEntity<>(new ApiResponse(false, e.getMessage()), HttpStatus.BAD_REQUEST);
        }
    }

//    @Operation(summary = "Get all businesses")
//    @GetMapping("/")
//    public ResponseEntity<List<BusinessResponse>> getAllBussiness(Pageable pageable) {
//        // Fetch the current logged-in user's ID
//        Long userId = ContextPrincipal.getId();
//
//        // Fetch paginated businesses for the user
//        Page<Business> businessPage = businessService.getBussinessByUserIdPage(userId, pageable);
//
//        // Convert the businesses to BusinessResponse DTOs
//        List<BusinessResponse> businessResponses = businessPage.getContent().stream().map(business -> {
//            BusinessResponse response = new BusinessResponse(business); // Use the constructor to set values
//            // You can add more properties to response if needed
//            return response;
//        }).collect(Collectors.toList());
//
//        // Return the response
//        return ResponseEntity.ok(businessResponses);
//    }


}
