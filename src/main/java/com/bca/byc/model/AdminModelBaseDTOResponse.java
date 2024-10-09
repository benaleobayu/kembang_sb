package com.bca.byc.model;

import lombok.Data;

@Data
public class AdminModelBaseDTOResponse<C> {
    private String id;
    private C index;
    private String createdAt;
    private String updatedAt;
    private String createdBy;
    private String updatedBy;
}