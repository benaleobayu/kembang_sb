package com.bca.byc.model;

import lombok.Data;

@Data
public class PostCategoryDetailResponse extends AdminModelBaseDTOResponse {

    private String name;
    private String icon;
    private String description;
    private Boolean isActive;
    private Boolean isDeleted;

}
