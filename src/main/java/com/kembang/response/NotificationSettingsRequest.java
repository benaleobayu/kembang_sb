package com.kembang.response;
import lombok.Data;
@Data
public class NotificationSettingsRequest {
    private boolean messages;
    private boolean followingFollowers;
    private boolean posts;
    private boolean events;
    private boolean levelLoyaltyProgram;
    private boolean network;
    private boolean promotions;
    private boolean news;
}
