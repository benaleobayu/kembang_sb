package com.bca.byc.model.apps;

import lombok.Data;

import java.util.List;

@Data
public class ListCommentResponse {

    private String id;

    private Long index;

    private String comment;

//    private List<ListCommentReplyResponse> commentReply;

    private PostOwnerResponse owner;

    private Boolean isLike = false; // TODO : integrate data

    private Boolean isOwnerPost = false; // TODO : integrate data

    private Integer repliesCount = 0; // TODO : integrate data

    private Integer likeCount = 0; // TODO : integrate data

    private String createdAt;
}
