package com.bca.byc.model;

import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class UserManagementListResponse extends AdminModelBaseDTOResponse {

    private String branchCode;
    private String name;
    private String birthDate;
    private String email;
    private String memberCin;
    private String phone;
    private String approveAt;
    private String approveBy;

    public UserManagementListResponse() {
    }

    public UserManagementListResponse(String branchCode, String name, String birthDate, String email, String memberCin, String phone, String approveAt, String approveBy) {
        this.branchCode = branchCode;
        this.name = name;
        this.birthDate = birthDate;
        this.email = email;
        this.memberCin = memberCin;
        this.phone = phone;
        this.approveAt = approveAt;
        this.approveBy = approveBy;
    }
}
