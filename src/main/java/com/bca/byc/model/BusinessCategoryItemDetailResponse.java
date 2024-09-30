package com.bca.byc.model;

import lombok.Data;

@Data
public class BusinessCategoryItemDetailResponse extends AdminModelBaseDTOResponse {

    private String name;

    private String description;

    private Integer orders;

    private Boolean status;

}
