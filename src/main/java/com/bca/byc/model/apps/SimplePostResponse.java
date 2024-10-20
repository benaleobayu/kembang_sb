package com.bca.byc.model.apps;

import lombok.Data;

@Data
public class SimplePostResponse {

    private String postId;
    private String postDescription;
    private String postContent;
    private String postThumbnail;
    private String postCreatedAt;

}
