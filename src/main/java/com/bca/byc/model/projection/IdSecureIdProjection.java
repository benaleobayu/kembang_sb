package com.bca.byc.model.projection;

import com.bca.byc.entity.AppUser;

public interface IdSecureIdProjection {
    Long getId();

    String getSecureId();

    AppUser toAppUser();
}
