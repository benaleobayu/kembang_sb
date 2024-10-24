package com.bca.byc.model;

import lombok.Data;

@Data
public class LogGeneralRequest {

    private Long modelId;

    private String modelType;

    private String note;

    private String logFrom = "";

    private String logTo = "";

    public LogGeneralRequest(Long modelId, String modelType, String note) {
        this.modelId = modelId;
        this.modelType = modelType;
        this.note = note;
    }

    public LogGeneralRequest(Long modelId, String modelType, String note, String logFrom, String logTo) {
        this.modelId = modelId;
        this.modelType = modelType;
        this.note = note;
        this.logFrom = logFrom;
        this.logTo = logTo;
    }
}
