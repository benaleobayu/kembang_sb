package com.bca.byc.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ReportUserIndexResponse {

    private String id;
    private Long index;
    private String fullName;
    private String email;
    private String phoneNumber;
    private Integer totalReport;
    private String reporterEmail;

}
