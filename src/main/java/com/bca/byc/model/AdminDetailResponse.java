package com.bca.byc.model;

import lombok.Data;

@Data
public class AdminDetailResponse {
    private Long id;
    private String name;
    private String email;
    private String roleName;
    private String description;
    private Boolean status;
    private String createdAt;
    private String updatedAt;
}

