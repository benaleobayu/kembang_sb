package com.bca.byc.model.data;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ListTagUserResponse {

    private Long id;
    private String avatar;
    private String name;

    private String businessName;
    private String lineOfBusiness;
    private Boolean isPrimary;

    public ListTagUserResponse() {

    }
}

