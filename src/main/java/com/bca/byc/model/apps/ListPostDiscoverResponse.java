package com.bca.byc.model.apps;

import com.bca.byc.model.PostCategoryResponse;
import com.bca.byc.model.PostLocationRequestAndResponse;
import lombok.Data;

import java.util.List;

@Data
public class ListPostDiscoverResponse {

    private String postId;
    private String postDescription;
    private String postAt;

    private List<String> postTagsList;
    private List<String> postHighlightsList; // new

    private List<PostContentDetailResponse> postContentList;
    private PostOwnerResponse postOwner;

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
    private Boolean isOfficial = true;
    // ------- added function -------
    private String officialUrl;
    // ------- count -------
    private Long likeCount = 0L;
    private Long commentCount = 0L;
    // ------- count -------
}
