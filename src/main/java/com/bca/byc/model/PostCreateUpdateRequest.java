package com.bca.byc.model;

import jakarta.persistence.Transient;
import lombok.Data;

import java.util.List;

@Data
public class PostCreateUpdateRequest {

    private String description;

    private List<String> tagName; // tag _id

    // can input multiple users
    private List<Long> tagUserIds; // user _id

//    private PostLocationRequest postLocation;

    private Long postCategoryId;

    private Boolean isCommentable;

    private Boolean isShareable;

    private Boolean isShowLikes;


    // not to request input

    @Transient
    private String content;

    @Transient
    private String type;

    @Data
    public static class PostLocationRequest {

        private String name;

        private String url;

        private String geoLocation;
    }
}
