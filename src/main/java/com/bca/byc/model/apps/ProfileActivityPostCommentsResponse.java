package com.bca.byc.model.apps;

import com.bca.byc.model.PostHomeResponse;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class ProfileActivityPostCommentsResponse {

    private String userId;
    private String userName;
    private String userAvatar;

    private PostHomeResponse post;

    private List<ListCommentResponse> comments = new ArrayList<>();
}
