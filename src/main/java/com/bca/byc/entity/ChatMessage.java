package com.bca.byc.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import jakarta.persistence.*;
import java.time.LocalDateTime;


@EqualsAndHashCode(callSuper = true)
@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "chat_messages")
public class ChatMessage extends AbstractBaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String fromUser;  // The user who sends the message

    @Column(nullable = false)
    private String toUser;    // The user who receives the message

    @Column(nullable = false)
    private String message;   // The message content

    @Column(nullable = false)
    private LocalDateTime timestamp;  // Time the message was sent

    @Column
    private LocalDateTime deliveredAt;  // Time the message was delivered

    @Column
    private LocalDateTime readAt;  // Time the message was read

    @Column(nullable = false)
    private String roomId;  // Unique room ID for private chat
}
