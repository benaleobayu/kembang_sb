package com.bca.byc.model;

import lombok.Data;

@Data
public class AdminModelBaseDTOResponse<C, U> {
    private String id;
    private Long index;
    private String createdAt;
    private String updatedAt;
    private String createdBy;
    private String updatedBy;
}