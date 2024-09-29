package com.bca.byc.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.UUID;


@Data
@MappedSuperclass
public abstract class AbstractBaseEntityCms implements Serializable {

    /**
     *
     */
    @Serial
    private static final long serialVersionUID = -2119574796403647424L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "secure_id", nullable = false, unique = true, columnDefinition = "char(36) default gen_random_uuid()")
    private String secureId = UUID.randomUUID().toString();

    @Column(name = "is_active", columnDefinition = "boolean default true")
    private boolean isActive = true;

    @Column(name = "is_deleted", columnDefinition = "boolean default false")
    private boolean isDeleted = false;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @ManyToOne
    @JoinColumn(name = "created_by")
    private AppAdmin createdBy;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @ManyToOne
    @JoinColumn(name = "updated_by")
    private AppAdmin updatedBy;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }

}
