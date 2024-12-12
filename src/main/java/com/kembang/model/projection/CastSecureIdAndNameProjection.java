package com.kembang.model.projection;

import com.kembang.entity.impl.AttrIdentificable;

public interface CastSecureIdAndNameProjection extends AttrIdentificable {

    String getSecureId();

    String getName();
}
