package com.bca.byc.service;

import com.bca.byc.entity.AppUser;
import com.bca.byc.entity.ChatMessage;
import com.bca.byc.entity.ChatRoom;
import com.bca.byc.enums.ChatType;
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
    public ChatMessage saveMessage(AppUser fromUser, AppUser toUser, String message, ChatRoom room,String filePath, ChatType chatType) {
        // Create and populate the ChatMessage entity
        ChatMessage chatMessage = new ChatMessage();
        chatMessage.setFromUser(fromUser);         // Set the sender (fromUser)
        chatMessage.setToUser(toUser);             // Set the receiver (toUser)
        chatMessage.setMessage(message);           // Set the message content
        chatMessage.setTimestamp(LocalDateTime.now()); // Set the current timestamp
        chatMessage.setChatRoom(room);             // Set the room the message belongs to
        chatMessage.setFilePath(filePath);          
        chatMessage.setChatType(chatType);         // Set the chat type
        
        // Save and return the ChatMessage
        return chatMessageRepository.save(chatMessage);
    }

    // Utility method to generate a consistent room ID
    public String generatePrivateRoomId(String fromUser, String toUser) {
        return (fromUser.compareTo(toUser) < 0) ? fromUser + "_" + toUser : toUser + "_" + fromUser;
    }
}
