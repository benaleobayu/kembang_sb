package com.bca.byc.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.bca.byc.entity.Notification;

public interface NotificationRepository extends JpaRepository<Notification, String> {

    // Find notifications by userId with pagination
    Page<Notification> findByUserId(Long userId, Pageable pageable);
}
