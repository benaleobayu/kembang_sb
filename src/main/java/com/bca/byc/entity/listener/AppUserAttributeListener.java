package com.bca.byc.entity.listener;

import com.bca.byc.entity.AppUserAttribute;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;

import java.time.LocalDateTime;

public class AppUserAttributeListener {

    @PrePersist
    @PreUpdate
    public void updateTimestamps(AppUserAttribute entity) {
        if (Boolean.TRUE.equals(entity.getIsVerified()) && entity.getVerifiedAt() == null) {
            entity.setVerifiedAt(LocalDateTime.now());
        }
        if (Boolean.TRUE.equals(entity.getIsApproved()) && entity.getApprovedAt() == null) {
            entity.setApprovedAt(LocalDateTime.now());
        }
        if (Boolean.TRUE.equals(entity.getIsRejected()) && entity.getRejectedAt() == null) {
            entity.setRejectedAt(LocalDateTime.now());
        }
        if (Boolean.TRUE.equals(entity.getIsSuspended()) && entity.getSuspendedAt() == null) {
            entity.setSuspendedAt(LocalDateTime.now());
        }
        if (Boolean.TRUE.equals(entity.getIsDeleted()) && entity.getDeletedAt() == null) {
            entity.setDeletedAt(LocalDateTime.now());
        }
        if (Boolean.TRUE.equals(entity.getIsHardDeleted()) && entity.getHardDeletedAt() == null) {
            entity.setHardDeletedAt(LocalDateTime.now());
        }
        if (Boolean.TRUE.equals(entity.getIsRecommended()) && entity.getRecommendedAt() == null) {
            entity.setRecommendedAt(LocalDateTime.now());
        }
        if (Boolean.TRUE.equals(entity.getIsOfficial()) && entity.getOfficialAt() == null) {
            entity.setOfficialAt(LocalDateTime.now());
        }
    }

}
