package com.bca.byc.model;

import com.bca.byc.model.apps.ListCommentResponse;
import com.bca.byc.model.apps.OwnerDataResponse;
import com.bca.byc.model.apps.PostContentDetailResponse;
import lombok.Data;

import java.util.List;

@Data
public class PostDetailResponse {

    private String id;
    private String description;
    private Boolean status;

    // ------- attributes -------
    private Boolean isCommentable;

    private Boolean isShareable;

    private Boolean isShowLikes;

    private Boolean isPosted;
    // ------- attributes -------

    private List<PostContentDetailResponse> contentList; //

    private List<ListCommentResponse> commentList;

    private OwnerDataResponse postOwner;

    // ------- added function -------
    private Boolean isLiked;
    // ------- added function -------

}
