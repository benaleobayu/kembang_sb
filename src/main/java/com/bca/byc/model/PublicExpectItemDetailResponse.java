package com.bca.byc.model;

import lombok.Data;

import java.io.Serializable;

@Data
public class PublicExpectItemDetailResponse  implements Serializable {

    private String id;
    private Long index;
    private String name;
    private Boolean isOther;
}
