package com.bca.byc.service;

import com.bca.byc.model.PostShareResponse;
import com.bca.byc.model.attribute.SetLikeDislikeRequest;
import com.bca.byc.model.attribute.TotalCountResponse;
import com.bca.byc.model.returns.ReturnIsSavedResponse;

public interface UserActionService {

    void followUser(String userId, String name);

    void unfollowUser(String userId, String name);

    TotalCountResponse likeDislike(String email, SetLikeDislikeRequest dto);

    ReturnIsSavedResponse saveUnsavePost(String postId);

    String sharePost(PostShareResponse postId, String email);
}
