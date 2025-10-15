package com.ocoelhogabriel.manager_user_security.infrastructure.security.jwt;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import com.ocoelhogabriel.manager_user_security.infrastructure.security.util.SecurityConstants;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 * Filter for JWT authentication.
 * Intercepts requests and authenticates users based on JWT tokens.
 */
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtManager jwtManager;
    private final List<String> excludedPaths;

    /**
     * Constructor for JwtAuthenticationFilter.
     * 
     * @param jwtManager the JWT manager
     */
    public JwtAuthenticationFilter(JwtManager jwtManager) {
        this.jwtManager = jwtManager;
        this.excludedPaths = Arrays.asList(
                SecurityConstants.AUTH_WHITELIST
        );
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        
        // Skip authentication for excluded paths
        if (isPathExcluded(request)) {
            filterChain.doFilter(request, response);
            return;
        }
        
        String token = extractToken(request);
        
        if (token != null) {
            try {
                Authentication authentication = jwtManager.getAuthentication(token);
                SecurityContextHolder.getContext().setAuthentication(authentication);
            } catch (Exception e) {
                SecurityContextHolder.clearContext();
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                response.setContentType("application/json");
                response.getWriter().write("{\"error\": \"" + e.getMessage() + "\"}");
                return;
            }
        }
        
        filterChain.doFilter(request, response);
    }
    
    /**
     * Extracts the JWT token from the Authorization header.
     * 
     * @param request the HTTP request
     * @return the token, or null if no valid token is found
     */
    private String extractToken(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }
        return null;
    }
    
    /**
     * Checks if the request path should be excluded from authentication.
     * 
     * @param request the HTTP request
     * @return true if the path should be excluded, false otherwise
     */
    private boolean isPathExcluded(HttpServletRequest request) {
        String path = request.getServletPath();
        return excludedPaths.stream().anyMatch(path::startsWith);
    }
}
