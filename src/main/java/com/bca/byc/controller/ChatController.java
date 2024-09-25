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
            ChatMessage savedMessage = chatService.saveMessage(chatMessage.getFromUser(), chatMessage.getToUser(), chatMessage.getMessage());
          //  String channel = "/private/" + chatMessage.getFromUser() + "/" + chatMessage.getToUser() + "/queue/messages";
            String channel2 = "/private/" + chatMessage.getToUser() + "/" + chatMessage.getFromUser() + "/queue/messages";
           // messagingTemplate.convertAndSend(channel, savedMessage);
            messagingTemplate.convertAndSend(channel2, savedMessage);
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
