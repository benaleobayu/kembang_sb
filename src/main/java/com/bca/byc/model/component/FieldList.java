package com.bca.byc.model.component;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class FieldList {

    private String name;
    private boolean required;
    private boolean disabled;

}
