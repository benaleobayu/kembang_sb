package com.bca.byc.response;
import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class NotificationResponse {
    private String id;
    private String notifiableType;
    private String readAt;
    private String createdAt;

    private String notifiableMessage;

    private String userid;
    private String userAvatar;
    private Boolean isFollowed;

    private String postId;
    private String postThumbnail;

    private Boolean isRead = false;
}
