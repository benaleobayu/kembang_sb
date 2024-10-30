package com.bca.byc.converter.parsing;

import com.bca.byc.entity.AppUser;
import com.bca.byc.entity.Notification;
import com.bca.byc.model.apps.NotificationCreateRequest;
import com.bca.byc.repository.NotificationRepository;

public class TreeNotification {

    public static void saveNotification(NotificationCreateRequest dto, NotificationRepository repository){
        Notification data = new Notification();
        data.setType(dto.getType());
        data.setNotifiableType(dto.getNotifiableType());
        data.setNotifiableId(dto.getNotifiableId());
        data.setMessage(dto.getMessage());
        data.setCreatedBy(dto.getCreatedBy());
        data.setUser(dto.getUserTarget());
        repository.save(data);
    }


}
