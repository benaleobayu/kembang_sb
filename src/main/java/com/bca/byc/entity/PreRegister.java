package com.bca.byc.entity;

import com.bca.byc.entity.impl.SecureIdentifiable;
import com.bca.byc.enums.AdminApprovalStatus;
import com.bca.byc.enums.UserType;
import com.bca.byc.validator.annotation.MemberTypeEnum;
import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity
@Table(name = "pre_registration", indexes = {
    @Index(name = "idx_pre_registration_secure_id", columnList = "secure_id", unique = true),
})
public class PreRegister extends AbstractBaseEntityCms implements SecureIdentifiable {

    @Override
    public String getSecureId() {
        return super.getSecureId();
    }

    @Override
    public Boolean getActive() {
        return super.getIsActive();
    }

    @Column(name = "name")
    private String name;

    @Column(name = "email", unique = true)
    private String email;

    @Column(name = "phone")
    private String phone;

    @MemberTypeEnum
    @Column(name = "member")
    private String memberType = "NOT_MEMBER";

    @Enumerated(EnumType.STRING)
    @Column(name = "type")
    private UserType type = UserType.MEMBER_SOLITAIRE;

    @Column(name = "description", columnDefinition = "text")
    private String description;

    // member data
    @Column(name = "member_bank_account", length = 20)
    private String memberBankAccount;

    @Column(name = "member_cin", length = 20)
    private String memberCin;

    @Column(name = "member_birthdate")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDate memberBirthdate;
    // ---

    // parent data
    @Column(name = "parent_bank_account", length = 20, columnDefinition = "varchar(20) default '0'")
    private String parentBankAccount;

    @Column(name = "parent_cin", length = 20)
    private String parentCin;

    @Column(name = "parent_birthdate")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDate parentBirthdate;
    // ---

    // other data

    @ManyToOne
    @JoinColumn(name = "branch_id")
    private Branch branchCode;

    @Column(name = "pic_name")
    private String picName;

    @Column(name = "orders", columnDefinition = "int default 1")
    private Integer orders = 1;

    @Enumerated(EnumType.ORDINAL)
    @Column(name = "status_approval")
    private AdminApprovalStatus statusApproval = AdminApprovalStatus.PENDING;



}
