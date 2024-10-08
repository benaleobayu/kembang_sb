package com.bca.byc.converter.parsing;

import com.bca.byc.entity.AppUser;
import com.bca.byc.entity.PreRegister;
import com.bca.byc.entity.UserManagementLog;
import com.bca.byc.enums.LogStatus;
import com.bca.byc.repository.LogUserManagementRepository;

public class TreeLogUserManagement {

    public static void logUserManagement(PreRegister preRegister, AppUser user, LogStatus logStatus, String message, LogUserManagementRepository repository) {

        UserManagementLog log = new UserManagementLog();
        log.setStatus(logStatus);
        log.setPreRegister(preRegister);
        log.setUser(user);
        log.setMessage(message);
        repository.save(log);
    }
}
