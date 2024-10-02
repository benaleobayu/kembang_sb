package com.bca.byc.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.UUID;
import lombok.EqualsAndHashCode;
@Entity
@Table(name = "notifications")
@AllArgsConstructor
@NoArgsConstructor
@Data
public class Notification {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "secure_id", nullable = false, unique = true, columnDefinition = "char(36) default gen_random_uuid()")
    private String secureId = UUID.randomUUID().toString();


    @Column(length = 100)
    private String type; // Equivalent to varchar(100)

    @Column(name = "notifiable_type", length = 255)
    private String notifiableType; // Equivalent to varchar(255)

    @Column(name = "notifiable_id")
    private Long notifiableId; // Equivalent to bigint unsigned

    @Column(columnDefinition = "json")
    private String data; // Equivalent to json (you can use a string to store JSON data)

    private LocalDateTime readAt; // Equivalent to timestamp NULL

    @Column(name = "created_at")
    private LocalDateTime createdAt = LocalDateTime.now(); // Equivalent to timestamp

    @Column(name = "updated_at")
    private LocalDateTime updatedAt = LocalDateTime.now(); // Equivalent to timestamp

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private AppUser user;

    @PreUpdate
    public void setLastUpdate() {
        this.updatedAt = LocalDateTime.now();
    }
}
