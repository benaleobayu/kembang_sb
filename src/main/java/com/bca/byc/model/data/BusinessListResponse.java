package com.bca.byc.model.data;

import lombok.Data;

@Data
public class BusinessListResponse {
    private Long id;
    private String name;
    private String lineOfBusiness;
    private Boolean isPrimary;
}
