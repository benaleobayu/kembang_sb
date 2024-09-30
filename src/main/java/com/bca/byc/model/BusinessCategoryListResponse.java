package com.bca.byc.model;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
public class BusinessCategoryListResponse extends AdminModelBaseDTOResponse {

    private String name;

    private String description;

    private Integer orders;

    private Boolean status;

    private List<BusinessCategoryItemListResponse> subCategories;
}
