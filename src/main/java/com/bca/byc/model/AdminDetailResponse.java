package com.bca.byc.model;

import lombok.Data;

@Data
public class AdminDetailResponse {
    private Long id;
    private String avatar;
    private String name;
    private String email;
    private String roleName;
    private Boolean status;
    private String createdAt;
    private String updatedAt;
}

