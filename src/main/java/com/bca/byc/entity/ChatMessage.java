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

    // Reference the AppUser entity for the sender, using secure_id as the foreign key
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "from_user_secure_id", referencedColumnName = "secure_id", nullable = false)
    private AppUser fromUser;  // The user who sends the message

    // Reference the AppUser entity for the receiver, using secure_id as the foreign key, nullable
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "to_user_secure_id", referencedColumnName = "secure_id", nullable = true)
    private AppUser toUser;    // The user who receives the message, nullable for room-based chats

    @Column(nullable = false)
    private String message;   // The message content

    @Column(nullable = false)
    private LocalDateTime timestamp;  // Time the message was sent

    @Column
    private LocalDateTime deliveredAt;  // Time the message was delivered

    @Column
    private LocalDateTime readAt;  // Time the message was read

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "room_secure_id", referencedColumnName = "secure_id", nullable = true)
    private ChatRoom chatRoom;  // The chat room this message belongs to
}
