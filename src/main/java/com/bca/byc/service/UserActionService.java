package com.bca.byc.service;

public interface UserActionService {

    void followUser(Long userId, String name);

    void unfollowUser(Long userId, String name);

    void likeDislikePost(Long postId, String email, Boolean isLike);

    void likeDislikeComment(Long id, String email, Boolean isLike);

    void likeDislikeCommentReply(Long id, String email, Boolean isLike);
}
