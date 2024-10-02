package com.bca.byc.service;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import com.bca.byc.entity.Notification;
import  com.bca.byc.repository.NotificationRepository;
@Service
public class NotificationService {

    @Autowired
    private NotificationRepository notificationRepository;

    public Page<Notification> getNotificationsByUserId(Long userId, Pageable pageable) {
        return notificationRepository.findByUserId(userId, pageable);
    }
}
