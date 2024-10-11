package com.bca.byc.model.apps;

import lombok.Data;

@Data
public class ListCommentReplyResponse {

    private String id;

    private Long index;

    private String comment;

    private PostOwnerResponse owner;

    private Boolean isLike = false;

    private Boolean isOwnerPost = false;

    private Integer likeCount = 0;

    private String createdAt;

}
