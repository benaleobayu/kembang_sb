package com.bca.byc.model.apps;

import lombok.Data;

@Data
public class ListCommentResponse {

    private String comment;

    private OwnerDataResponse owner;

    private String createdAt;
}
