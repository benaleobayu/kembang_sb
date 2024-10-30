package com.bca.byc.entity;

import com.bca.byc.entity.impl.SecureIdentifiable;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@EqualsAndHashCode(callSuper = true)
@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "broadcast", uniqueConstraints = {@UniqueConstraint(name = "branch_seq_id", columnNames = "secure_id")})
public class BroadcastManagement extends AbstractBaseEntityCms implements SecureIdentifiable {

    @Column(name = "title")
    private String title;
    
    @Column(name = "message", columnDefinition = "text")
    private String message;

    @Column(name = "status")
    private String status;

    @Column(name = "is_scheduled")
    private Boolean isScheduled;

    @Column(name = "is_sent")
    private Boolean isSent;

    @Column(name = "post_at")
    private LocalDateTime postAt;

    @Override
    public Long getId() {
        return super.getId();
    }

    @Override
    public String getSecureId() {
        return super.getSecureId();
    }

    @Override
    public Boolean getIsActive() {
        return super.getIsActive();
    }
}
