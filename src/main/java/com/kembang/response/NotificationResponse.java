package com.kembang.response;
import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Data;

public record NotificationResponse(
    String id,
    String notifiableType,
    String readAt,
    String createdAt,

    String notifiableMessage,

    String userid,
    String userAvatar,
    String userName,
    Boolean isFollowed,

    String postId,
    String postThumbnail,

    Boolean isRead,

    String status
) {
    public NotificationResponse {
        isRead = isRead != null && isRead;
    }
}