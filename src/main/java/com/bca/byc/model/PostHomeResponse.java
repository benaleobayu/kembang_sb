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
    private PostLocationRequestAndResponse postLocation;

    // ------- attributes -------
    private Boolean isCommentable = false;
    private Boolean isShareable = false;
    private Boolean isShowLikes = false;
    private Boolean isPosted = false;
    // ------- attributes -------

    // ------- added function -------
    private Boolean isMyPost = false;
    private Boolean isLiked = false;
    private Boolean isSaved = false;
    private Boolean isFollowed = false;
    private Boolean isOfficial = false;
    // ------- added function -------
    private String officialUrl;
    // ------- count -------
    private Long likeCount = 0L;
    private Long commentCount = 0L;
    // ------- count -------

}
