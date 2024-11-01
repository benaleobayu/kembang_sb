package com.bca.byc.model.projection;

public interface BusinessExportProjection {
    Long getId();
    String getName();
    String getAddress();
    String getLineOfBusiness();
    Boolean getIsPrimary();
    String getUserEmail();
    String getUserName();
    String getUserCin();
}