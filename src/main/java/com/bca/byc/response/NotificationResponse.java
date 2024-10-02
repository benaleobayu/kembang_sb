package com.bca.byc.response;
import java.time.LocalDateTime;
import lombok.Data;

@Data
public class NotificationResponse {
    private String id;
    private String type;
    private String notifiableType;
    private Long notifiableId;
    private String data;
    private LocalDateTime readAt;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public NotificationResponse(String id, String type, String notifiableType, Long notifiableId, 
                                String data, LocalDateTime readAt, LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.id = id;
        this.type = type;
        this.notifiableType = notifiableType;
        this.notifiableId = notifiableId;
        this.data = data;
        this.readAt = readAt;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    // Getters and setters (or use Lombok's @Data if preferred)
}
