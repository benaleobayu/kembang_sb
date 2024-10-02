package com.bca.byc.model.apps;

import lombok.Data;

@Data
public class ListCommentReplyResponse {

    private String id;

    private Integer index;

    private String comment;

    private OwnerDataResponse owner;

    private String createdAt;

}
