package com.bca.byc.response;
import com.bca.byc.entity.ChatMessage;
import com.bca.byc.enums.ChatType;
import com.fasterxml.jackson.annotation.JsonFormat;
import java.time.LocalDateTime;
import lombok.Data;

@Data
public class ChatMessageResponse {
    private String id;
    private String message;
    private LocalDateTime timestamp;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime createdAt;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime readAt;
    private String fromName;
    private String toName;
    private String filePath;
    private ChatType chatType;
    private ChatMessageParentResponse parentMessage; 

   public ChatMessageResponse(String id, String message, LocalDateTime timestamp, String fromName, String toName,
                               LocalDateTime createdAt, LocalDateTime readAt, String filePath, ChatType chatType,
                               ChatMessage parentMessage) {
        this.id = id;
        this.message = message;
        this.timestamp = timestamp;
        this.fromName = fromName;
        this.toName = toName;
        this.createdAt = createdAt;
        this.readAt = readAt;
        this.filePath = filePath;
        this.chatType = chatType;
        // Set the secure ID of the parent message, if it exists
       // Set the parentMessage if it exists
        // Set the parentMessage if it exists and map it to ChatMessageParentResponse
        if (parentMessage != null) {
            this.parentMessage = new ChatMessageParentResponse(
                parentMessage.getId().toString(),
                parentMessage.getMessage(),
                parentMessage.getTimestamp(),
                parentMessage.getFromUser().getName(),
                parentMessage.getToUser() != null ? parentMessage.getToUser().getName() : null,
                parentMessage.getCreatedAt(),
                parentMessage.getReadAt(),
                parentMessage.getFilePath(),
                parentMessage.getChatType()
            );
        }
     
    }
}
