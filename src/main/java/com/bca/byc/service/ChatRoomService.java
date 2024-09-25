package com.bca.byc.service;

import com.bca.byc.entity.AppUser;
import com.bca.byc.entity.ChatRoom;
import com.bca.byc.entity.ChatRoomUser;
import com.bca.byc.enums.RoomType;
import com.bca.byc.repository.ChatRoomRepository;
import com.bca.byc.repository.auth.AppUserRepository;
import com.bca.byc.repository.ChatRoomUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.springframework.transaction.annotation.Transactional;
@Service
public class ChatRoomService {

    @Autowired
    private ChatRoomRepository chatRoomRepository;

    @Autowired
    private AppUserRepository appUserRepository;  // Replaced UserRepository with AppUserRepository
    @Autowired
    private ChatRoomUserRepository chatRoomUserRepository;  // Replaced UserRepository with AppUserRepository

    /**
     * Creates a new chat room with the provided name and participants.
     * 
     * @param roomName    The name of the chat room.
     * @param participantSecureIds The list of secure IDs of users who will participate in the chat room.
     * @return The newly created ChatRoom.
     */
    @Transactional // This ensures the entire method is executed within a single transaction
    public ChatRoom createRoom(String roomName, List<String> participantSecureIds, RoomType roomType, String creatorSecureId) {
        // Fetch participants based on their secure IDs
        List<AppUser> participants = appUserRepository.findBySecureIdIn(participantSecureIds);
    
        // Fetch the creator from AppUserRepository
        AppUser creator = appUserRepository.findBySecureId(creatorSecureId)
                .orElseThrow(() -> new RuntimeException("Creator not found"));
    
        // Create a new ChatRoom entity
        ChatRoom chatRoom = new ChatRoom();
        chatRoom.setRoomName(roomName);
        chatRoom.setType(roomType);
        chatRoom.setCreatedAt(LocalDateTime.now());
        chatRoom.setCreator(creator);  // Set the creator in ChatRoom
       // chatRoom.setParticipants(participants); // Set participants as List<AppUser>
    
        // Save the chat room first
        ChatRoom savedChatRoom = chatRoomRepository.save(chatRoom);
    
        // Create and save ChatRoomUser entities for each participant
        List<ChatRoomUser> chatRoomUsers = new ArrayList<>();
        for (AppUser user : participants) {
            ChatRoomUser chatRoomUser = new ChatRoomUser();
            chatRoomUser.setChatRoom(savedChatRoom);
            chatRoomUser.setAppUser(user);
            chatRoomUser.setCreator(user.getSecureId().equals(creatorSecureId));  // Mark the creator
            chatRoomUsers.add(chatRoomUser);
        }
    
        // Save ChatRoomUser entities
        chatRoomUserRepository.saveAll(chatRoomUsers);
    
        return savedChatRoom;
    }
    

    /**
     * Retrieves all chat rooms for a particular user based on their secure ID.
     * 
     * @param userSecureId The secure ID of the user.
     * @return A list of chat rooms the user is part of.
     */
    public List<ChatRoom> getUserChatRooms(String userSecureId) {
        return chatRoomRepository.findByParticipantSecureId(userSecureId);
    }
    
    public Optional<ChatRoom> findRoomByParticipants(String fromSecureId, String toSecureId) {
        return chatRoomRepository.findByParticipantsSecureIds(fromSecureId, toSecureId);
    }

    public List<ChatRoom> findRoomsByUser(String userSecureId) {
        return chatRoomRepository.findByParticipantSecureId(userSecureId);
    }

    public Optional<ChatRoom> findBySecureId(String secureId) {
        return chatRoomRepository.findBySecureId(secureId);
    }
       // New method to find by type and secureId
    public Optional<ChatRoom> findByTypeAndSecureId(RoomType type, String secureId) {
        return chatRoomRepository.findByTypeAndSecureId(type, secureId);
    }
      // Method to find a chat room by participants and type
    public Optional<ChatRoom> findRoomByParticipantTypes(String fromSecureId, String toSecureId, RoomType roomType) {
        return chatRoomRepository.findByParticipantsSecureIdsAndType(fromSecureId, toSecureId, roomType);
    }
}
