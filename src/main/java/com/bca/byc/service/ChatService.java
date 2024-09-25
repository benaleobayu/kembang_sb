package com.bca.byc.service;

import com.bca.byc.entity.ChatMessage;
import com.bca.byc.repository.ChatMessageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class ChatService {

    @Autowired
    private ChatMessageRepository chatMessageRepository;

    // Save a new message
    public ChatMessage saveMessage(String fromUser, String toUser, String message) {
        String roomId = generatePrivateRoomId(fromUser, toUser);  // Generate roomId
        ChatMessage chatMessage = new ChatMessage(null, fromUser, toUser, message, LocalDateTime.now(), null, null, roomId);
        return chatMessageRepository.save(chatMessage);
    }

    // Fetch chat history between two users (private message)
    public List<ChatMessage> getPrivateChatHistory(String fromUser, String toUser) {
        String roomId = generatePrivateRoomId(fromUser, toUser);
        return chatMessageRepository.findByRoomIdOrderByTimestamp(roomId);
    }

    // Utility method to generate a consistent room ID
    public String generatePrivateRoomId(String fromUser, String toUser) {
        return (fromUser.compareTo(toUser) < 0) ? fromUser + "_" + toUser : toUser + "_" + fromUser;
    }
}
