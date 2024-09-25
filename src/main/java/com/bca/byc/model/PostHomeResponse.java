package com.bca.byc.model;

import com.bca.byc.model.apps.PostOwnerResponse;
import lombok.Data;

import java.util.List;

@Data
public class PostHomeResponse {

    private Long postId;

    private String postDescription;

    private List<PostContent> postContentList;

    private List<String> postTagsList;

    private PostOwnerResponse postOwner;

    @Data
    public static class PostContent {

        private Long contentId;

        private String content;

        private String type;

        private String thumbnail;
    }

}
