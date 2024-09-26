package com.bca.byc.controller;

import com.bca.byc.entity.AppUser;
import com.bca.byc.entity.ChatMessage;
import com.bca.byc.entity.ChatRoom;
import com.bca.byc.enums.ChatType;
import com.bca.byc.enums.RoomType;
import com.bca.byc.service.ChatService;

import com.bca.byc.service.impl.AppUserServiceImpl;

import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import io.swagger.v3.oas.annotations.Parameter;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.tags.Tag;
import com.bca.byc.response.ChatMessageDTO;
import com.bca.byc.response.ChatMessageResponse;
import com.bca.byc.service.ChatRoomService;
import com.bca.byc.service.ChatMessageService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import com.bca.byc.response.ApiDataResponse;
import com.bca.byc.util.FileUploadHelper;
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
    @PostMapping(value = "/send", consumes = "multipart/form-data")
    public ResponseEntity<?> sendMessage(
        @Parameter(description = "secure_id of the sender") @RequestPart("fromUserSecureId") String fromUserSecureId,
        @Parameter(description = "secure_id of the receiver") @RequestPart(value = "toUserSecureId", required = false) String toUserSecureId,
        @Parameter(description = "Type of the chat room") @RequestPart("roomType") String roomType,
        @Parameter(description = "The actual message content") @RequestPart("message") String message,
        @Parameter(description = "Chat room secure ID (required if roomType is CHANNEL)", required = false) @RequestPart(value = "chatRoomSecureId", required = false) String chatRoomSecureId,
        @Parameter(description = "Optional file upload", content = @Content(mediaType = "multipart/form-data"))
        @RequestPart(value = "file", required = false) MultipartFile file) {
            
        
        try {
            ChatMessageDTO chatMessageDTO = new ChatMessageDTO();
            chatMessageDTO.setFromUserSecureId(fromUserSecureId);
            chatMessageDTO.setToUserSecureId(toUserSecureId);
            chatMessageDTO.setRoomType(RoomType.valueOf(roomType));  // Convert the string to enum
            chatMessageDTO.setMessage(message);
            chatMessageDTO.setChatRoomSecureId(chatRoomSecureId);
            chatMessageDTO.setFile(file);  // Optional file

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
            logger.error("Error sending message", e);
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
                 message.getReadAt(),
                 message.getFilePath(),
                 message.getChatType()
         ));
     
         return ResponseEntity.ok(responseMessages);
     }
    
    private ResponseEntity<?> handlePrivateMessage(ChatMessageDTO chatMessageDTO, AppUser fromUser){
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
        MultipartFile file = chatMessageDTO.getFile();
        String filePath = null;
        ChatType fileType = ChatType.TEXT;
        if (file != null && !file.isEmpty()) {
            try {
                String UPLOAD_DIR = "chats/";
                filePath = FileUploadHelper.saveFile(file, UPLOAD_DIR);  // Try to save the file
                String contentType = file.getContentType();
        
                if (contentType != null) {
                    if (contentType.startsWith("image/")) {
                        fileType = ChatType.IMAGE;
                    } else if (contentType.startsWith("video/")) {
                        fileType = ChatType.VIDEO;
                    }
                }
            } catch (IOException e) {
                // Handle the exception: log the error and possibly notify the user
                logger.error("Failed to upload file", e);
                throw new RuntimeException("An error occurred while uploading the file. Please try again.");
            }
        }
        ChatMessage savedMessage = chatService.saveMessage(fromUser, toUser, chatMessageDTO.getMessage(),chatRoom,filePath,fileType );
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

          // Save and send the message
          MultipartFile file = chatMessageDTO.getFile();
          String filePath = null;
          ChatType fileType = ChatType.TEXT;
          if (file != null && !file.isEmpty()) {
            try {
                String UPLOAD_DIR = "chats/";
                filePath = FileUploadHelper.saveFile(file, UPLOAD_DIR);  // Try to save the file
                String contentType = file.getContentType();
        
                if (contentType != null) {
                    if (contentType.startsWith("image/")) {
                        fileType = ChatType.IMAGE;
                    } else if (contentType.startsWith("video/")) {
                        fileType = ChatType.VIDEO;
                    }
                }
            } catch (IOException e) {
                // Handle the exception: log the error and possibly notify the user
                logger.error("Failed to upload file", e);
                throw new RuntimeException("An error occurred while uploading the file. Please try again.");
            }
        }
        // Save and send the message (no toUser for a channel)
        ChatMessage savedMessage = chatService.saveMessage(fromUser, null, chatMessageDTO.getMessage(),chatRoom,filePath,fileType );
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
