package com.bca.byc.model;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Data
public class PublicExpectCategoryDetailResponse implements Serializable {
    private String id;
    private Long index;
    private String name;
    private String description;
    private Boolean isOther;

    private List<PublicExpectItemDetailResponse> expectItems = new ArrayList<>();
}
