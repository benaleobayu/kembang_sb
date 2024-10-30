package com.bca.byc.response;
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

    Boolean isRead
) {
    public NotificationResponse {
        isRead = isRead == null ? false : isRead;
    }
}