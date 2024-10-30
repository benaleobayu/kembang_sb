package com.bca.byc.entity;

import com.bca.byc.entity.impl.SecureIdentifiable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.UUID;
import lombok.EqualsAndHashCode;
@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "notifications")
@AllArgsConstructor
@NoArgsConstructor
@Data
public class Notification extends AbstractBaseEntity implements SecureIdentifiable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 100)
    private String type; // USER / ADMIN

    @Column(name = "notifiable_type", length = 255)
    private String notifiableType; // e.g LIKE / COMMENT / FOLLOWER

    @Column(name = "notifiable_id")
    private String notifiableId; // Equivalent to bigint unsigned

    @Column(columnDefinition = "text")
    private String message; // Equivalent to json (you can use a string to store JSON data)

    @Column(name = "read_at")
    private LocalDateTime readAt; // Equivalent to timestamp NULL

    @Column(name = "is_read")
    private Boolean isRead;

    @Column(name = "created_by")
    private Long createdBy;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private AppUser user;

}
