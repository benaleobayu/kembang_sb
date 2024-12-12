package com.kembang.model;

import lombok.Data;

@Data
public class ModelBaseDTOResponse<C> {
    private String id;
    private C index;
    private String createdAt;
    private String updatedAt;
    private String createdBy;
    private String updatedBy;
}