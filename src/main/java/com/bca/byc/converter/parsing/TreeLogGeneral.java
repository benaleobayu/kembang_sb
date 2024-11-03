package com.bca.byc.converter.parsing;

import com.bca.byc.entity.AppAdmin;
import com.bca.byc.entity.LogRequest;
import com.bca.byc.model.LogGeneralRequest;
import com.bca.byc.repository.LogRequestRepository;

public class TreeLogGeneral {

    public static void savingToLogGeneral(
            LogRequestRepository repository,
            AppAdmin admin,
            LogGeneralRequest r
    ){

        LogRequest log = new LogRequest();
        log.setModelId(r.getModelId());
        log.setModelType(r.getModelType());
        log.setNameCreatedBy(admin.getName());
        log.setNote(r.getNote());
        log.setLogFrom(r.getLogFrom());
        log.setLogTo(r.getLogTo());
        log.setCreatedBy(admin.getId());

        repository.save(log);
    }

}
