package com.bca.byc.model;

import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class PostCategoryDetailResponse extends AdminModelBaseDTOResponse {

    private String id;
    private String name;
    private String icon;
    private String description;
    private Boolean isActive;
    private Boolean isDeleted;

}
