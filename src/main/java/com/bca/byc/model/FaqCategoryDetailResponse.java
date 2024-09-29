package com.bca.byc.model;

import lombok.Data;

import java.util.List;

@Data
public class FaqCategoryDetailResponse extends AdminModelBaseDTOResponse {

    private String name;
    private String description;
    private Integer orders;
    private Boolean status;

    private List<FaqDetailResponse> faqs; // <1>
}
