package com.ocoelhogabriel.manager_user_security.unit.infrastructure.security;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import java.io.IOException;
import java.util.Collection;
import java.util.Collections;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.access.AccessDeniedException;

import com.auth0.jwt.exceptions.TokenExpiredException;

import com.ocoelhogabriel.manager_user_security.infrastructure.security.authorization.PermissionEvaluator;
import com.ocoelhogabriel.manager_user_security.infrastructure.security.authorization.UrlPathMatcher;
import com.ocoelhogabriel.manager_user_security.infrastructure.security.filter.JwtAuthenticationFilter;
import com.ocoelhogabriel.manager_user_security.infrastructure.security.jwt.JwtService;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@ExtendWith(MockitoExtension.class)
public class JwtAuthenticationFilterTest {

    @Mock
    private JwtService jwtService;
    
    @Mock
    private UserDetailsService userDetailsService;
    
    @Mock
    private PermissionEvaluator permissionEvaluator;
    
    @Mock
    private HttpServletRequest request;
    
    @Mock
    private HttpServletResponse response;
    
    @Mock
    private FilterChain filterChain;
    
    @Mock
    private SecurityContext securityContext;
    
    @InjectMocks
    private JwtAuthenticationFilter jwtAuthenticationFilter;
    
    private String validToken = "valid.jwt.token";
    
    @BeforeEach
    public void setup() {
        SecurityContextHolder.setContext(securityContext);
    }
    
    @Test
    @DisplayName("Should pass to next filter when no token is provided")
    public void testDoFilterInternalNoToken() throws ServletException, IOException {
        // Arrange
        when(request.getHeader("Authorization")).thenReturn(null);
        
        // Act
        jwtAuthenticationFilter.doFilterInternal(request, response, filterChain);
        
        // Assert
        verify(filterChain).doFilter(request, response);
        verifyNoInteractions(jwtService);
    }
    
    @Test
    @DisplayName("Should authenticate user with valid token")
    public void testDoFilterInternalValidToken() throws ServletException, IOException {
        // Arrange
        Collection<GrantedAuthority> authorities = Collections.singleton(new SimpleGrantedAuthority("ROLE_ADMIN"));
        UserDetails userDetails = new User("testuser", "password", authorities);
        
        when(request.getHeader("Authorization")).thenReturn("Bearer " + validToken);
        when(request.getRequestURI()).thenReturn("/api/test");
        when(request.getMethod()).thenReturn("GET");
        
        when(jwtService.validateToken(validToken)).thenReturn("testuser");
        when(userDetailsService.loadUserByUsername("testuser")).thenReturn(userDetails);
        
        UsernamePasswordAuthenticationToken authToken = 
                new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
        
        when(securityContext.getAuthentication()).thenReturn(authToken);
        
        UrlPathMatcher urlMatcher = new UrlPathMatcher(null, "Valid URL!");
        when(UrlPathMatcher.validateUrl(anyString(), anyString())).thenReturn(urlMatcher);
        
        when(permissionEvaluator.checkPermission(anyString(), any(UrlPathMatcher.class), anyString())).thenReturn(true);
        
        // Act
        jwtAuthenticationFilter.doFilterInternal(request, response, filterChain);
        
        // Assert
        verify(filterChain).doFilter(request, response);
        verify(jwtService).validateToken(validToken);
        verify(userDetailsService).loadUserByUsername("testuser");
    }
    
    @Test
    @DisplayName("Should handle expired token")
    public void testDoFilterInternalExpiredToken() throws ServletException, IOException {
        // Arrange
        when(request.getHeader("Authorization")).thenReturn("Bearer " + validToken);
        when(jwtService.validateToken(validToken)).thenThrow(new TokenExpiredException("Token expired", null));
        
        // Act
        jwtAuthenticationFilter.doFilterInternal(request, response, filterChain);
        
        // Assert
        verify(jwtService).validateToken(validToken);
        verify(filterChain, never()).doFilter(request, response);
    }
    
    @Test
    @DisplayName("Should handle permission denied")
    public void testDoFilterInternalPermissionDenied() throws ServletException, IOException {
        // Arrange
        Collection<GrantedAuthority> authorities = Collections.singleton(new SimpleGrantedAuthority("ROLE_USER"));
        UserDetails userDetails = new User("testuser", "password", authorities);
        
        when(request.getHeader("Authorization")).thenReturn("Bearer " + validToken);
        when(request.getRequestURI()).thenReturn("/api/admin");
        when(request.getMethod()).thenReturn("POST");
        
        when(jwtService.validateToken(validToken)).thenReturn("testuser");
        when(userDetailsService.loadUserByUsername("testuser")).thenReturn(userDetails);
        
        UsernamePasswordAuthenticationToken authToken = 
                new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
        when(securityContext.getAuthentication()).thenReturn(authToken);
        
        UrlPathMatcher urlMatcher = new UrlPathMatcher(null, "Valid URL!");
        when(UrlPathMatcher.validateUrl(anyString(), anyString())).thenReturn(urlMatcher);
        
        when(permissionEvaluator.checkPermission(anyString(), any(UrlPathMatcher.class), anyString())).thenReturn(false);
        
        // Act
        jwtAuthenticationFilter.doFilterInternal(request, response, filterChain);
        
        // Assert
        verify(permissionEvaluator).checkPermission(anyString(), any(UrlPathMatcher.class), anyString());
        verify(filterChain, never()).doFilter(request, response);
    }
    
    @Test
    @DisplayName("Should handle invalid URL")
    public void testDoFilterInternalInvalidUrl() throws ServletException, IOException {
        // Arrange
        Collection<GrantedAuthority> authorities = Collections.singleton(new SimpleGrantedAuthority("ROLE_ADMIN"));
        UserDetails userDetails = new User("testuser", "password", authorities);
        
        when(request.getHeader("Authorization")).thenReturn("Bearer " + validToken);
        when(request.getRequestURI()).thenReturn("/invalid/url");
        when(request.getMethod()).thenReturn("GET");
        
        when(jwtService.validateToken(validToken)).thenReturn("testuser");
        when(userDetailsService.loadUserByUsername("testuser")).thenReturn(userDetails);
        
        UsernamePasswordAuthenticationToken authToken = 
                new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
        when(securityContext.getAuthentication()).thenReturn(authToken);
        
        UrlPathMatcher urlMatcher = new UrlPathMatcher(null, "Error: Invalid URL");
        when(UrlPathMatcher.validateUrl(anyString(), anyString())).thenReturn(urlMatcher);
        
        // Act
        jwtAuthenticationFilter.doFilterInternal(request, response, filterChain);
        
        // Assert
        verify(filterChain, never()).doFilter(request, response);
    }
}