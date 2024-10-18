package com.bca.byc.model.projection;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class ReasonReportProjection {

    private String id;
    private String icon;
    private String name;
    private Integer orders;
    private Boolean status;
    private Boolean isRequired;

    private LocalDateTime createdAt;
    private String createdBy;
    private LocalDateTime updatedAt;
    private String updatedBy;

}
