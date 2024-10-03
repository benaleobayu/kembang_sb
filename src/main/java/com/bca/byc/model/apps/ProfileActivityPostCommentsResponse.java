package com.bca.byc.model.apps;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class ProfileActivityPostCommentsResponse {

    private String userId;
    private String userName;
    private String userAvatar;

    private SimplePostResponse post;

    private List<ListCommentResponse> comments = new ArrayList<>();
}
