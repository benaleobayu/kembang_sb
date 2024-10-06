package com.bca.byc.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BusinessCatalogCountsResponse<S> {
    private String id;
    private S index;
    private String businessName;
    private Long totalCatalogs;
}
