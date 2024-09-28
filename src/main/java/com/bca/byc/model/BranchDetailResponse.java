package com.bca.byc.model;

import lombok.Data;

@Data
public class BranchDetailResponse {

    private String id;

    private String code;

    private String name;

    private String address;

    private String phone;

    private String location;

    private String createdAt;

    private String updatedBy;

    private String updatedAt;

    private String createdBy;

    private String status;
}
