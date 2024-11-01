package com.bca.byc.controller;

import com.bca.byc.entity.AppUser;
import com.bca.byc.entity.ChatMessage;
import com.bca.byc.entity.ChatRoom;
import com.bca.byc.entity.ChatRoomUser;
import com.bca.byc.enums.ChatType;
import com.bca.byc.enums.RoomType;
import com.bca.byc.exception.ResourceNotFoundException;
import com.bca.byc.exception.UnauthorizedException;
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
import com.bca.byc.repository.ChatMessageRepository;
import java.util.Collections;
import com.bca.byc.repository.ChatRoomRepository;
import com.bca.byc.repository.ChatRoomUserRepository;
import com.bca.byc.response.UpdateChatRoomChannelRequest;
import com.bca.byc.response.KickParticipantsRequest;


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
    ChatMessageRepository chatMessageRepository;

    @Autowired
    ChatRoomRepository chatRoomRepository;


    @Autowired
    ChatRoomUserRepository chatRoomUserRepository;    
    
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
        @RequestPart(value = "file", required = false) MultipartFile file,
        @Parameter(description = "Optional parent message secure ID for replies", required = false) @RequestPart(value = "chatMessageParentSecureId", required = false) String chatMessageParentSecureId) {
            
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
              // Skip validation of parent message, directly assign if provided
            if (chatMessageParentSecureId != null && !chatMessageParentSecureId.isEmpty()) {
                chatMessageDTO.setParentMessageSecureId(chatMessageParentSecureId);
            }


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


    @DeleteMapping("/chat/delete/{secureId}")
    public ResponseEntity<?> deleteData(@PathVariable String secureId) throws Exception {
        String tokenSecureId = ContextPrincipal.getSecureUserId();

        // Example logic for checking and deleting chat messages
        ChatMessage chatMessage = chatMessageRepository.findBySecureId(secureId);
        if (chatMessage == null) {
            throw new ResourceNotFoundException("Chat Message not found");
        }

        if (!chatMessage.getFromUser().getSecureId().equals(tokenSecureId)) {
            throw new UnauthorizedException("You are not authorized to delete this message");
        }

        chatMessageRepository.deleteBySecureId(secureId);

        // Returning success response as JSON using Collections.singletonMap
        return ResponseEntity.ok(Collections.singletonMap("message", "Chat message successfully deleted"));
    }

    @PutMapping("/chat/edit/{secureId}")
    public ResponseEntity<?> editData(@PathVariable String secureId, @RequestBody String message) throws Exception {
        String tokenSecureId = ContextPrincipal.getSecureUserId();

        // Find the chat message by secureId
        ChatMessage chatMessage = chatMessageRepository.findBySecureId(secureId);
        if (chatMessage == null) {
            throw new ResourceNotFoundException("Chat Message not found");
        }

        // Ensure the user editing the message is the sender of the message
        if (!chatMessage.getFromUser().getSecureId().equals(tokenSecureId)) {
            throw new UnauthorizedException("You are not authorized to edit this message");
        }

        // Update the message content
        chatMessage.setMessage(message);
        chatMessageRepository.save(chatMessage); // Persist the changes

        // Returning success response as JSON using Collections.singletonMap
        return ResponseEntity.ok(Collections.singletonMap("message", "Chat message successfully updated"));
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
                 message.getChatType(),
                 message.getParentMessage()
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
                 message.getChatType(),
                 message.getParentMessage()
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
        ChatMessage parentMessage = null;
        if (chatMessageDTO.getParentMessageSecureId() != null && !chatMessageDTO.getParentMessageSecureId().isEmpty()) {
            parentMessage = chatMessageRepository.findBySecureId(chatMessageDTO.getParentMessageSecureId());
        }

        ChatMessage savedMessage = chatService.saveMessage(fromUser, toUser, chatMessageDTO.getMessage(),chatRoom,filePath,fileType,parentMessage  );
        
        ChatMessageResponse chatMessageResponse = new ChatMessageResponse(
            savedMessage.getSecureId(),
            savedMessage.getMessage(),
            savedMessage.getTimestamp(),
            savedMessage.getFromUser().getUsername(),
            savedMessage.getToUser() != null ? savedMessage.getToUser().getName() : null,
            savedMessage.getCreatedAt(),
            savedMessage.getReadAt(),
            savedMessage.getFilePath(),
            savedMessage.getChatType(),
            savedMessage.getParentMessage()
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
          ChatMessage parentMessage = null;
          if (chatMessageDTO.getParentMessageSecureId() != null && !chatMessageDTO.getParentMessageSecureId().isEmpty()) {
              parentMessage = chatMessageRepository.findBySecureId(chatMessageDTO.getParentMessageSecureId());
          }


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
        ChatMessage savedMessage = chatService.saveMessage(fromUser, null, chatMessageDTO.getMessage(),chatRoom,filePath,fileType,parentMessage );

        ChatMessageResponse chatMessageResponse = new ChatMessageResponse(
            savedMessage.getSecureId(),
            savedMessage.getMessage(),
            savedMessage.getTimestamp(),
            savedMessage.getFromUser().getUsername(),
            savedMessage.getToUser() != null ? savedMessage.getToUser().getName() : null,
            savedMessage.getCreatedAt(),
            savedMessage.getReadAt(),
            savedMessage.getFilePath(),
            savedMessage.getChatType(),
            savedMessage.getParentMessage()
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
    
                ChatRoom newRoom = chatRoomService.createRoom(
                   "Private Chat",
                    "",  // Add description
                    participantSecureIds,
                    RoomType.PRIVATE,
                    fromUser.getSecureId(),
                    true,  // Add isNotifications
                    true    // Add isAdminMessage
            );
            
       // ChatRoom newRoom = chatRoomService.createRoom("Private Chat", participantSecureIds, RoomType.PRIVATE, fromUser.getSecureId(),);
        log.info("Created new private chat room: " + newRoom.getSecureId());
        return newRoom;
    }
    


    @PostMapping("/channel-room/create")
    public ResponseEntity<?> createChannelRoom(@RequestBody CreateChatRoomChannelRequest request) {
        String tokenSecureId = ContextPrincipal.getSecureUserId();
        if (tokenSecureId != null && !tokenSecureId.equals(request.getFromUserSecureId())) {
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

            // Create the chat room with additional fields: description, isNotifications, and isAdminMessage
            ChatRoom newRoom = chatRoomService.createRoom(
                    request.getChannelName(),
                    request.getDescription(),  // Add description
                    participantSecureIds,
                    RoomType.CHANNEL,
                    request.getFromUserSecureId(),
                    request.isNotifications(),  // Add isNotifications
                    request.isAdminMessage()    // Add isAdminMessage
            );

            // Log and return the newly created chat room
            log.info("Created new Channel chat room: " + newRoom.getSecureId());
            Map<String, String> dataObject = new HashMap<>();
            dataObject.put("chat_room_secure_id", newRoom.getSecureId());
            return ResponseEntity.ok(new ApiDataResponse<>(true, "Successfully created Channel", dataObject));

        } catch (Exception e) {
            log.error("Error creating channel chat room", e);
            return ResponseEntity.status(500).body("Error creating chat room.");
        }
    }


    @PutMapping("/channel-room/edit/{secureId}")
    public ResponseEntity<?> editChannelRoom(@PathVariable String secureId, @RequestBody UpdateChatRoomChannelRequest request) {
        String tokenSecureId = ContextPrincipal.getSecureUserId();
        
        if (tokenSecureId != null && !tokenSecureId.equals(request.getFromUserSecureId())) {
            return ResponseEntity.status(403).body("You are not authorized to update this chat room on behalf of another user");
        }
        boolean isAdmin = chatRoomUserRepository
        .findByChatRoomSecureIdAndAppUserSecureIdAndIsAdminTrue(secureId, tokenSecureId)
        .isPresent();

        if (!isAdmin) {
            return ResponseEntity.status(403).body("You are not authorized to update this chat room as you are not an admin.");
        }
        try {
            // Fetch the existing chat room by its secure ID
            ChatRoom existingRoom = chatRoomService.findBySecureId(secureId)
                    .orElseThrow(() -> new RuntimeException("Chat room not found"));

            // Update fields in the chat room
            existingRoom.setRoomName(request.getChannelName());
            existingRoom.setDescription(request.getDescription());
            existingRoom.setNotifications(request.isNotifications());
            existingRoom.setAdminMessage(request.isAdminMessage());

            // Update participants if provided
            if (request.getParticipantSecureIds() != null && !request.getParticipantSecureIds().isEmpty()) {
                List<AppUser> participants = appUserService.findUsersBySecureIds(request.getParticipantSecureIds());
                if (participants.isEmpty()) {
                    return ResponseEntity.badRequest().body("Participants not found.");
                }
                existingRoom.setParticipants(participants);
            }

            // Save the updated chat room
            ChatRoom updatedRoom = chatRoomRepository.save(existingRoom);

            // Log and return the updated chat room
            log.info("Updated Channel chat room: " + updatedRoom.getSecureId());
            Map<String, String> dataObject = new HashMap<>();
            dataObject.put("chat_room_secure_id", updatedRoom.getSecureId());
            return ResponseEntity.ok(new ApiDataResponse<>(true, "Successfully updated Channel", dataObject));

        } catch (Exception e) {
            log.error("Error updating channel chat room", e);
            return ResponseEntity.status(500).body("Error updating chat room.");
        }
    }



    @PutMapping("/channel-room/kicks/{secureId}")
    public ResponseEntity<?> kickParticipants(@PathVariable String secureId, @RequestBody KickParticipantsRequest request) {
        String tokenSecureId = ContextPrincipal.getSecureUserId();

        // Authorization check: Only allow if the user is the requester or an admin
        if (tokenSecureId != null && !tokenSecureId.equals(request.getFromUserSecureId())) {
            return ResponseEntity.status(403).body("You are not authorized to modify this chat room on behalf of another user");
        }

        // Check if the user is an admin in this chat room
        boolean isAdmin = chatRoomUserRepository
                .findByChatRoomSecureIdAndAppUserSecureIdAndIsAdminTrue(secureId, tokenSecureId)
                .isPresent();

        if (!isAdmin) {
            return ResponseEntity.status(403).body("You are not authorized to modify this chat room as you are not an admin.");
        }

        try {
            // Fetch the existing chat room by its secure ID
            ChatRoom existingRoom = chatRoomService.findBySecureId(secureId)
                    .orElseThrow(() -> new RuntimeException("Chat room not found"));

            // Remove specified participants
             List<String> kickParticipantSecureIds = request.getKickParticipantSecureIds();
            if (kickParticipantSecureIds != null && !kickParticipantSecureIds.isEmpty()) {
                // Retrieve and delete ChatRoomUser entries for participants to be removed
                List<ChatRoomUser> usersToRemove = chatRoomUserRepository.findByChatRoomSecureIdAndAppUserSecureIdIn(secureId, kickParticipantSecureIds);
                chatRoomUserRepository.deleteAll(usersToRemove);
            }

            // Save the updated chat room
            ChatRoom updatedRoom = chatRoomRepository.save(existingRoom);

            // Log and return the updated chat room
            log.info("Updated Channel chat room after kicking participants: " + updatedRoom.getSecureId());
            Map<String, String> dataObject = new HashMap<>();
            dataObject.put("chat_room_secure_id", updatedRoom.getSecureId());
            return ResponseEntity.ok(new ApiDataResponse<>(true, "Successfully removed participants from Channel", dataObject));

        } catch (Exception e) {
            log.error("Error updating channel chat room", e);
            return ResponseEntity.status(500).body("Error removing participants from chat room.");
        }
    }


    
    
    
}
