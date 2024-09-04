package com.bca.byc.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.bca.byc.enums.StatusType;
import com.bca.byc.enums.UserType;
import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDate;
import java.time.LocalDateTime;


@EqualsAndHashCode(callSuper = true)
@Entity
@Data
@Table(name = "app_user_detail")
public class AppUserDetail extends AbstractBaseEntityTimestamp{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", columnDefinition = "varchar(255) default ''")
    private String name = "";

    @Column(name = "phone")
    private String phone;

    @Column(name = "member_bank_account")
    private String memberBankAccount;

    @Column(name = "child_bank_account")
    private String childBankAccount;

    @Column(name = "member_birthdate")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDate memberBirthdate;

    @Column(name = "child_birthdate")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDate childBirthdate;

    @Column(name = "member_cin")
    private String memberCin;

    @Column(name = "child_cin")
    private String childCin;

    @Column(name = "education")
    private String education;

    @Column(name = "biodata", columnDefinition = "TEXT")
    private String biodata;

    @Enumerated(EnumType.ORDINAL)
    @Column(name = "status", columnDefinition = "int default 0")
    private StatusType status = StatusType.PENDING;

    @Enumerated(EnumType.STRING)
    @Column(name = "type", columnDefinition = "varchar(255) default 'MEMBER'")
    private UserType type;

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


}
