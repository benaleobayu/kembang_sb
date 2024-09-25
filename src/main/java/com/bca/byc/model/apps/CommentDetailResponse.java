package com.bca.byc.model.apps;

import lombok.Data;

@Data
public class CommentDetailResponse {

    private Long id;

    private String content;

    private Boolean status;

    private PostOwnerResponse owner;

    private Long likesCount;

    private Long sharesCount;

    private Long commentsCount;

}
