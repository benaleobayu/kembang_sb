package com.bca.byc.model.apps;

import lombok.Data;

@Data
public class CommentDetailResponse {

    private String id;

    private String comment;

    private Boolean status;

    private OwnerDataResponse owner;

    private String createdAt;

    private Long likesCount;

    private Long sharesCount;

    private Long commentRepliesCount;

}
