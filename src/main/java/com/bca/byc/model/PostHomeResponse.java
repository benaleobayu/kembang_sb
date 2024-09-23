package com.bca.byc.model;

import lombok.Data;

import java.util.List;

@Data
public class PostHomeResponse {

    private Long id;

    private String description;

    private List<PostContent> posts;

    private List<TagResponse> tags;

    private AppUserResponse appUser;

    @Data
    private static class AppUserResponse {
        private String name;
    }

    @Data
    private static class TagResponse {
        private String name;
    }

    @Data
    private static class PostContent {

        private Long id;

        private String content;

        private String type;

        private String description;

        private String thumbnail;
    }

}
