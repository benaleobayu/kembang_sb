package com.bca.byc.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

import com.bca.byc.enums.RoomType;

@EqualsAndHashCode(callSuper = true)
@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "chat_rooms")
public class ChatRoom extends AbstractBaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String roomName;

    @Column(nullable = true, length = 1000)
    private String description;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private RoomType type;

    @ManyToMany
    @JoinTable(
        name = "chat_room_users",
        joinColumns = @JoinColumn(name = "chat_room_secure_id", referencedColumnName = "secure_id"),
        inverseJoinColumns = @JoinColumn(name = "app_user_secure_id", referencedColumnName = "secure_id")
    )
    @JsonManagedReference
    private List<AppUser> participants;

    @ManyToOne
    @JoinColumn(name = "creator_secure_id", referencedColumnName = "secure_id", nullable = false)
    private AppUser creator;  // Reference to the user who created the chat room

    @Column(nullable = false)
    private LocalDateTime createdAt;

    @Column
    private LocalDateTime lastMessageAt;
}
