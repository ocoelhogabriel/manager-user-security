package com.ocoelhogabriel.usersecurity.infrastructure.security.filter;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.auth0.jwt.exceptions.JWTVerificationException;
import com.ocoelhogabriel.usersecurity.application.service.UrlValidator;
import com.ocoelhogabriel.usersecurity.domain.service.AuthorizationService;
import com.ocoelhogabriel.usersecurity.infrastructure.auth.jwt.JwtTokenProvider;
import com.ocoelhogabriel.usersecurity.infrastructure.security.handler.CustomAccessDeniedHandler;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 * Filter for authenticating users with JWT tokens.
 */
@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final CustomAccessDeniedHandler accessDeniedHandler = new CustomAccessDeniedHandler();

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    private UrlValidator urlValidator;

    @Autowired
    private AuthorizationService authorizationService;

    /**
     * Extracts the JWT token from the request's Authorization header.
     *
     * @param request the HTTP request
     * @return the extracted token, or null if not found
     */
    private String extractTokenFromRequest(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }
        return null;
    }

    @Override
    protected void doFilterInternal(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain
    ) throws ServletException, IOException {
        try {
            // Skip filter for public URLs
            if (urlValidator.isPublicUrl(request.getRequestURI())) {
                filterChain.doFilter(request, response);
                return;
            }

            // Extract token from request
            String token = extractTokenFromRequest(request);
            if (token == null) {
                filterChain.doFilter(request, response);
                return;
            }

            // Validate token and get username
            String username = jwtTokenProvider.validateToken(token);
            
            // If token is valid and user is not authenticated yet
            if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                UserDetails userDetails = userDetailsService.loadUserByUsername(username);
                
                // Create authentication token
                UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                        userDetails, null, userDetails.getAuthorities());
                
                // Set authentication details
                authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                
                // Set authentication in security context
                SecurityContextHolder.getContext().setAuthentication(authToken);
                
                // Validate URL and permission
                String requestURI = request.getRequestURI();
                String method = request.getMethod();
                
                if (!urlValidator.isValidUrl(requestURI, method)) {
                    accessDeniedHandler.handle(request, response, 
                            new org.springframework.security.access.AccessDeniedException("Invalid URL"));
                    return;
                }
                
                String resourceName = urlValidator.extractResourceNameFromUrl(requestURI);
                String action = urlValidator.mapHttpMethodToAction(method);
                
                if (!authorizationService.hasPermission(username, resourceName, action)) {
                    accessDeniedHandler.handle(request, response,
                            new org.springframework.security.access.AccessDeniedException(
                                    "Not authorized to perform this action"));
                    return;
                }
            }
            
            filterChain.doFilter(request, response);
        } catch (JWTVerificationException e) {
            accessDeniedHandler.handle(request, response,
                    new org.springframework.security.access.AccessDeniedException("Invalid token: " + e.getMessage()));
        } catch (Exception e) {
            accessDeniedHandler.handle(request, response,
                    new org.springframework.security.access.AccessDeniedException("Error processing request"));
        }
    }
}