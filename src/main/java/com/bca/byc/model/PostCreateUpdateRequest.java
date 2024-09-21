package com.bca.byc.model;

import lombok.Data;

import java.util.List;

@Data
public class PostCreateUpdateRequest {

    private String description;

    private List<String> tagName; // tag _id

    // can input multiple users
//    private List<Long> tagUserIds; // user _id

    private PostLocationRequest postLocation;

    private Long postCategoryId;

    private Boolean isCommentable;

    private Boolean isShareable;

    private Boolean isShowLikes;

    private Boolean isPosted;


    // not to request input

    @Data
    public static class PostLocationRequest {

        private String placeName;

        private String description;

        private String placedId;

        private Double longitude;

        private Double latitude;
    }
}
