package com.ocoelhogabriel.manager_user_security.infrastructure.security.filter;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.NonNull;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.auth0.jwt.exceptions.TokenExpiredException;

import com.ocoelhogabriel.manager_user_security.infrastructure.security.authorization.PermissionEvaluator;
import com.ocoelhogabriel.manager_user_security.infrastructure.security.authorization.UrlPathMatcher;
import com.ocoelhogabriel.manager_user_security.infrastructure.security.handler.CustomAccessDeniedHandler;
import com.ocoelhogabriel.manager_user_security.infrastructure.security.jwt.JwtService;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 * JWT authentication filter for validating and processing JWT tokens in requests
 */
@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    
    private static final Logger log = LoggerFactory.getLogger(JwtAuthenticationFilter.class);
    
    private final CustomAccessDeniedHandler accessDeniedHandler = new CustomAccessDeniedHandler();

    @Autowired
    private JwtService jwtService;
    
    @Autowired
    private UserDetailsService userDetailsService;
    
    @Autowired
    private PermissionEvaluator permissionEvaluator;

    @Override
    protected void doFilterInternal(
            @NonNull HttpServletRequest request, 
            @NonNull HttpServletResponse response, 
            @NonNull FilterChain filterChain) throws ServletException, IOException {
        try {
            String token = extractToken(request);
            String requestUri = request.getRequestURI();
            String method = request.getMethod();
            
            // If no token is present, pass to next filter
            if (token == null) {
                filterChain.doFilter(request, response);
                return;
            }

            // Validate token and load user details
            String username = jwtService.validateToken(token);
            UserDetails userDetails = userDetailsService.loadUserByUsername(username);

            // Create authentication token and set in security context
            UsernamePasswordAuthenticationToken authToken = 
                    new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
            SecurityContextHolder.getContext().setAuthentication(authToken);

            // Get current authentication
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            if (authentication == null || !authentication.isAuthenticated()) {
                accessDeniedHandler.handle(request, response, 
                        new AccessDeniedException("Authentication required"));
                return;
            }

            // Extract role from authentication
            String role = authentication.getAuthorities().stream()
                    .map(GrantedAuthority::getAuthority)
                    .findFirst()
                    .orElse(null);

            // Validate URL and check permissions
            UrlPathMatcher urlMatcher = UrlPathMatcher.validateUrl(requestUri, method);
            
            // Handle URL validation failure
            if (urlMatcher.getMessage().startsWith("Error") || 
                urlMatcher.getMessage().startsWith("Invalid")) {
                accessDeniedHandler.handle(request, response, 
                        new AccessDeniedException("Invalid URL: " + urlMatcher.getMessage()));
                return;
            }
            
            // Check permission
            boolean hasPermission = permissionEvaluator.checkPermission(role, urlMatcher, method);
            if (!hasPermission) {
                accessDeniedHandler.handle(request, response, 
                        new AccessDeniedException("Not authorized to perform this action"));
                return;
            }
            
            filterChain.doFilter(request, response);
        } catch (TokenExpiredException | AccessDeniedException e) {
            log.error("Security exception: {}", e.getMessage());
            accessDeniedHandler.handle(request, response, new AccessDeniedException(e.getMessage()));
        }
    }

    /**
     * Extracts token from request header
     * 
     * @param request The HTTP request
     * @return The extracted token or null if not present
     */
    private String extractToken(HttpServletRequest request) {
        String authHeader = request.getHeader("Authorization");
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return null;
        }
        return authHeader.substring(7);
    }
}