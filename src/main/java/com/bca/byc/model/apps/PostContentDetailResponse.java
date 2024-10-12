package com.bca.byc.model.apps;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class PostContentDetailResponse {

    private String contentId;
    private String content;
    private String contentType;
    private String thumbnail;

    private List<OwnerDataResponse> contentTagsUser = new ArrayList<>();

    public PostContentDetailResponse() {
    }

    public PostContentDetailResponse(String contentId, String content, String contentType, String thumbnail) {
        this.contentId = contentId;
        this.content = content;
        this.contentType = contentType;
        this.thumbnail = thumbnail;
    }

    public PostContentDetailResponse(String contentId, String content, String contentType, String thumbnail, List<OwnerDataResponse> contentTagsUser) {
        this.contentId = contentId;
        this.content = content;
        this.contentType = contentType;
        this.thumbnail = thumbnail;
        this.contentTagsUser = contentTagsUser;
    }
}
