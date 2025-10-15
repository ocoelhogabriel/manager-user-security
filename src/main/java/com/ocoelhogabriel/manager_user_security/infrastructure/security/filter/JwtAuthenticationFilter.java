package com.ocoelhogabriel.manager_user_security.infrastructure.security.filter;

import com.auth0.jwt.exceptions.TokenExpiredException;
import com.ocoelhogabriel.manager_user_security.infrastructure.security.authorization.PermissionEvaluator;
import com.ocoelhogabriel.manager_user_security.infrastructure.security.authorization.UrlPathMatcher;
import com.ocoelhogabriel.manager_user_security.infrastructure.security.handler.CustomAccessDeniedHandler;
import com.ocoelhogabriel.manager_user_security.infrastructure.security.jwt.JwtManager;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
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
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

public class JwtAuthenticationFilter extends OncePerRequestFilter {
    
    private static final Logger log = LoggerFactory.getLogger(JwtAuthenticationFilter.class);
    
    private final CustomAccessDeniedHandler accessDeniedHandler = new CustomAccessDeniedHandler();

    @Autowired
    private JwtManager jwtManager; // Corrigido
    
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
            
            if (token == null) {
                filterChain.doFilter(request, response);
                return;
            }

            String username = jwtManager.validateToken(token); // Corrigido
            UserDetails userDetails = userDetailsService.loadUserByUsername(username);

            UsernamePasswordAuthenticationToken authToken = 
                    new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
            SecurityContextHolder.getContext().setAuthentication(authToken);

            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            if (authentication == null || !authentication.isAuthenticated()) {
                accessDeniedHandler.handle(request, response, 
                        new AccessDeniedException("Authentication required"));
                return;
            }

            String role = authentication.getAuthorities().stream()
                    .map(GrantedAuthority::getAuthority)
                    .findFirst()
                    .orElse(null);

            UrlPathMatcher urlMatcher = UrlPathMatcher.validateUrl(requestUri, method);
            
            if (urlMatcher.getMessage().startsWith("Error") || 
                urlMatcher.getMessage().startsWith("Invalid")) {
                accessDeniedHandler.handle(request, response, 
                        new AccessDeniedException("Invalid URL: " + urlMatcher.getMessage()));
                return;
            }
            
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

    private String extractToken(HttpServletRequest request) {
        String authHeader = request.getHeader("Authorization");
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return null;
        }
        return authHeader.substring(7);
    }
}