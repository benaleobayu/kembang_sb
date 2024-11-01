package com.bca.byc.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.persistence.*;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "chat_room_users")
public class ChatRoomUser {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "chat_room_secure_id", nullable = false, referencedColumnName = "secure_id")
    private ChatRoom chatRoom;  // Reference to ChatRoom, loaded lazily

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "app_user_secure_id", nullable = false, referencedColumnName = "secure_id")
    private AppUser appUser;  // Reference to AppUser by secureId, loaded lazily

    @Column(nullable = true)
    private boolean isCreator = false;  // Whether this user is the creator of the chat room, default to false

    @Column(nullable = true)
    private boolean isAdmin = false;  // Whether this user is an admin of the chat room, default to false
}
