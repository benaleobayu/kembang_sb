package com.bca.byc.controller;

import com.bca.byc.entity.AppUser;
import com.bca.byc.entity.ChatMessage;
import com.bca.byc.entity.ChatRoom;
import com.bca.byc.enums.RoomType;
import com.bca.byc.service.ChatService;

import com.bca.byc.service.impl.AppUserServiceImpl;

import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import com.bca.byc.response.ChatMessageDTO;
import com.bca.byc.response.ChatMessageResponse;
import com.bca.byc.service.ChatRoomService;
import com.bca.byc.service.ChatMessageService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import com.bca.byc.response.ApiDataResponse;
import java.util.HashMap;
import java.util.Map;
@Tag(name = "Chat API", description = "API for managing chat messages")
@RestController
@RequestMapping("/api/chat")
@Slf4j
public class ChatController {
    @Autowired
    private AppUserServiceImpl appUserService;

    @Autowired
    private ChatService chatService;

    @Autowired
    private ChatRoomService chatRoomService;

    @Autowired
    private ChatMessageService chatMessageService;


    
    
    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    private static final Logger logger = LoggerFactory.getLogger(ChatController.class);
    @Operation(summary = "Send a chat message", description = "Send a message from one user to another using secure_id, creating a chat room if it doesn't exist")
    @PostMapping("/send")
    public ResponseEntity<?> sendMessage(@RequestBody ChatMessageDTO chatMessageDTO) {
        try {
            // Fetch the sender (fromUser)
            AppUser fromUser = appUserService.findBySecureId(chatMessageDTO.getFromUserSecureId());
            if (fromUser == null) {
                return ResponseEntity.status(404).body("Sender user not found");
            }
    
            // Handle the message depending on room type
            if (chatMessageDTO.getRoomType() == RoomType.PRIVATE) {
                return handlePrivateMessage(chatMessageDTO, fromUser);
            } else if (chatMessageDTO.getRoomType() == RoomType.CHANNEL) {
                return handleChannelMessage(chatMessageDTO, fromUser);
            } else {
                return ResponseEntity.status(400).body("Invalid room type");
            }
        } catch (Exception e) {
            log.error("Error sending message", e);
            return ResponseEntity.status(500).body("An error occurred while sending the message");
        }
    }


     // API to get chat messages between two users in a private room
     @GetMapping("/private/messages")
     public ResponseEntity<Page<ChatMessageResponse>> getPrivateMessages(
             @RequestParam String fromUserSecureId,
             @RequestParam String toUserSecureId,
             Pageable pageable) {
     
         // Fetch paginated ChatMessages
         Page<ChatMessage> messages = chatMessageService.getMessagesForPrivateRoom(fromUserSecureId, toUserSecureId, pageable);
        // Mark messages as read in the database
        chatMessageService.markMessagesAsRead(fromUserSecureId, toUserSecureId);
         // Map ChatMessage to ChatMessageResponse
         Page<ChatMessageResponse> responseMessages = messages.map(message -> new ChatMessageResponse(
                 message.getId(),
                 message.getMessage(),
                 message.getTimestamp(),
                 message.getFromUser().getUsername(),
                 message.getToUser() != null ? message.getToUser().getName() : null,
                 message.getCreatedAt(),
                 message.getReadAt()
         ));
     
         return ResponseEntity.ok(responseMessages);
     }
    
    private ResponseEntity<?> handlePrivateMessage(ChatMessageDTO chatMessageDTO, AppUser fromUser) {
        // Ensure toUserSecureId is provided
        if (chatMessageDTO.getToUserSecureId() == null) {
            return ResponseEntity.status(400).body("toUserSecureId is required for private chat");
        }
    
        // Fetch the receiver (toUser)
        AppUser toUser = appUserService.findBySecureId(chatMessageDTO.getToUserSecureId());
        if (toUser == null) {
            return ResponseEntity.status(404).body("Recipient user not found");
        }
    
        // Check if a chat room exists between these two users, or create one
        ChatRoom chatRoom = chatRoomService.findRoomByParticipantTypes(fromUser.getSecureId(), toUser.getSecureId(),RoomType.PRIVATE)
            .orElseGet(() -> createPrivateChatRoom(fromUser, toUser));
    
        // Save and send the message
        ChatMessage savedMessage = chatService.saveMessage(fromUser, toUser, chatMessageDTO.getMessage(), chatRoom);
        messagingTemplate.convertAndSend("/private/" + toUser.getSecureId() + "/queue/messages", savedMessage);
        
        Map<String, String> dataObject = new HashMap<>();
        dataObject.put("chat_room_secure_id", chatRoom.getSecureId());
        dataObject.put("message_secure_id", savedMessage.getSecureId());
        return ResponseEntity.ok(new ApiDataResponse<>(true, "Successfully create private messages", dataObject));
    }
    
    private ResponseEntity<?> handleChannelMessage(ChatMessageDTO chatMessageDTO, AppUser fromUser) {
        // Ensure chatRoomSecureId is provided
        if (chatMessageDTO.getChatRoomSecureId() == null) {
            return ResponseEntity.status(400).body("chatRoomSecureId is required for channel chat");
        }
    
        // Fetch the chat room by secureId
        ChatRoom chatRoom = chatRoomService.findBySecureId(chatMessageDTO.getChatRoomSecureId())
            .orElseThrow(() -> new RuntimeException("Chat room not found"));
    
        // Save and send the message (no toUser for a channel)
        ChatMessage savedMessage = chatService.saveMessage(fromUser, null, chatMessageDTO.getMessage(), chatRoom);
        messagingTemplate.convertAndSend("/channel/" + chatRoom.getSecureId() + "/queue/messages", savedMessage);
    
        return ResponseEntity.ok(savedMessage);
    }
    
    private ChatRoom createPrivateChatRoom(AppUser fromUser, AppUser toUser) {
        // Create a new private chat room
        List<AppUser> participants = List.of(fromUser, toUser);
        List<String> participantSecureIds = participants.stream()
                .map(AppUser::getSecureId)
                .collect(Collectors.toList());
    
        ChatRoom newRoom = chatRoomService.createRoom("Private Chat", participantSecureIds, RoomType.PRIVATE, fromUser.getSecureId());
        log.info("Created new private chat room: " + newRoom.getSecureId());
        return newRoom;
    }
    
    
    
    
}
