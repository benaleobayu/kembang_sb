package com.bca.byc.model;

import lombok.Data;

@Data
public class LogRequestIndexResponse {

    private String id;
    private Long index;

    private Long modelId;
    private String modelType;
    private String note;
    private Long adminId;
    private String adminName;

    private String createdAt;
}
