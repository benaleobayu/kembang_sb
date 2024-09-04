package com.bca.byc.model;

import lombok.Data;

import java.util.List;

@Data
public class FaqCategoryDetailResponse {
    private Long id;
    private String name;
    private String description;
    private Integer orders;
    private Boolean status;
    private String createdAt;
    private String updatedAt;

    private List<FaqDetailResponse> faqs; // <1>
}
