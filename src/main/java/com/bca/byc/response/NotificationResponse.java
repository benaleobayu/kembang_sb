package com.bca.byc.response;
import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class NotificationResponse {
    private String id;
    private String type;
    private String notifiableType;
    private String notifiableId;
    private String message;
    private String readAt;
    private String createdAt;
    private String updatedAt;

    private Boolean isRead;
}
