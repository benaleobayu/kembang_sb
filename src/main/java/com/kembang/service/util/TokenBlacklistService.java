package com.kembang.service.util;

public interface TokenBlacklistService {

    void addToBlacklist (String token);

    boolean isTokenBlacklisted (String token);
}
