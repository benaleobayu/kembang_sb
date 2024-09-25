package com.bca.byc.model.apps;

import lombok.Data;

import java.util.List;

@Data
public class ListCommentResponse {

    private String comment;

    private List<ListCommentReplyResponse> commentReply;

    private OwnerDataResponse owner;

    private String createdAt;
}
