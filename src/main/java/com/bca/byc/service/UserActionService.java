package com.bca.byc.service;

public interface UserActionService {

    void followUser(Long userId, String name);

    void unfollowUser(Long userId, String name);

    void likeDislikePost(Long userId, String name);

}
