package com.bca.byc.repository;

import com.bca.byc.entity.AppUser;
import com.bca.byc.entity.ChatMessage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;
import java.util.List;
@Repository
public interface ChatMessageRepository extends JpaRepository<ChatMessage, Long> {

    // Fetch all messages between two users
    List<ChatMessage> findByFromUserAndToUserOrToUserAndFromUserOrderByTimestamp(
        AppUser fromUser, AppUser toUser, AppUser reverseToUser, AppUser reverseFromUser);

    @Query("SELECT m FROM ChatMessage m " +
    "JOIN m.chatRoom r " +
    "JOIN ChatRoomUser cru1 ON r = cru1.chatRoom " +
    "JOIN ChatRoomUser cru2 ON r = cru2.chatRoom " +
    "WHERE ((cru1.appUser.secureId = :fromSecureId AND cru2.appUser.secureId = :toSecureId) " +
    "OR (cru1.appUser.secureId = :toSecureId AND cru2.appUser.secureId = :fromSecureId)) " +
    "AND r.type = com.bca.byc.enums.RoomType.PRIVATE " +
    "ORDER BY m.timestamp DESC")
    Page<ChatMessage> findMessagesByPrivateRoomAndParticipants(@Param("fromSecureId") String fromSecureId,
    @Param("toSecureId") String toSecureId,
    Pageable pageable);

    @Modifying
    @Transactional
    @Query("UPDATE ChatMessage m SET m.readAt = :readAt " +
        "WHERE m.toUser.secureId = :fromSecureId AND m.fromUser.secureId = :toUserSecureId " +
        "AND m.readAt IS NULL")
    int updateReadAtForMessages(@Param("fromSecureId") String fromSecureId,
                                @Param("toUserSecureId") String toUserSecureId,
                                @Param("readAt") LocalDateTime readAt);

        // Find all messages for a specific channel using room_secure_id
        @Query("SELECT m FROM ChatMessage m WHERE m.chatRoom.secureId = :roomSecureId")
        Page<ChatMessage> findByRoomSecureId(@Param("roomSecureId") String roomSecureId, Pageable pageable);
                                

        @Modifying
        @Transactional
        @Query("UPDATE ChatMessage m SET m.readAt = :readAt " +
                "WHERE m.chatRoom.secureId = :channelSecureId AND m.readAt IS NULL " +
                "AND m.toUser.secureId = :fromSecureId")
        int updateReadAtForChannelMessages(
                                            @Param("fromSecureId") String fromSecureId,
                                            @Param("channelSecureId") String channelSecureId,
                                            @Param("readAt") LocalDateTime readAt);
    // Query messages by chatRoom's secureId and order by timestamp
   // List<ChatMessage> findByChatRoomSecureIdOrderByTimestamp(String secureId);
}
