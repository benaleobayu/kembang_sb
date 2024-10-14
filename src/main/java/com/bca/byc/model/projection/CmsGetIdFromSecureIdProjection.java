package com.bca.byc.model.projection;

public interface CmsGetIdFromSecureIdProjection {
    Long getId();
    String getSecureId();
    boolean getIsSuspended();
}
