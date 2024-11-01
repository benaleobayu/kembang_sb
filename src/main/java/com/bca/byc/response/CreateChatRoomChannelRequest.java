package com.bca.byc.response;

import lombok.Data;
import java.util.List;

@Data
public class CreateChatRoomChannelRequest {
    
    private String channelName;
    private List<String> participantSecureIds;
    private String fromUserSecureId;
    private String description; // Description of the chat room
    private boolean isNotifications = true; // Enable notifications by default
    private boolean isAdminMessage = true; // Only admin messages allowed by default
}
