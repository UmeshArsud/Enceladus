package com.enceladus.enceladus.config;

import io.github.bucket4j.Bandwidth;
import io.github.bucket4j.Bucket;
import io.github.bucket4j.Refill;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.time.Duration;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Throttles requests to the authentication endpoints to blunt brute-force
 * and credential-stuffing attacks. Keyed by client IP.
 *
 * NOTE: in-memory buckets, same caveat as TokenBlacklistService — fine for a
 * single instance; use a shared store (e.g. Bucket4j + Redis) if you scale
 * horizontally.
 */
@Component
public class RateLimitingFilter extends OncePerRequestFilter {

    // 5 requests per minute per IP on auth endpoints — tune to your needs
    private static final int CAPACITY = 5;
    private static final Duration REFILL_PERIOD = Duration.ofMinutes(1);

    private final ConcurrentHashMap<String, Bucket> buckets = new ConcurrentHashMap<>();

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        String path = request.getRequestURI();
        boolean isAuthEndpoint = path.startsWith("/api/auth/signin") || path.startsWith("/api/auth/signup");

        if (!isAuthEndpoint) {
            filterChain.doFilter(request, response);
            return;
        }

        String clientKey = resolveClientIp(request);
        Bucket bucket = buckets.computeIfAbsent(clientKey, k -> newBucket());

        if (bucket.tryConsume(1)) {
            filterChain.doFilter(request, response);
        } else {
            response.setStatus(429); // HttpServletResponse doesn't have a TOO_MANY_REQUESTS constant pre-6.x
            response.setContentType("application/json");
            response.getWriter().write(
                    "{\"message\":\"Too many requests. Please try again later.\"}");
        }
    }

    private Bucket newBucket() {
        Bandwidth limit = Bandwidth.classic(CAPACITY, Refill.greedy(CAPACITY, REFILL_PERIOD));
        return Bucket.builder().addLimit(limit).build();
    }

    private String resolveClientIp(HttpServletRequest request) {
        // Respect X-Forwarded-For only if you're actually behind a trusted proxy/load balancer;
        // otherwise an attacker can spoof this header to bypass the limiter entirely.
        String forwardedFor = request.getHeader("X-Forwarded-For");
        if (forwardedFor != null && !forwardedFor.isBlank()) {
            return forwardedFor.split(",")[0].trim();
        }
        return request.getRemoteAddr();
    }
}

