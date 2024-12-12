package com.kembang.converter.parsing;

import com.kembang.entity.AppAdmin;
import com.kembang.entity.LogRequest;
import com.kembang.model.LogGeneralRequest;
import com.kembang.repository.LogRequestRepository;

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
