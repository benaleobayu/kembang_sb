package com.bca.byc.repository;

import com.bca.byc.entity.AppUser;
import com.bca.byc.entity.ChatRoom;
import com.bca.byc.entity.ChatRoomUser;
import com.bca.byc.enums.RoomType;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ChatRoomRepository extends JpaRepository<ChatRoom, Long> {

    @Query("SELECT r FROM ChatRoom r JOIN r.participants p1 JOIN r.participants p2 WHERE p1.secureId = :fromSecureId AND p2.secureId = :toSecureId")
    Optional<ChatRoom> findByParticipantsSecureIds(String fromSecureId, String toSecureId);

    @Query("SELECT r FROM ChatRoom r JOIN r.participants p WHERE p.secureId = :userSecureId")
    List<ChatRoom> findByParticipantSecureId(String userSecureId);
    Optional<ChatRoom> findBySecureId(String secureId);

      // Add the method to find by type and secureId
    Optional<ChatRoom> findByTypeAndSecureId(RoomType type, String secureId);


     // Query to find chat room by participants' secure IDs and type
     @Query("SELECT cr FROM ChatRoom cr JOIN cr.participants p1 JOIN cr.participants p2 " +
     "WHERE p1.secureId = :fromSecureId AND p2.secureId = :toSecureId AND cr.type = :roomType")
     Optional<ChatRoom> findByParticipantsSecureIdsAndType(String fromSecureId, String toSecureId, RoomType roomType);

    @Query("SELECT c.participants FROM ChatRoom c WHERE c.secureId = :secureId")
    List<ChatRoomUser> findParticipantsByChatRoomSecureId(@Param("secureId") String secureId);

}
