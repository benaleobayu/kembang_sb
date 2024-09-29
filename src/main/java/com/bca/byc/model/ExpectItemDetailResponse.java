package com.bca.byc.model;

import lombok.Data;

import java.io.Serializable;

@Data
public class ExpectItemDetailResponse extends AdminModelBaseDTOResponse implements Serializable {

    private String name;
    private String description;
    private Integer orders;
    private Boolean isOther;
    private Boolean status;
}
