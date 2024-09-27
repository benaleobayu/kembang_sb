package com.bca.byc.service;

public interface UserActionService {

    void followUser(String userId, String name);

    void unfollowUser(String userId, String name);

    void likeDislikePost(String postId, String email, Boolean isLike);

    void likeDislikeComment(String commentId, String email, Boolean isLike);

    void likeDislikeCommentReply(String commentId, String email, Boolean isLike);
}
