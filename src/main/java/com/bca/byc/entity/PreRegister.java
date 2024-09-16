package com.bca.byc.entity;

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
@Table(name = "pre_registration")
public class PreRegister extends AbstractBaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

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
    private UserType type = UserType.MEMBER;

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

    // child data
    @Column(name = "child_bank_account", length = 20, columnDefinition = "varchar(20) default '0'")
    private String childBankAccount;

    @Column(name = "child_cin", length = 20)
    private String childCin;

    @Column(name = "child_birthdate")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDate childBirthdate;
    // ---

    // other data

    @Column(name = "branch_code", length = 80)
    private String branchCode;

    @Column(name = "pic_name")
    private String picName;

    @Column(name = "orders", columnDefinition = "int default 1")
    private Integer orders = 1;

    @Column(name = "status", columnDefinition = "boolean default true")
    private Boolean status = true;

    @ManyToOne
    @JoinColumn(name = "created_by")
    private AppAdmin createdBy;

    @ManyToOne
    @JoinColumn(name = "updated_by")
    private AppAdmin updatedBy;

    @Enumerated(EnumType.ORDINAL)
    @Column(name = "status_approval")
    private AdminApprovalStatus statusApproval = AdminApprovalStatus.PENDING;



}
