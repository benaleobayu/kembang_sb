package com.bca.byc.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
@Table(name = "app_user_notification")
public class AppUserNotification {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "app_user_id", nullable = false)
    private Long appUserId;  // Store only the user ID
    
    // Kolom untuk pengaturan notifikasi yang lain
    @Column(name = "messages", nullable = false)
    private boolean messages;

    @Column(name = "following_followers", nullable = false)
    private boolean followingFollowers;

    @Column(name = "posts", nullable = false)
    private boolean posts;

    @Column(name = "events", nullable = false)
    private boolean events;

    @Column(name = "level_loyalty_program", nullable = false)
    private boolean levelLoyaltyProgram;

    @Column(name = "network", nullable = false)
    private boolean network;

    @Column(name = "promotions", nullable = false)
    private boolean promotions;

    @Column(name = "news", nullable = false)
    private boolean news;

}
