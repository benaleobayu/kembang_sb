package com.bca.byc.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.bca.byc.entity.ChatRoomUser;

import java.util.List;
import java.util.Optional;


@Repository
public interface ChatRoomUserRepository extends JpaRepository<ChatRoomUser, Long> {
    // You can add custom query methods here if needed
    Optional<ChatRoomUser> findByChatRoomSecureIdAndAppUserSecureIdAndIsAdminTrue(String chatRoomSecureId, String appUserSecureId);
    List<ChatRoomUser> findByChatRoomSecureIdAndAppUserSecureIdIn(String chatRoomSecureId, List<String> appUserSecureIds);

    List<ChatRoomUser> findByChatRoomSecureId(String chatRoomSecureId);
    Optional<ChatRoomUser> findByChatRoomSecureIdAndAppUserSecureId(String chatRoomSecureId, String appUserSecureId);


}
