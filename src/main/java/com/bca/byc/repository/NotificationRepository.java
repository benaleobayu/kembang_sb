package com.bca.byc.repository;

import com.bca.byc.entity.Notification;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface NotificationRepository extends JpaRepository<Notification, String> {

    // Find notifications by userId with pagination
    Page<Notification> findByUserId(Long userId, Pageable pageable);

    @Query("""
            SELECT n From Notification n
            WHERE
            (LOWER(n.message) LIKE LOWER(:keyword) ) AND
            n.user.id = :id
            """)
    Page<Notification> findByUserIdAndKeyword(@Param("id") Long id, @Param("keyword") String keyword, Pageable pageable);
}
