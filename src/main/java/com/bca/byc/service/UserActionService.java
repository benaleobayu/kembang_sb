package com.bca.byc.service;

import com.bca.byc.model.attribute.SetLikeDislikeRequest;

public interface UserActionService {

    void followUser(String userId, String name);

    void unfollowUser(String userId, String name);

    void likeDislikePost(String postId, String email, SetLikeDislikeRequest isLike);

    void likeDislikeComment(String commentId, String email, Boolean isLike);

    void likeDislikeCommentReply(String commentId, String email, Boolean isLike);
}
