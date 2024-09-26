package com.bca.byc.service;
import com.bca.byc.entity.ChatMessage;
import com.bca.byc.repository.ChatMessageRepository;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

@Service
public class ChatMessageService {

    @Autowired
    private ChatMessageRepository chatMessageRepository;

    // Get paginated messages in a private room between two users (both directions)
    public Page<ChatMessage> getMessagesForPrivateRoom(String fromUserSecureId, String toUserSecureId, Pageable pageable) {
        return chatMessageRepository.findMessagesByPrivateRoomAndParticipants(fromUserSecureId, toUserSecureId, pageable);
    }
    public int markMessagesAsRead(String fromUserSecureId, String toUserSecureId) {
        // Optional validation logic before the update
        if (fromUserSecureId == null || toUserSecureId == null) {
            throw new IllegalArgumentException("User IDs cannot be null");
        }
    
        // Perform the batch update in the database
        return chatMessageRepository.updateReadAtForMessages(fromUserSecureId, toUserSecureId, LocalDateTime.now());
    }
      // Fetch paginated messages for the specified channel
    public Page<ChatMessage> getMessagesForChannel(String roomSecureId, Pageable pageable) {
        return chatMessageRepository.findByRoomSecureId(roomSecureId, pageable);
    }
    // Mark all messages as read for a user in the specified channel
    public int markMessagesAsReadForChannel(String fromUserSecureId,  String channelSecureId) {
          // Optional validation logic before the update
          if (fromUserSecureId == null || channelSecureId == null) {
            throw new IllegalArgumentException("User IDs cannot be null");
        }
        // Perform the batch update in the database
        return chatMessageRepository.updateReadAtForChannelMessages(fromUserSecureId, channelSecureId, LocalDateTime.now());
    }



}
