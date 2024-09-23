package com.bca.byc.model;

import lombok.Data;

@Data
public class RoleListResponse {
    private Long id;
    private String name;
    private Boolean status;
    private Boolean orders;
    private String createdAt;
    private String createdBy;
    private String updatedAt;
    private String updatedBy;
}
