package com.bca.byc.model;

import com.bca.byc.model.apps.PostContentDetailResponse;
import com.bca.byc.model.apps.PostOwnerResponse;
import lombok.Data;

import java.util.List;

@Data
public class PostHomeResponse {

    private Long postId;

    private String postDescription;

    private List<PostContentDetailResponse> postContentList;

    private List<String> postTagsList;

    private PostOwnerResponse postOwner;

}
