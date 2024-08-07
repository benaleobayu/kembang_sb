package com.bca.byc.entity;

import com.bca.byc.validation.PhoneNumberValidation;
import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(columnDefinition = "bigint")
    private Long id;


    @Column(name = "name", length = 50, nullable = false)
    private String name;

    @Column(name = "email", length = 50, nullable = false, unique = true)
    private String email;

    @PhoneNumberValidation
    @Column(name = "phone", length = 16)
    private String phone;

    @Enumerated(EnumType.STRING)
    @Column(name = "type", nullable = false, columnDefinition = "varchar(255) default 'member'")
    private UserType type = UserType.MEMBER;

    @Column(name = "bank_account", length = 20)
    private String bankAccount;

    @Column(name = "cin", length = 20)
    private String cin;

    @Column(name = "birthdate")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDate birthdate;

    @Column(name = "education", length = 50)
    private String education;

    @Column(name = "business_name", length = 50)
    private String businessName;

    @Column(name = "biodata", columnDefinition = "text")
    private String biodata;

    @Column(name = "parent_name", length = 50)
    private String parentName;


//    private MyBusiness  business;

//    private MyInterest interest;

//    private Kanwil kanwil_id;

    private String rank;

    @Column(name = "password", length = 100)
    private String password;

    @Column(name = "last_token", columnDefinition = "text")
    private String lastToken;

    @Enumerated(EnumType.ORDINAL)
    @Column(name = "status", nullable = false, columnDefinition = "int default 0")
    private StatusType status = StatusType.PENDING;

    @Column(name = "is_suspend", nullable = false, columnDefinition = "boolean default false")
    private Boolean isSuspend = false;

    @Column(name = "is_deleted", nullable = false, columnDefinition = "boolean default false")
    private Boolean isDeleted = false;

    @Column(name = "verified_at")
    private LocalDateTime verifiedAt;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }

}
