package com.bca.byc.controller;

import com.bca.byc.entity.AppUser;
import com.bca.byc.entity.ChatMessage;
import com.bca.byc.entity.ChatRoom;
import com.bca.byc.enums.ChatType;
import com.bca.byc.enums.RoomType;
import com.bca.byc.service.ChatService;

import com.bca.byc.service.impl.AppUserServiceImpl;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.access.prepost.PreAuthorize;
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
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import com.bca.byc.response.ChatMessageDTO;
import com.bca.byc.response.ChatMessageResponse;
import com.bca.byc.response.CreateChatRoomChannelRequest;
import com.bca.byc.security.util.ContextPrincipal;
import com.bca.byc.service.ChatRoomService;
import com.bca.byc.service.ChatMessageService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import com.bca.byc.response.ApiDataResponse;
import com.bca.byc.util.FileUploadHelper;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/chat")
@Slf4j
@AllArgsConstructor
@Tag(name = "Chat API", description = "API for managing chat messages")
@SecurityRequirement(name = "Authorization")
@PreAuthorize("isAuthenticated()")

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
            
        String tokenSecureId  = ContextPrincipal.getSecureUserId();
        if(tokenSecureId != null && !tokenSecureId.equals(fromUserSecureId)){
            return ResponseEntity.status(403).body("You are not authorized to send messages on behalf of another user");
        }
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
     public ResponseEntity<?>  getPrivateMessages(
             @RequestParam String fromUserSecureId,
             @RequestParam String toUserSecureId,
             Pageable pageable) {
            String tokenSecureId  = ContextPrincipal.getSecureUserId();
            if(tokenSecureId != null && !tokenSecureId.equals(fromUserSecureId)){
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body("You are not authorized to view messages for another user");
            }
        

                

         // Fetch paginated ChatMessages
         Page<ChatMessage> messages = chatMessageService.getMessagesForPrivateRoom(fromUserSecureId, toUserSecureId, pageable);
        // Mark messages as read in the database
        chatMessageService.markMessagesAsRead(fromUserSecureId, toUserSecureId);
         // Map ChatMessage to ChatMessageResponse
         Page<ChatMessageResponse> responseMessages = messages.map(message -> new ChatMessageResponse(
                 message.getSecureId(),
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
     @GetMapping("/channel/{channelSecureId}/messages")
     public ResponseEntity<?> getChannelMessages(
             @PathVariable String channelSecureId,
             @RequestParam String fromUserSecureId,
             Pageable pageable) {

        String tokenSecureId  = ContextPrincipal.getSecureUserId();
        if(tokenSecureId != null && !tokenSecureId.equals(fromUserSecureId)){
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("You are not authorized to view messages for another user");
        }
 
         // Check if the chat room exists
        ChatRoom chatRoom = chatRoomService.findBySecureId(channelSecureId)
        .orElseThrow(() -> new RuntimeException("Chat room not found"));
         if (chatRoom == null) {
             return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Channel not found.");
         }
 
         // Ensure that the requesting user is part of the chat room participants
         boolean isParticipant = chatRoom.getParticipants().stream()
                 .anyMatch(participant -> participant.getSecureId().equals(fromUserSecureId));
 
         if (!isParticipant) {
             return ResponseEntity.status(HttpStatus.FORBIDDEN).body("User is not a participant of this channel.");
         }
 
         // Fetch paginated chat messages for the channel
         Page<ChatMessage> messages = chatMessageService.getMessagesForChannel(chatRoom.getSecureId(), pageable);
 
         // Mark messages as read for the user in this channel
         chatMessageService.markMessagesAsReadForChannel(fromUserSecureId,chatRoom.getSecureId());
 
         // Map ChatMessage to ChatMessageResponse
         Page<ChatMessageResponse> responseMessages = messages.map(message -> new ChatMessageResponse(
                 message.getSecureId(),
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
        
        ChatMessageResponse chatMessageResponse = new ChatMessageResponse(
            savedMessage.getSecureId(),
            savedMessage.getMessage(),
            savedMessage.getTimestamp(),
            savedMessage.getFromUser().getUsername(),
            savedMessage.getToUser() != null ? savedMessage.getToUser().getName() : null,
            savedMessage.getCreatedAt(),
            savedMessage.getReadAt(),
            savedMessage.getFilePath(),
            savedMessage.getChatType()
        );
         
        String channel = "/private/" + toUser.getSecureId() +'/'+ fromUser.getSecureId()+  "/queue/messages";
        messagingTemplate.convertAndSend(channel, chatMessageResponse);
        
        Map<String, String> dataObject = new HashMap<>();
        dataObject.put("chat_room_secure_id", chatRoom.getSecureId());
        dataObject.put("message_secure_id", savedMessage.getSecureId());
        dataObject.put("channel", channel);
        
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
              // Ensure that the requesting user is part of the chat room participants
            boolean isParticipant = chatRoom.getParticipants().stream()
            .anyMatch(participant -> participant.getSecureId().equals(chatMessageDTO.getFromUserSecureId()));
            if (!isParticipant) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body("User is not a participant of this channel.");
            }

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

        ChatMessageResponse chatMessageResponse = new ChatMessageResponse(
            savedMessage.getSecureId(),
            savedMessage.getMessage(),
            savedMessage.getTimestamp(),
            savedMessage.getFromUser().getUsername(),
            savedMessage.getToUser() != null ? savedMessage.getToUser().getName() : null,
            savedMessage.getCreatedAt(),
            savedMessage.getReadAt(),
            savedMessage.getFilePath(),
            savedMessage.getChatType()
        );
         
        String channel = "/channel/" + chatRoom.getSecureId() + "/queue/messages";
        messagingTemplate.convertAndSend(channel, chatMessageResponse);    
        Map<String, String> dataObject = new HashMap<>();
        dataObject.put("chat_room_secure_id", chatRoom.getSecureId());
        dataObject.put("message_secure_id", savedMessage.getSecureId());
        return ResponseEntity.ok(new ApiDataResponse<>(true, "Successfully create channels messages", dataObject));
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
    


    @PostMapping("/channel-room/create")
    public ResponseEntity<?> createChannelRoom(@RequestBody CreateChatRoomChannelRequest request) {
        String tokenSecureId  = ContextPrincipal.getSecureUserId();
        if(tokenSecureId != null && !tokenSecureId.equals(request.getFromUserSecureId())){
            return ResponseEntity.status(403).body("You are not authorized to send messages on behalf of another user");
        }
    

        try {
            // Fetch participants based on secure_ids from request
            List<AppUser> participants = appUserService.findUsersBySecureIds(request.getParticipantSecureIds());
            AppUser fromUser = appUserService.findBySecureId(request.getFromUserSecureId());
            // Add fromUser to the participants list if they are not already in it
            if (!participants.contains(fromUser)) {
                participants.add(fromUser);
            }
            if (participants.isEmpty()) {
                return ResponseEntity.badRequest().body("Participants not found.");
            }

            // Extract secure_ids from the participant list
            List<String> participantSecureIds = participants.stream()
                    .map(AppUser::getSecureId)
                    .collect(Collectors.toList());

            ChatRoom newRoom = chatRoomService.createRoom(
                    request.getChannelName(), 
                    participantSecureIds, 
                    RoomType.CHANNEL, 
                    request.getFromUserSecureId()
            );

            // Log and return the newly created chat room
            log.info("Created new Channel chat room: " + newRoom.getSecureId());
            Map<String, String> dataObject = new HashMap<>();
            dataObject.put("chat_room_secure_id", newRoom.getSecureId());
            return ResponseEntity.ok(new ApiDataResponse<>(true, "Successfully create Channel", dataObject));

        } catch (Exception e) {
            log.error("Error creating channel chat room", e);
            return ResponseEntity.status(500).body("Error creating chat room.");
        }
    }

    
    
    
}
