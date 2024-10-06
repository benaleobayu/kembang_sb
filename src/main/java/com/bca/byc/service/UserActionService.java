package com.bca.byc.service;

import com.bca.byc.model.attribute.SetLikeDislikeRequest;
import com.bca.byc.model.attribute.TotalCountResponse;

public interface UserActionService {

    void followUser(String userId, String name);

    void unfollowUser(String userId, String name);

    TotalCountResponse likeDislike(String email, SetLikeDislikeRequest dto);

    String saveUnsavePost(String postId, String email);
}
