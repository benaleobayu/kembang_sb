package com.bca.byc.model.attribute;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class AttributeResponse<S> {

    private Long id;
    private String value;
    private String name;

    public AttributeResponse() {

    }
}
