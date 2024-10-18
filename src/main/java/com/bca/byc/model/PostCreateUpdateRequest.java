package com.bca.byc.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.List;

@Data
public class PostCreateUpdateRequest {

    @NotBlank(message = "Post description is required")
    private String description;

    @NotNull(message = "Content tag is required")
    private List<String> tagName; // tag _id

    private PostLocationRequestAndResponse postLocation;

    @NotNull(message = "Post category is required")
    private String postCategoryId;

    private Boolean isCommentable;

    private Boolean isShareable;

    private Boolean isShowLikes;

    private Boolean isPosted;

}

