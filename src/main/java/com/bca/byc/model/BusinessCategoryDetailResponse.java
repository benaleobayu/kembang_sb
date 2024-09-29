package com.bca.byc.model;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.ArrayList;
import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
public class BusinessCategoryDetailResponse extends AdminModelBaseDTOResponse {

    private String name;
    private String description;
    private Integer orders;
    private Boolean status;

    private Long parentId;

    private List<BusinessCategoryDetailResponse> children = new ArrayList<>();

}
