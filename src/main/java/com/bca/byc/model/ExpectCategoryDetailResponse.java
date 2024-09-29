package com.bca.byc.model;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
public class ExpectCategoryDetailResponse extends AdminModelBaseDTOResponse implements Serializable {

    private String name;
    private String description;
    private Boolean isOther;
    private Integer orders;
    private Boolean status;

    private List<ExpectItemDetailResponse> expectItems = new ArrayList<>();

}
