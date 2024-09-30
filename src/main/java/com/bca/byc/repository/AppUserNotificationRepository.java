package com.bca.byc.repository;

import com.bca.byc.entity.AppUserNotification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AppUserNotificationRepository extends JpaRepository<AppUserNotification, Long> {
    // Custom query method to find notification settings by appUserId
    Optional<AppUserNotification> findByAppUserId(Long appUserId);
}
