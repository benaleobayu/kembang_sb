package com.bca.byc.model.export;

import com.bca.byc.enums.AdminApprovalStatus;
import com.bca.byc.enums.UserType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PreRegisterExportResponse {
    private String name;
    private String email;
    private String phone;
    private String type;
    private UserType memberType;
    private String memberBankAccount;
    private String memberCin;
    private UserType parentType;
    private String parentBankAccount;
    private String parentCin;
    private LocalDate memberBirthdate;
    private LocalDate parentBirthdate;
    private String branch;
    private String picName;
    private AdminApprovalStatus adminApprovalStatus;
    private LocalDateTime createdAt;
    private String createdBy;
}
