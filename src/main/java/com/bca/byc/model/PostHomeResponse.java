package com.bca.byc.model;

import com.bca.byc.model.apps.PostContentDetailResponse;
import com.bca.byc.model.apps.PostOwnerResponse;
import lombok.Data;

import java.util.List;

@Data
public class PostHomeResponse {

    private String postId;
    private String postDescription;
    private String postAt;

    private List<String> postTagsList;
    private List<String> postHighlightsList; // new

    private List<PostContentDetailResponse> postContentList;
    private PostOwnerResponse postOwner;
    private PostCategoryResponse postCategory;

    // ------- attributes -------
    private Boolean isCommentable;
    private Boolean isShareable;
    private Boolean isShowLikes;
    private Boolean isPosted;
    // ------- attributes -------

    // ------- added function -------
    private Boolean isMyPost;
    private Boolean isLiked;
    private Boolean isFollowed;
    private Boolean isOfficial;
    // ------- added function -------
    private String officialUrl;
    // ------- count -------
    private Long likeCount;
    private Long commentCount;
    // ------- count -------

}
