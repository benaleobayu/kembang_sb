package com.bca.byc.model;

import com.bca.byc.enums.AdminApprovalStatus;
import com.bca.byc.enums.UserType;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.time.LocalDate;

@Data
public class PreRegisterCreateRequest {

    @NotBlank(message = "Name is required")
    private String name;

    @NotBlank(message = "Email is required")
    private String email;

    private String phone;

    @Schema(description = "MEMBER | NOT_CUSTOMER", example = "MEMBER | NOT_CUSTOMER")
    private UserType type;

    @Schema(description = "SOLITAIRE | PRIORITY | NOT_MEMBER", example = "NOT_MEMBER")
    private String memberType;

    private String description;

    private String memberBankAccount;

    private String childBankAccount;

    private LocalDate memberBirthdate;

    private LocalDate childBirthdate;

    private String memberCin;

    private String childCin;

    private Boolean status;

    private AdminApprovalStatus statusApproval;

    public PreRegisterCreateRequest() {
    }

    public PreRegisterCreateRequest(String name, String email, String phone, UserType type, String memberType, String description, String memberBankAccount, String childBankAccount, LocalDate memberBirthdate, LocalDate childBirthdate, String memberCin, String childCin, AdminApprovalStatus statusApproval) {
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.type = type;
        this.memberType = memberType;
        this.description = description;
        this.memberBankAccount = memberBankAccount;
        this.childBankAccount = childBankAccount;
        this.memberBirthdate = memberBirthdate;
        this.childBirthdate = childBirthdate;
        this.memberCin = memberCin;
        this.childCin = childCin;
        this.statusApproval = statusApproval;
    }

    public PreRegisterCreateRequest(String name, String email, String phone, UserType type, String memberType, String description, String memberBankAccount, String childBankAccount, LocalDate memberBirthdate, LocalDate childBirthdate, String memberCin, String childCin) {
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.type = type;
        this.memberType = memberType;
        this.description = description;
        this.memberBankAccount = memberBankAccount;
        this.childBankAccount = childBankAccount;
        this.memberBirthdate = memberBirthdate;
        this.childBirthdate = childBirthdate;
        this.memberCin = memberCin;
        this.childCin = childCin;
    }


}
