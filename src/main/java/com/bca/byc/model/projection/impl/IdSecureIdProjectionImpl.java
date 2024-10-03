package com.bca.byc.model.projection.impl;

import com.bca.byc.entity.AppUser;
import com.bca.byc.model.projection.IdSecureIdProjection;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class IdSecureIdProjectionImpl implements IdSecureIdProjection {

    private Long id;
    private String secureId;

    @Override
    public Long getId() {
        return id;
    }

    @Override
    public String getSecureId() {
        return secureId;
    }

    @Override
    public AppUser toAppUser() {
        AppUser appUser = new AppUser();
        appUser.setId(id);
        appUser.setSecureId(secureId);
        return appUser;
    }
}
