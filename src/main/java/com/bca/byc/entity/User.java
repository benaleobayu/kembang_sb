package com.bca.byc.entity;

import com.bca.byc.validation.PhoneNumberValidation;
import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Entity
@Data
@Table(name = "users")
public class User extends AbstractBaseEntity {

    @Column(name = "name", length = 50)
    private String name;

    @Column(name = "email", length = 50, nullable = false, unique = true)
    private String email;

    @PhoneNumberValidation
    @Column(name = "phone", length = 16)
    private String phone;

    @Enumerated(EnumType.STRING)
    @Column(name = "type", nullable = false, columnDefinition = "varchar(255) default 'member'")
    private UserType type = UserType.MEMBER;

    @Column(name = "solitaire_bank_account", length = 20)
    private String solitaireBankAccount;

    @Column(name = "member_bank_account", length = 20)
    private String memberBankAccount;

    @Column(name = "cin", length = 20)
    private String cin;

    @Column(name = "birthdate")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDate birthdate;

    // parent data
    @Column(name = "parent_name", length = 50)
    private String parentName;

    @Column(name = "parent_birthdate")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDate parentBirthdate;
    // end parent data

    @Column(name = "education", length = 50)
    private String education;

    @Column(name = "biodata", columnDefinition = "text")
    private String biodata;

    private String rank;

    @Column(name = "password", length = 100)
    private String password;

    @Column(name = "last_token", columnDefinition = "text")
    private String lastToken;

    @Enumerated(EnumType.ORDINAL)
    @Column(name = "status", nullable = false, columnDefinition = "int default 0")
    private StatusType status = StatusType.PENDING;

    @Column(name = "count_reject", nullable = false, columnDefinition = "int default 0")
    private Integer countReject = 0;

    @Column(name = "is_suspended", nullable = false, columnDefinition = "boolean default false")
    private Boolean isSuspended = false;

    @Column(name = "is_deleted", nullable = false, columnDefinition = "boolean default false")
    private Boolean isDeleted = false;

    // relation
    @OneToMany(mappedBy = "user", cascade = CascadeType.MERGE, orphanRemoval = true)
    private List<Business> businesses = new ArrayList<>();
    @OneToMany(mappedBy = "user", cascade = CascadeType.MERGE, orphanRemoval = true)
    private List<UserHasFeedback> feedbacks = new ArrayList<>();
    @OneToMany(mappedBy = "user", cascade = CascadeType.MERGE, orphanRemoval = true)
    private List<UserHasExpect> userHasExpects = new ArrayList<>();

}
