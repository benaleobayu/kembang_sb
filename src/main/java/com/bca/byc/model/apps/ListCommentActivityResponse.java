package com.bca.byc.model.apps;

import lombok.Data;

import java.util.List;

@Data
public class ListCommentActivityResponse {

    private String id;

    private Long index;

    private String comment;

    private List<ListCommentReplyResponse> commentReply;

    private PostOwnerResponse owner;

    private Boolean isLike = false;

    private Boolean isOwnerPost = false;

    private Integer repliesCount = 0;

    private Integer likeCount = 0;

    private String createdAt;
}
