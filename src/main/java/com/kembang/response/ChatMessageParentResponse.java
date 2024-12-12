package com.kembang.response;
import com.kembang.enums.ChatType;

import java.time.LocalDateTime;
import lombok.Data;

@Data
public class ChatMessageParentResponse {
    private String id;
    private String message;
    private LocalDateTime timestamp;
    private String fromName;
    private String toName;
    private LocalDateTime createdAt;
    private LocalDateTime readAt;
    private String filePath;
    private ChatType chatType;

    public ChatMessageParentResponse(String id, String message, LocalDateTime timestamp, String fromName, String toName,
                                     LocalDateTime createdAt, LocalDateTime readAt, String filePath, ChatType chatType) {
        this.id = id;
        this.message = message;
        this.timestamp = timestamp;
        this.fromName = fromName;
        this.toName = toName;
        this.createdAt = createdAt;
        this.readAt = readAt;
        this.filePath = filePath;
        this.chatType = chatType;
    }

    // Getters and setters...
}
