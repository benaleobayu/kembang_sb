package com.bca.byc.entity;

import com.bca.byc.enums.StatusType;
import com.bca.byc.enums.UserType;
import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;


@EqualsAndHashCode(callSuper = true)
@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "app_user_detail")
public class AppUserDetail extends AbstractBaseEntityTimestamp {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", columnDefinition = "varchar(255) default ''")
    private String name = "";

    @Column(name = "phone")
    private String phone;

    @Column(name = "member_bank_account")
    private String memberBankAccount;

    @Column(name = "parent_bank_account")
    private String parentBankAccount;

    @Column(name = "member_birthdate")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDate memberBirthdate;

    @Column(name = "parent_birthdate")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDate parentBirthdate;

    @Column(name = "member_cin")
    private String memberCin;

    @Column(name = "parent_cin")
    private String parentCin;

    @Column(name = "education")
    private String education;

    @Column(name = "biodata", columnDefinition = "TEXT")
    private String biodata;

    @Enumerated(EnumType.ORDINAL)
    @Column(name = "status")
    private StatusType status = StatusType.PENDING;

    @Enumerated(EnumType.STRING)
    @Column(name = "type")
    private UserType type = UserType.MEMBER;

    @Column(name = "member_type", length = 50)
    private String memberType;

    @Column(name = "approved_by")
    private String ApprovedBy;

    @Column(name = "approved_at")
    private LocalDateTime ApprovedAt;

    @Column(name = "avatar", columnDefinition = "text")
    private String avatar;

    @Column(name = "cover", columnDefinition = "text")
    private String cover;

    @Column(name = "user_as")
    private String userAs;

    @Column(name = "branch_code")
    private String branchCode;

    @Column(name = "pic_name")
    private String picName;

}
