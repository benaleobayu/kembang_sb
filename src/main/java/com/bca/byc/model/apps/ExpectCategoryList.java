package com.bca.byc.model.apps;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

// inner
@Data
public  class ExpectCategoryList {
    private String id;
    private Integer index;
    private String name;
    private List<String> otherValue = new ArrayList<>();
    private List<SubExpectCategoryList> subCategories = new ArrayList<>();
}
