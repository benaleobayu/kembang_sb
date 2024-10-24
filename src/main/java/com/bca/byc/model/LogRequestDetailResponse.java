package com.bca.byc.model;

import com.bca.byc.enums.RequestType;
import lombok.Data;

@Data
public class LogRequestDetailResponse extends AdminModelBaseDTOResponse {

    private String id;

    private Long modelId;
    private String modelType;
    private String note;
    private Long adminId;
    private String adminName;

    private String createdAt;

}
