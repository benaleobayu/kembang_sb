package com.bca.byc.controller;

import com.bca.byc.response.ApiDataResponse;
import com.bca.byc.response.BusinessCatalogRequest;
import com.bca.byc.response.BusinessCatalogResponse;
import com.bca.byc.security.util.ContextPrincipal;
import com.bca.byc.entity.Business;
import com.bca.byc.entity.BusinessCatalog;
import com.bca.byc.service.BusinessCatalogService;
import com.bca.byc.service.BusinessService;
import com.bca.byc.util.FileUploadHelper;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/business-catalog")
@Slf4j
@AllArgsConstructor
@Tag(name = "Business Catalog API", description = "API for managing business catalog entries")
@SecurityRequirement(name = "Authorization")
public class BusinessCatalogController {

    private final BusinessCatalogService businessCatalogService;
    private final BusinessService businessService;

    @Operation(summary = "Create a new business catalog entry with image upload")
    @PostMapping(consumes = "multipart/form-data")
    public ResponseEntity<?> createCatalog(
            @RequestParam("businessSecureId") String businessSecureId,
            @RequestParam("title") String title,
            @RequestParam("description") String description,
            @RequestParam("image") MultipartFile image) {


        Long userId  = ContextPrincipal.getId();
        // Find the Business by its secure ID
        Business business = businessService.getBusinessBySecureIdAndUserId(businessSecureId,userId);

        if (business == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Business not found or does not belong to the user.");
        };
        // Store the image (you can add logic here to save the file on disk or cloud storage)
        String imagePath = null;
        try {
            String UPLOAD_DIR = "uploads/catalog/";
            // Placeholder: Save the image to a specific directory or cloud storage and retrieve its URL or path
            imagePath =  FileUploadHelper.saveFile(image, UPLOAD_DIR);  
        } catch (IOException e) {
            log.error("Error while saving image", e);
            return ResponseEntity.internalServerError().build();
        }

        // Create and populate the BusinessCatalog entity
        BusinessCatalog businessCatalog = new BusinessCatalog();
        businessCatalog.setBusiness(business);
        businessCatalog.setTitle(title);
        businessCatalog.setImage(imagePath); // Store the path or URL of the uploaded image
        businessCatalog.setDescription(description);

        // Save the catalog entry
        BusinessCatalog createdCatalog = businessCatalogService.saveCatalog(businessCatalog);


        Map<String, String> dataObject = new HashMap<>();
        dataObject.put("businessSecureId", businessSecureId);
        dataObject.put("catalogSecureId", createdCatalog.getSecureId());
        
        return ResponseEntity.ok(new ApiDataResponse<>(true, "Successfully create Catalog Business", dataObject));
    }
 
    @Operation(summary = "Update a business catalog entry by SecureId with image upload")
    @PutMapping(value = "/update/{secureId}", consumes = "multipart/form-data")
    public ResponseEntity<?> updateCatalog(
            @PathVariable String secureId,
            @RequestParam("title") String title,
            @RequestParam("description") String description,
            @RequestParam(value = "image", required = false) MultipartFile image) {

        Long userId = ContextPrincipal.getId();

        // Fetch the existing catalog entry by secureId
        BusinessCatalog existingCatalog = businessCatalogService.getCatalogBySecureId(secureId);

        // Check if the catalog exists
        if (existingCatalog == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Catalog not found.");
        }

        // Check if the catalog belongs to the current user by validating the associated business
        if (!existingCatalog.getBusiness().getUser().getId().equals(userId)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("You are not authorized to update this catalog.");
        }

        // Update the catalog's fields
        existingCatalog.setTitle(title);
        existingCatalog.setDescription(description);

        // Handle the optional image upload
        if (image != null && !image.isEmpty()) {
            try {
                String UPLOAD_DIR = "uploads/catalog/";
                // Save the new image and update the image path
                String imagePath = FileUploadHelper.saveFile(image, UPLOAD_DIR);  
                existingCatalog.setImage(imagePath);
            } catch (IOException e) {
                log.error("Error while saving image", e);
                return ResponseEntity.internalServerError().body("Error while updating the image.");
            }
        }

        // Use the updated updateCatalog method to save the changes
        BusinessCatalog updatedCatalog = businessCatalogService.updateCatalog(secureId, existingCatalog);

        if (updatedCatalog == null) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to update the catalog.");
        }

        Map<String, String> dataObject = new HashMap<>();
        dataObject.put("catalogSecureId", updatedCatalog.getSecureId());

        return ResponseEntity.ok(new ApiDataResponse<>(true, "Successfully updated Catalog Business", dataObject));
    }





    @Operation(summary = "Get a business catalog entry by SecureId")
    @GetMapping("/{secureId}")
    public ResponseEntity<?> getCatalogBySecureId(@PathVariable String secureId) {
        // Fetch the current logged-in user's ID
        Long userId = ContextPrincipal.getId();

        // Fetch the catalog entry by secureId
        BusinessCatalog catalog = businessCatalogService.getCatalogBySecureId(secureId);

        // Check if the catalog exists
        if (catalog == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Catalog not found.");
        }

        // Check if the catalog belongs to the current user by validating the associated business
        if (!catalog.getBusiness().getUser().getId().equals(userId)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("You are not authorized to access this catalog.");
        }

        // Prepare the response using BusinessCatalogResponse DTO
        BusinessCatalogResponse response = new BusinessCatalogResponse();
        response.setId(catalog.getId());
        response.setTitle(catalog.getTitle());
        response.setDescription(catalog.getDescription());
        response.setImage(catalog.getImage());
        response.setBusinessSecureId(catalog.getBusiness().getSecureId());
        response.setUserId(catalog.getBusiness().getUser().getId());
        response.setBusinessName(catalog.getBusiness().getName());
        response.setUserName(catalog.getBusiness().getUser().getName()); // Optional field for user's name

        // If validation passes, return the catalog with the prepared response
        return ResponseEntity.ok(response);
    }

    
    
    @Operation(summary = "Get all business catalog entries by Business SecureId")
    @GetMapping("/lists/{businessSecureId}")
    public ResponseEntity<?> getAllCatalogs(
            @PathVariable String businessSecureId, 
            Pageable pageable) {

        // Fetch the current logged-in user's ID
        Long userId = ContextPrincipal.getId();

        // Validate if the user is authorized to access the business with the given secureId
        Business business = businessService.getBusinessBySecureIdAndUserId(businessSecureId, userId);
        if (business == null) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("You are not authorized to access these catalogs.");
        }

        // Fetch paginated catalogs
        Page<BusinessCatalog> catalogPage = businessCatalogService.getCatalogsByBusiness(business, pageable);

        // Convert the catalogs to BusinessCatalogResponse DTOs
        List<BusinessCatalogResponse> catalogResponses = catalogPage.getContent().stream().map(catalog -> {
            BusinessCatalogResponse response = new BusinessCatalogResponse();
            response.setId(catalog.getId());
            response.setTitle(catalog.getTitle());
            response.setDescription(catalog.getDescription());
            response.setImage(catalog.getImage());
            response.setBusinessSecureId(catalog.getBusiness().getSecureId());
            response.setUserId(catalog.getBusiness().getUser().getId());
            response.setBusinessName(catalog.getBusiness().getName());
            response.setUserName(catalog.getBusiness().getUser().getName());
            return response;
        }).collect(Collectors.toList());

        return ResponseEntity.ok(catalogResponses);
    }


    @Operation(summary = "Delete a business catalog entry by SecureId")
    @DeleteMapping("/delete/{secureId}")
    public ResponseEntity<?> deleteCatalog(@PathVariable String secureId) {
        // Fetch the current logged-in user's ID
        Long userId = ContextPrincipal.getId();
    
        // Fetch the catalog entry by secureId
        BusinessCatalog catalog = businessCatalogService.getCatalogBySecureId(secureId);
    
        // Check if the catalog exists
        if (catalog == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Catalog not found.");
        }
    
        // Check if the catalog belongs to the current user by validating the associated business
        if (!catalog.getBusiness().getUser().getId().equals(userId)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("You are not authorized to delete this catalog.");
        }
    
        // Proceed with deletion
        boolean deleted = businessCatalogService.deleteCatalogBySecureId(secureId);
    
        // Return appropriate response based on the result of deletion
        if (deleted) {
            return ResponseEntity.status(HttpStatus.OK).body("Catalog successfully deleted."); 
        } else {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to delete the catalog.");
        }
    }
    
}