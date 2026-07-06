package com.enceladus.enceladus.service.jwt_service;

import org.springframework.stereotype.Service;

import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Stateless JWTs can't be "deleted" server-side, so logout is implemented as a
 * denylist: once a token is logged out, it is rejected even though it hasn't
 * technically expired yet.
 *
 * NOTE: this is an in-memory implementation for a single instance / dev use.
 * For production (multiple instances, restarts) swap this for a shared store
 * with a TTL, e.g. Redis with expiry = token's remaining lifetime.
 */
@Service
public class TokenBlacklistService {

    private final Set<String> blacklistedTokens = ConcurrentHashMap.newKeySet();

    public void blacklist(String token) {
        blacklistedTokens.add(token);
    }

    public boolean isBlacklisted(String token) {
        return blacklistedTokens.contains(token);
    }
}
