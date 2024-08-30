package com.bca.byc.service;

public interface AppUserService {

    // follow
    void followUser(Long userId, String email);

    void unfollowUser(Long userId, String email);

}
