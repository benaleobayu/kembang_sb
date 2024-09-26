package com.bca.byc.model.apps;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ListCommentResponse {

    private String id;

    private String comment;

    private List<ListCommentReplyResponse> commentReply;

    private OwnerDataResponse owner;

    private String createdAt;
}
