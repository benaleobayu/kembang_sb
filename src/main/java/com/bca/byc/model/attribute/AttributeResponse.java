package com.bca.byc.model.attribute;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class AttributeResponse {

    private Integer id;
    private String name;

    public AttributeResponse() {

    }
}
