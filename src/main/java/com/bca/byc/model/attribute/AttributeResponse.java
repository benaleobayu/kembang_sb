package com.bca.byc.model.attribute;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class AttributeResponse<S> {


    private S id;
    private String name;

    public AttributeResponse() {

    }
}
