package com.bca.byc.response;

import com.bca.byc.enums.RoomType;

import lombok.Data;

@Data
public class ChatMessageDTO {
    private String fromUserSecureId;  // secure_id of the sender
    private String toUserSecureId;
    private RoomType roomType;    // secure_id of the receiver (optional or nullable)
    private String message;           // The actual message content
    private String chatRoomSecureId;

    // Getters and Setters
}
