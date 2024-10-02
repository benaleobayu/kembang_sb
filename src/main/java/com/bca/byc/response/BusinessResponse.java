package com.bca.byc.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

import com.bca.byc.entity.Business;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BusinessResponse {

    private String secureId;
    private String name;
    private String address;
    private String website;
    private String description;
    private List<String> categories; // List of category names or IDs
    private List<String> locations; // List of location names or IDs

    // Constructor to initialize from Business entity
    public BusinessResponse(Business business) {
        this.secureId = business.getSecureId();
        this.name = business.getName();
        this.address = business.getAddress();
        this.website = business.getWebsite();
        this.description = business.getDescription();
        this.categories = business.getBusinessCategories() // Assuming you have a method to get categories
                                .stream()
                                .map(category -> category.getBusinessCategoryChild().getName()) // Replace with appropriate method to get category name
                                .toList();
        this.locations = business.getBusinessHasLocations() // Assuming you have a method to get locations
                                 .stream()
                                 .map(location -> location.getLocation().getName()) // Replace with appropriate method to get location name
                                 .toList();
    }
}
