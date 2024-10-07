package com.bca.byc.model;

import com.bca.byc.model.apps.PostContentDetailResponse;
import com.bca.byc.model.apps.PostOwnerResponse;
import lombok.Data;

import java.util.List;

@Data
public class PostHomeResponse {

    private String postId;

    private String postDescription;

    // ------- attributes -------
    private Boolean isCommentable;

    private Boolean isShareable;

    private Boolean isShowLikes;

    private Boolean isPosted;
    // ------- attributes -------

    private List<PostContentDetailResponse> postContentList;

    private List<String> postTagsList;

    private PostOwnerResponse postOwner;

    private String postAt;

    // ------- added function -------
    private Boolean isLiked;
    // ------- added function -------

    // ------- count -------
    private Long likeCount;

    private Long commentCount;
    // ------- count -------

}
