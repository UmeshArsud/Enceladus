package com.enceladus.enceladus.config.authentication_config;

import com.enceladus.enceladus.service.jwt_service.JwtService;
import com.enceladus.enceladus.service.jwt_service.TokenBlacklistService;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class JwtFilter extends OncePerRequestFilter {

    private static final Logger log = LoggerFactory.getLogger(JwtFilter.class);
    private final JwtService jwtService;
    private final UserDetailsService userDetailsService;
    private final TokenBlacklistService tokenBlacklistService;

    public JwtFilter(JwtService jwtService, UserDetailsService userDetailsService,
                     TokenBlacklistService tokenBlacklistService) {
        this.jwtService = jwtService;
        this.userDetailsService = userDetailsService;
        this.tokenBlacklistService = tokenBlacklistService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String authHeader = request.getHeader("Authorization");
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }
        String token = authHeader.substring(7).trim();
        // reject empty token right away instead of passing "" downstream
        if (token.isEmpty()) {
            filterChain.doFilter(request, response);
            return;
        }
        // logged-out tokens are rejected even if not yet expired
        if (tokenBlacklistService.isBlacklisted(token)) {
            log.debug("Rejected blacklisted (logged-out) token");
            filterChain.doFilter(request, response);
            return;
        }
        try {
            String username = jwtService.extractUserName(token);
            if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                UserDetails userDetails = userDetailsService.loadUserByUsername(username);
                if (jwtService.validateToken(token, userDetails)) {
                    UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                            userDetails, null, userDetails.getAuthorities()
                    );
                    authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    SecurityContextHolder.getContext().setAuthentication(authToken);
                } else {
                    log.debug("JwtToken Failed Validation For User: {}", username);
                }
            }
        } catch (ExpiredJwtException e) {
            log.debug("JwtExpired: {}", e.getMessage());
            // Don't set authentication — request proceeds unauthenticated,
            // Spring Security's access rules will return 401/403 as appropriate.
        } catch (UnsupportedJwtException | MalformedJwtException e) {
            log.warn("Invalid Jwt: {}", e.getMessage());
        } catch (UsernameNotFoundException e) {
            log.warn("Jwt Referenced Unknown User: {}", e.getMessage());
        } catch (IllegalArgumentException e) {
            log.warn("Jwt Claims String IS Empty or Invalid: {}", e.getMessage());
        } catch (Exception e) {
            log.error("Unexpected error While Proccessing JWT...!!!", e);
        }
        filterChain.doFilter(request, response);
    }
}
