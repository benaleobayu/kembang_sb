package com.kembang.response;

import lombok.Data;
import java.util.List;

@Data
public class UpdateChatRoomChannelRequest {
    
    private String fromUserSecureId;  // ID of the user requesting the update
    private String channelName;       // New name for the chat room
    private String description;       // New description for the chat room
    private boolean isNotifications = true;  // Whether notifications are enabled
    private boolean isAdminMessage = true;   // Whether only admin messages are allowed
    private List<String> participantSecureIds;  // List of participant IDs to be included in the chat room
}
