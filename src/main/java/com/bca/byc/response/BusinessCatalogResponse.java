package com.bca.byc.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BusinessCatalogResponse {

    private Long id;
    private String title;
    private String description;
    private String image;  // Path or URL to the image

    // Business-related information
    private String businessSecureId;  // Secure ID of the associated Business

    // User-related information
    private Long userId;  // ID of the user associated with the Business
    private String userName;  // Name of the user (optional, if needed)
    private String businessName ;
}
