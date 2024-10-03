package com.bca.byc.model.projection;

public interface PostContentProjection {

    String getSecureId();

    Long getId();

    String getContent();

    String getContentType();

    String getThumbnail();
}
