package com.bca.byc.entity;

import com.bca.byc.validation.PhoneNumberValidation;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(columnDefinition = "bigint")
    private Long id;

    @Column(name="user_parent", length = 50, nullable = false)
    private String userParent;

    @Column(name = "name", length = 50, nullable = false)
    private String name;

    @Column(name = "email", length = 50, nullable = false, unique = true)
    private String email;

    @PhoneNumberValidation
    @Column(name = "phone", length = 16)
    private String phone;

    @Column(name = "bank_account", length = 20)
    private String bankAccount;

    @Column(name = "education", length = 50)
    private String education;

    @Column(name = "business_name", length = 50)
    private String businessName;

    @Column(name = "cin", length = 20)
    private String cin;

    @Lob
    @Column(name = "biodata", columnDefinition = "text")
    private String biodata;

//    private MyBusiness  business;

//    private MyInterest interest;

//    private Kanwil kanwil_id;

    private String rank;

    @Column(name = "password", length = 100)
    private String password;

    @Column(name = "last_token", columnDefinition = "text")
    private String lastToken;

    @Enumerated(EnumType.STRING)
    @Column(name = "type", nullable = false, columnDefinition = "varchar(255) default 'member'")
    private UserType type = UserType.MEMBER;

    @Column(name = "status", nullable = false, columnDefinition = "boolean default false")
    private Boolean status = false;

    @Column(name = "isSuspend", nullable = false, columnDefinition = "boolean default false")
    private Boolean isSuspend = false;

    @Column(name = "isDeleted", nullable = false, columnDefinition = "boolean default false")
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
