package com.bca.byc.service.util;

import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;

@Service
public class TokenBlacklistService {
    private final Set<String> blacklist = new HashSet<>();

    public void blacklistToken(String token) {
        blacklist.add(token); // Add the token to the blacklist
    }

    public boolean isTokenBlacklisted(String token) {
        return blacklist.contains(token); // Check if the token is blacklisted
    }
}
