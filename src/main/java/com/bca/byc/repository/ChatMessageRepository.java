package com.bca.byc.repository;

import com.bca.byc.entity.ChatMessage;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ChatMessageRepository extends JpaRepository<ChatMessage, Long> {

    // Fetch all messages between two users
    List<ChatMessage> findByFromUserAndToUserOrToUserAndFromUserOrderByTimestamp(String fromUser, String toUser, String toUserAlt, String fromUserAlt);

    // Fetch all messages in a specific chat room
    List<ChatMessage> findByRoomIdOrderByTimestamp(String roomId);
}
