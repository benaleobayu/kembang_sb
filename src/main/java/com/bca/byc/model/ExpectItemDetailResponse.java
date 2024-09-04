package com.bca.byc.model;

import lombok.Data;

import java.io.Serializable;

@Data
public class ExpectItemDetailResponse implements Serializable {

    private Long id;
    private String name;
    private String description;
    private Integer orders;
    private Boolean isOther;
    private Boolean status;
    private String createdAt;
    private String updatedAt;
}
