package com.bca.byc.repository;

import com.bca.byc.entity.AppUser;
import com.bca.byc.entity.ChatMessage;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface ChatMessageRepository extends JpaRepository<ChatMessage, Long> {

    // Fetch all messages between two users
    List<ChatMessage> findByFromUserAndToUserOrToUserAndFromUserOrderByTimestamp(
        AppUser fromUser, AppUser toUser, AppUser reverseToUser, AppUser reverseFromUser);

    // Query messages by chatRoom's secureId and order by timestamp
   // List<ChatMessage> findByChatRoomSecureIdOrderByTimestamp(String secureId);
}
