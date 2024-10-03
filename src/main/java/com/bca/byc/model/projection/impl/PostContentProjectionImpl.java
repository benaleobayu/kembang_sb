package com.bca.byc.model.projection.impl;

import com.bca.byc.model.projection.PostContentProjection;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
public class PostContentProjectionImpl implements PostContentProjection {

    private final String secureId;
    private final Long id;
    private final String content;
    private final String contentType;
    private final String thumbnail;

    @Override
    public String getSecureId() {
        return secureId;
    }

    @Override
    public Long getId() {
        return id;
    }

    @Override
    public String getContent() {
        return content;
    }

    @Override
    public String getContentType() {
        return contentType;
    }

    @Override
    public String getThumbnail() {
        return thumbnail;
    }
}
