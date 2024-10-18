package com.bca.byc.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ReportUserDetailResponse {

    private String id;
    private String fullName;
    private String phoneNumber;
    private String email;

}
