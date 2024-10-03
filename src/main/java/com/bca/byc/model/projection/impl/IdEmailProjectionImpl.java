package com.bca.byc.model.projection.impl;

import com.bca.byc.model.projection.IdEmailProjection;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class IdEmailProjectionImpl implements IdEmailProjection {

    private final Long id;
    private final String email;

    @Override
    public Long getId() {
        return id;
    }

    @Override
    public String getEmail() {
        return email;
    }
}
