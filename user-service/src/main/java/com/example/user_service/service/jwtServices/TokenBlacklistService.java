package com.example.user_service.service.jwtServices;

import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class TokenBlacklistService {

    private final Map<String, Instant> blacklistedTokens = new ConcurrentHashMap<>();


    public void blacklistToken(String token, Instant expiration) {
        blacklistedTokens.put(token, expiration);
    }


    public boolean isTokenBlacklisted(String token) {
        Instant expiration = blacklistedTokens.get(token);
        if (expiration == null) return false;


        if (Instant.now().isAfter(expiration)) {
            blacklistedTokens.remove(token);
            return false;
        }

        return true;
    }
}