package com.bca.byc.response;

import com.bca.byc.enums.RoomType;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

@Data
public class ChatMessageDTO {

    @Schema(description = "secure_id of the sender", required = true)
    private String fromUserSecureId;

    @Schema(description = "secure_id of the receiver", required = false)
    private String toUserSecureId;

    @Schema(description = "Type of the chat room", required = true)
    private RoomType roomType;

    @Schema(description = "The actual message content", required = false)
    private String message;

    @Schema(description = "Chat room secure ID", required = false)
    private String chatRoomSecureId;

    @Schema(description = "Optional file upload", required = false)
    private MultipartFile file; // Optional file upload
    
    // Getters and Setters
}
