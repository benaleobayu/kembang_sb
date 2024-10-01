package com.bca.byc.model;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
public class BusinessCategoryIndexResponse extends AdminModelBaseDTOResponse implements Serializable {

    private String name;

    private Boolean status;

    private Integer orders;

    private List<String> subCategories = new ArrayList<>();

}

