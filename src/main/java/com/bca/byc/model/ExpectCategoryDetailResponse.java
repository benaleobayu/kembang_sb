package com.bca.byc.model;

import lombok.Data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Data
public class ExpectCategoryDetailResponse implements Serializable {

    private Long id;
    private String name;
    private String description;
    private Boolean isOther;
    private Integer orders;
    private Boolean status;
    private String createdAt;
    private String updatedAt;

    private List<ExpectItemDetailResponse> expectItems = new ArrayList<>();


}
