package com.bca.byc.model.projection;

import com.bca.byc.entity.impl.AttrIdentificable;

public interface CastSecureIdAndNameProjection extends AttrIdentificable {

    String getSecureId();

    String getName();
}
