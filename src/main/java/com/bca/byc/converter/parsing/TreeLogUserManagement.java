package com.bca.byc.converter.parsing;

import com.bca.byc.entity.AppAdmin;
import com.bca.byc.entity.AppUser;
import com.bca.byc.entity.PreRegister;
import com.bca.byc.entity.UserManagementLog;
import com.bca.byc.enums.LogStatus;
import com.bca.byc.model.LogUserManagementRequest;
import com.bca.byc.repository.LogUserManagementRepository;

import java.time.LocalDateTime;

public class TreeLogUserManagement {

    public static void logUserManagement(PreRegister preRegister,
                                         AppUser user,
                                         AppAdmin admin,
                                         LogStatus status,
                                         LogUserManagementRequest dto,
                                         LogUserManagementRepository repository) {

        UserManagementLog log = new UserManagementLog();
        log.setStatus(status);
        log.setPreRegister(preRegister);
        log.setUser(user);
        log.setMessage(dto.getReason());
        log.setType(dto.getType());
        log.setCreatedAt(LocalDateTime.now());
        log.setUpdatedAt(LocalDateTime.now());
        log.setUpdatedBy(admin);
        repository.save(log);
    }

}
