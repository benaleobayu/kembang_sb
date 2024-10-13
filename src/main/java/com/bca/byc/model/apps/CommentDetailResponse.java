package com.bca.byc.model.apps;

import lombok.Data;

@Data
public class CommentDetailResponse {

    private String id;

    private String comment;

    private Boolean status;

    private PostOwnerResponse owner;

    private String createdAt;

    private Long likesCount = 0L;

    private Long sharesCount = 0L;

    private Long commentRepliesCount = 0L;

}
