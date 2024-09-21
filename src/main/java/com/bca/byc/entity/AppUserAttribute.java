package com.bca.byc.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "app_user_attribute")
public class AppUserAttribute {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "is_verified", columnDefinition = "boolean default false")
    private Boolean isVerified = false;

    @Column(name = "verified_at")
    private LocalDateTime verifiedAt;

    @Column(name = "is_approved", columnDefinition = "boolean default false")
    private Boolean isApproved = false;

    @Column(name = "approved_at")
    private LocalDateTime approvedAt;

    @Column(name = "is_rejected", columnDefinition = "boolean default false")
    private Boolean isRejected = false;

    @Column(name = "rejected_at")
    private LocalDateTime rejectedAt;

    @Column(name = "is_suspended", columnDefinition = "boolean default false")
    private Boolean isSuspended = false;

    @Column(name = "suspended_at")
    private LocalDateTime suspendedAt;

    @Column(name = "is_deleted", columnDefinition = "boolean default false")
    private Boolean isDeleted = false;

    @Column(name = "deleted_at")
    private LocalDateTime deletedAt;

    @Column(name = "is_recommended", columnDefinition = "boolean default false")
    private Boolean isRecommended = false;

    @Column(name = "recommended_at")
    private LocalDateTime recommendedAt;

}
