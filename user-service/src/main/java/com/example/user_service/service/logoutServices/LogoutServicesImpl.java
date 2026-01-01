package com.example.user_service.service.logoutServices;

import com.example.user_service.service.jwtServices.JwtUtils;
import com.example.user_service.service.jwtServices.TokenBlacklistService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Date;

@Service
public class LogoutServicesImpl implements LogoutServices {

    private final TokenBlacklistService tokenBlacklistService;
    private final JwtUtils jwtUtils;

    @Autowired
    public LogoutServicesImpl(TokenBlacklistService tokenBlacklistService, JwtUtils jwtUtils) {
        this.tokenBlacklistService = tokenBlacklistService;
        this.jwtUtils = jwtUtils;
    }

    @Override
    public void logout(String token) {
        // extract expiration and blacklist the token until its natural expiry
        try {
            Date expiration = jwtUtils.extractExpiration(token);
            Instant expiresAt = expiration.toInstant();
            tokenBlacklistService.blacklistToken(token, expiresAt);
        } catch (Exception e) {
            // In case of malformed token, still attempt to blacklist by storing short-lived entry
            tokenBlacklistService.blacklistToken(token, Instant.now().plusSeconds(60));
        }
    }
}

