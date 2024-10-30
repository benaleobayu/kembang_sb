package com.bca.byc.model.apps;

import com.bca.byc.entity.AppUser;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class NotificationCreateRequest {

    private String type;

    private String notifiableType;

    private String notifiableId;

    private String message;

    private Long createdBy;

    private AppUser userTarget;

}
