package com.bca.byc.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import com.bca.byc.entity.AppUserNotification;

@Data  // Lombok generates getters, setters, toString, equals, and hashCode
@NoArgsConstructor  // Lombok generates a no-args constructor
@AllArgsConstructor  // Lombok generates an all-args constructor
public class NotificationSettingsResponse {
    private boolean messages;
    private boolean followingFollowers;
    private boolean posts;
    private boolean events;
    private boolean levelLoyaltyProgram;
    private boolean network;
    private boolean promotions;
    private boolean news;

    // Constructor to map from AppUserNotification entity to DTO
    public NotificationSettingsResponse(AppUserNotification notification) {
        this.messages = notification.isMessages();
        this.followingFollowers = notification.isFollowingFollowers();
        this.posts = notification.isPosts();
        this.events = notification.isEvents();
        this.levelLoyaltyProgram = notification.isLevelLoyaltyProgram();
        this.network = notification.isNetwork();
        this.promotions = notification.isPromotions();
        this.news = notification.isNews();
    }
}
