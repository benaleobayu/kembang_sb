package com.bca.byc.model;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.List;

@Data
public class PostCreateUpdateRequest {

    private String description;

    @NotNull(message = "Content tag is required")
    private List<String> tagName; // tag _id

    private PostLocationRequest postLocation;

    @NotNull(message = "Post category is required")
    private String postCategoryId;

    private Boolean isCommentable;

    private Boolean isShareable;

    private Boolean isShowLikes;

    private Boolean isPosted;

    @Data
    public static class PostLocationRequest {

        private String placeName;

        private String description;

        private String placeId;

        private Double longitude;

        private Double latitude;
    }
}
