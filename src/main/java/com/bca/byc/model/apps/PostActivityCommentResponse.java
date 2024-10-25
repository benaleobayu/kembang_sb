package com.bca.byc.model.apps;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class PostActivityCommentResponse {

    private String postId;
    private String postThumbnail;
    private String postDescription;
    private String postAt;
}
