package com.bca.byc.model.apps;

import lombok.Data;

import java.util.List;

@Data
public  class SubExpectCategoryList {
    private Long id;
    private String name;
    private List<String> otherValue;
}
