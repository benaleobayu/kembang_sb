package com.bca.byc.model;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.ArrayList;
import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
public class FaqCategoryIndexResponse extends AdminModelBaseDTOResponse {

    private String name;
    private String description;
    private Boolean status;
    private Integer orders;
    private List<String> faqs = new ArrayList<>();

}
