package com.kembang.controller;

import com.kembang.model.ChatMessage;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

import java.util.Date;

@Controller
public class ChatController {

    // Send the message to all connected clients
    @MessageMapping("/messages")
    @SendTo("/topic/messages")
    public ChatMessage sendMessage(@Payload ChatMessage chatMessage) {
        chatMessage.setTimestamp(new Date());
        return chatMessage;
    }

    // Handle message deletion and broadcast the message ID to /topic/deletions
    @MessageMapping("/deleteMessage")
    @SendTo("/topic/deletions")
    public String deleteMessage(@Payload String messageId) {
        // Logic to remove the message from a database if needed can be added here
        // For now, we just send the message ID to all clients
        return messageId;  // Send the message ID to inform clients that it should be deleted
    }
}

