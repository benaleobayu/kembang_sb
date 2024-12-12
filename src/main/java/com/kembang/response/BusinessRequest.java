package com.kembang.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.Set;
@Data
@NoArgsConstructor
@AllArgsConstructor
public class BusinessRequest {
    private String name;
    // private String province;
    private String address;
    private String website;
    private String description;
    private Set<Long> businessCategoryIds; // Assuming you are using category IDs
    private Set<Long> locationIds; // Assuming you are using location IDs

}
