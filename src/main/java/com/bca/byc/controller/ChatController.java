package com.bca.byc.controller;

import com.bca.byc.entity.ChatMessage;
import com.bca.byc.service.ChatService;

import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
@RestController
@RequestMapping("/api/chat")
@Slf4j
public class ChatController {

    @Autowired
    private ChatService chatService;
    @Autowired
    private SimpMessagingTemplate messagingTemplate; 
    private static final Logger logger = LoggerFactory.getLogger(ChatController.class);

// Endpoint to save a message
    @PostMapping("/send")
    public ResponseEntity<?> sendMessage(@RequestBody ChatMessage chatMessage) {
        log.info("POST /api/chat/send hit");
        try {
            log.info("Attempting to send message from {} to {}", chatMessage.getFromUser(), chatMessage.getToUser());

            // Save the message in the database
            ChatMessage savedMessage = chatService.saveMessage(chatMessage.getFromUser(), chatMessage.getToUser(), chatMessage.getMessage());

            // Send the message via WebSocket to the specific user
    //        messagingTemplate.convertAndSendToUser("user/queue/messages", savedMessage);
            messagingTemplate.convertAndSendToUser(chatMessage.getToUser(), "/queue/messages", savedMessage);

            // Log and return the saved message
            log.info("Message successfully sent from {} to {}", chatMessage.getFromUser(), chatMessage.getToUser());
            return ResponseEntity.ok(savedMessage);

        } catch (Exception e) {
            // Log the error
            log.error("Error sending message from {} to {}: {}", chatMessage.getFromUser(), chatMessage.getToUser(), e.getMessage());

            // Return error response
            return ResponseEntity.status(500).body("Failed to send message");
        }
    }

    // Endpoint to fetch chat history between two users
    @GetMapping("/private/{fromUser}/{toUser}")
    public List<ChatMessage> getPrivateChatHistory(@PathVariable String fromUser, @PathVariable String toUser) {
        return chatService.getPrivateChatHistory(fromUser, toUser);
    }
}
