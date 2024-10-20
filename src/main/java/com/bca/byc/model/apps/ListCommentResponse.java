package com.bca.byc.model.apps;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class ListCommentResponse {

    private String id;

    private Long index;

    private String comment;

//    private List<ListCommentReplyResponse> commentReply;

    private PostOwnerResponse owner;

    private Boolean isLike = false;

    private Boolean isOwnerPost = false;

    private Integer repliesCount = 0;

    private Integer likeCount = 0;

    private LocalDateTime createdAt;
}
