package com.bca.byc.controller;

import com.bca.byc.entity.AppUser;
import com.bca.byc.entity.Business;
import com.bca.byc.entity.BusinessCatalog;
import com.bca.byc.entity.BusinessCategory;
import com.bca.byc.entity.BusinessHasCategory;
import com.bca.byc.entity.BusinessHasLocation;
import com.bca.byc.exception.BadRequestException;
import com.bca.byc.exception.ForbiddenException;
import com.bca.byc.entity.Location; 
import com.bca.byc.response.ApiDataResponse;
import com.bca.byc.response.BusinessCatalogResponse;
import com.bca.byc.response.BusinessRequest;
import com.bca.byc.response.BusinessResponse;
import com.bca.byc.security.util.ContextPrincipal;
import com.bca.byc.service.BusinessService;
import com.bca.byc.service.impl.AppUserServiceImpl;
import com.bca.byc.service.impl.BusinessCategoryServiceImpl;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.data.crossstore.ChangeSetPersister.NotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/business")
@Slf4j
@AllArgsConstructor
@Tag(name = "Business API", description = "API for managing businesses")
@SecurityRequirement(name = "Authorization")
public class BusinessController {

    private final BusinessService businessService;
    private final AppUserServiceImpl appUserService;
    private final BusinessCategoryServiceImpl businessCategoryService;

    @Operation(summary = "Create a new business with categories and locations")
    @PostMapping
    public ResponseEntity<ApiDataResponse<Map<String, String>>> createBusiness(
            @RequestBody BusinessRequest businessRequest) {

        String secureId = ContextPrincipal.getSecureUserId();

        // Create and populate the Business entity
        Business business = new Business();
        business.setName(businessRequest.getName());
       // business.setProvince(businessRequest.getProvince());
        business.setAddress(businessRequest.getAddress());
        business.setWebsite(businessRequest.getWebsite());
        business.setDescription(businessRequest.getDescription());

        // Set the associated user
        AppUser user = appUserService.findBySecureId(secureId); // Fetching user based on logged-in userId
        business.setUser(user);

        // Save the business entity first before handling relationships
        Business createdBusiness = businessService.saveBusiness(business);

        // Handle Business Categories via categoryItemIds
        for (Long categoryId : businessRequest.getBusinessCategoryIds()) {
            BusinessCategory childCategory = businessCategoryService.getCategoryById(categoryId);

            BusinessCategory parentCategory = childCategory.getParentId();
            if (parentCategory == null) {
                throw new BadRequestException("Parent Category not found for Child ID: " + categoryId);
            }

            BusinessHasCategory businessHasCategory = new BusinessHasCategory();
            businessHasCategory.setBusiness(createdBusiness);  // Use the saved Business entity
            businessHasCategory.setBusinessCategoryParent(parentCategory);
            businessHasCategory.setBusinessCategoryChild(childCategory);
            businessHasCategory.setCreatedAt(LocalDateTime.now());
            // businessHasCategory.setUpdatedAt(LocalDateTime.now());

            businessService.saveBusinessHasCategory(businessHasCategory);
        }

        // Handle Business Locations via locationIds
        for (Long locationId : businessRequest.getLocationIds()) {
            Location location = businessService.getLocationById(locationId)
                    .orElseThrow(() -> new BadRequestException("Location not found for ID: " + locationId));

            BusinessHasLocation businessHasLocation = new BusinessHasLocation();
            businessHasLocation.setBusiness(createdBusiness);  // Use the saved Business entity
            businessHasLocation.setLocation(location);
            businessService.saveBusinessHasLocation(businessHasLocation);
        }

        // Prepare the response
        Map<String, String> dataObject = new HashMap<>();
        dataObject.put("businessSecureId", createdBusiness.getSecureId());

        return ResponseEntity.ok(new ApiDataResponse<>(true, "Successfully created Business", dataObject));
    }



    @Operation(summary = "Update an existing business with categories and locations")
    @PutMapping("/{secureId}")
    public ResponseEntity<ApiDataResponse<Map<String, String>>> updateBusiness(
            @PathVariable String secureId, 
            @RequestBody BusinessRequest businessRequest) {

        Long userId = ContextPrincipal.getId();

        // Fetch the existing business to ensure it exists
        Business existingBusiness = businessService.getBusinessBySecureId(secureId);
        if (existingBusiness == null) {
            throw new ForbiddenException("Business not found for ID: " + secureId);
        }

        // Check if the user ID matches the business owner's user ID
        if (!existingBusiness.getUser().getId().equals(userId)) {
            throw new ForbiddenException("You do not have permission to update this business.");
        }

        // Update the business fields
        existingBusiness.setName(businessRequest.getName());
        existingBusiness.setAddress(businessRequest.getAddress());
        existingBusiness.setWebsite(businessRequest.getWebsite());
        existingBusiness.setDescription(businessRequest.getDescription());

        // Save the updated business entity
        Business updatedBusiness = businessService.saveBusiness(existingBusiness);

        // Handle Business Categories via categoryItemIds
        // First, remove existing categories to update them
        businessService.deleteBusinessHasCategoriesByBusiness(existingBusiness);

        for (Long categoryId : businessRequest.getBusinessCategoryIds()) {
            BusinessCategory childCategory = businessCategoryService.getCategoryById(categoryId);

            BusinessCategory parentCategory = childCategory.getParentId();
            if (parentCategory == null) {
                throw new BadRequestException("Parent Category not found for Child ID: " + categoryId);
            }

            BusinessHasCategory businessHasCategory = new BusinessHasCategory();
            businessHasCategory.setBusiness(updatedBusiness);  // Use the updated Business entity
            businessHasCategory.setBusinessCategoryParent(parentCategory);
            businessHasCategory.setBusinessCategoryChild(childCategory);
            businessHasCategory.setCreatedAt(LocalDateTime.now());
            businessHasCategory.setUpdatedAt(LocalDateTime.now()); // Optionally update the timestamp if required

            businessService.saveBusinessHasCategory(businessHasCategory);
        }

        // Handle Business Locations via locationIds
        // First, remove existing locations to update them
        businessService.deleteBusinessHasLocationsByBusiness(existingBusiness);

        for (Long locationId : businessRequest.getLocationIds()) {
            Location location = businessService.getLocationById(locationId)
                    .orElseThrow(() -> new BadRequestException("Location not found for ID: " + locationId));

            BusinessHasLocation businessHasLocation = new BusinessHasLocation();
            businessHasLocation.setBusiness(updatedBusiness);  // Use the updated Business entity
            businessHasLocation.setLocation(location);
            businessService.saveBusinessHasLocation(businessHasLocation);
        }

        // Prepare the response
        Map<String, String> dataObject = new HashMap<>();
        dataObject.put("businessSecureId", updatedBusiness.getSecureId());

        return ResponseEntity.ok(new ApiDataResponse<>(true, "Successfully updated Business", dataObject));
    }


    @Operation(summary = "Delete a business and its related categories and locations")
    @DeleteMapping("/delete/{secureId}")
    public ResponseEntity<ApiDataResponse<String>> deleteBusiness(@PathVariable String secureId) {
        Long userId = ContextPrincipal.getId();
        
        // Fetch the business to ensure it exists
        Business business = businessService.getBusinessBySecureId(secureId);
        if (business == null) {
            throw new ForbiddenException("Business not found for ID: " + secureId);
        }       
    
        // Check if the user ID matches the business owner's user ID
        if (!business.getUser().getId().equals(userId)) {
            throw new ForbiddenException("You do not have permission to view this business.");
        }

        // Delete associated BusinessHasCategory
        businessService.deleteBusinessHasCategoriesByBusiness(business);

        // Delete associated BusinessHasLocation
        businessService.deleteBusinessHasLocationsByBusiness(business);
        
        // Delete associated BusinessCatalog entries
        businessService.deleteBusinessCatalogsByBusiness(business);

        // Delete the business
        businessService.deleteBusiness(business);

        // Prepare the response
        return ResponseEntity.ok(new ApiDataResponse<>(true, "Business deleted successfully", "Deleted business ID: " + secureId));
    }

    @Operation(summary = "Get all businesses")
    @GetMapping("/")
    public ResponseEntity<List<BusinessResponse>> getAllBussiness(Pageable pageable) {
        // Fetch the current logged-in user's ID
        Long userId = ContextPrincipal.getId();

        // Fetch paginated businesses for the user
        Page<Business> businessPage = businessService.getBussinessByUserIdPage(userId, pageable);

        // Convert the businesses to BusinessResponse DTOs
        List<BusinessResponse> businessResponses = businessPage.getContent().stream().map(business -> {
            BusinessResponse response = new BusinessResponse(business); // Use the constructor to set values
            // You can add more properties to response if needed
            return response;
        }).collect(Collectors.toList());

        // Return the response
        return ResponseEntity.ok(businessResponses);
    }

    @Operation(summary = "Get details of a business")
    @GetMapping("/{secureId}")
    public ResponseEntity<ApiDataResponse<BusinessResponse>> getBusinessDetail(@PathVariable String secureId) {
        Long userId = ContextPrincipal.getId();
        // Fetch the business by ID
        Business business = businessService.getBusinessBySecureId(secureId);
        // Check if the user ID matches the business owner's user ID
        if (!business.getUser().getId().equals(userId)) {
            throw new ForbiddenException("You do not have permission to view this business.");
        }

        // Prepare the response
        BusinessResponse response = new BusinessResponse(business);
        return ResponseEntity.ok(new ApiDataResponse<>(true, "Business details retrieved successfully", response));
    }

}
