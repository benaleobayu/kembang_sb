package com.bca.byc.model.apps;

import lombok.Data;

@Data
public class ListCommentReplyResponse {

    private String comment;

    private OwnerDataResponse owner;

}
