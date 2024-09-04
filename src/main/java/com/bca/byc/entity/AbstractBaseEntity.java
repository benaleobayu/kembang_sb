package com.bca.byc.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.UUID;


@Data
@MappedSuperclass
public abstract class AbstractBaseEntity implements Serializable {

    /**
     *
     */
    @Serial
    private static final long serialVersionUID = -2119574796403647424L;

    @Column(name = "secure_id", nullable = false, unique = true)
    private String secureId = UUID.randomUUID().toString();

    @Column(name = "is_active", columnDefinition = "boolean default true")
    private boolean isActive = true;

    @Column(name = "is_deleted", columnDefinition = "boolean default false")
    private boolean isDeleted = false;

    @Column(name = "created_at")
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
