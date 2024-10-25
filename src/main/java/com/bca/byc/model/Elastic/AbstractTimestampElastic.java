package com.bca.byc.model.Elastic;

import lombok.Data;

@Data
public class AbstractTimestampElastic {

    private String id;

    private Long index;

    private String createdAt;

    private String createdBy;

    private String updatedAt;

    private String updatedBy;

}
