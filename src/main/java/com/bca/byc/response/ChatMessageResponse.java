package com.bca.byc.response;
import com.fasterxml.jackson.annotation.JsonFormat;
import java.time.LocalDateTime;
import lombok.Data;

@Data
public class ChatMessageResponse {
    private Long id;
    private String message;
    private LocalDateTime timestamp;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime createdAt;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime readAt;
    private String fromName;
    private String toName;

    public ChatMessageResponse(Long id, String message, LocalDateTime timestamp, String fromName, String toName,LocalDateTime createdAt,LocalDateTime readAt) {
        this.id = id;
        this.message = message;
        this.timestamp = timestamp;
        this.createdAt = createdAt;
        this.fromName = fromName;
        this.toName = toName;
        this.readAt = readAt;
    }
}
