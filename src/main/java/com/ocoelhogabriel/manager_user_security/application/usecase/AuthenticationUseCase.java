package com.ocoelhogabriel.manager_user_security.application.usecase;

import java.time.Duration;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.ocoelhogabriel.manager_user_security.application.exception.ApplicationException;
import com.ocoelhogabriel.manager_user_security.domain.entity.Role;
import com.ocoelhogabriel.manager_user_security.domain.entity.User;
import com.ocoelhogabriel.manager_user_security.domain.exception.AuthenticationException;
import com.ocoelhogabriel.manager_user_security.domain.service.AuthenticationService;
import com.ocoelhogabriel.manager_user_security.domain.service.UserService;
import com.ocoelhogabriel.manager_user_security.infrastructure.security.jwt.JwtTokenProvider;
import com.ocoelhogabriel.manager_user_security.interfaces.dto.AuthenticationResponse;
import com.ocoelhogabriel.manager_user_security.interfaces.dto.TokenValidationResponse;
import com.ocoelhogabriel.manager_user_security.interfaces.dto.UserRoleDto;

/**
 * Implementation of the Authentication use case.
 */
@Service
public class AuthenticationUseCase {

    @Autowired
    private AuthenticationService authenticationService;
    
    @Autowired
    private UserService userService;
    
    @Autowired
    private JwtTokenProvider jwtTokenProvider;
    
    @Autowired
    private AuthenticationManager authenticationManager;
    
    /**
     * Authenticates a user with username and password.
     *
     * @param username the username of the user
     * @param password the password of the user
     * @return the authentication response with the JWT token
     * @throws AuthenticationException if authentication fails
     */
    public AuthenticationResponse authenticate(String username, String password) {
        try {
            // Authenticate with Spring Security
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(username, password));
            SecurityContextHolder.getContext().setAuthentication(authentication);
            
            // Find the user
            Optional<User> userOpt = userService.findByUsername(username);
            if (userOpt.isEmpty()) {
                throw new AuthenticationException("User not found");
            }
            
            User user = userOpt.get();
            
            // Generate JWT token
            String token = jwtTokenProvider.generateToken(user);
            
            // Get token expiration
            Instant expiration = jwtTokenProvider.getExpirationFromToken(token);
            LocalDateTime issuedAt = LocalDateTime.now();
            long expiresInSeconds = Duration.between(issuedAt, expiration.atZone(ZoneId.systemDefault()).toLocalDateTime()).getSeconds();
            
            // Get user role
            Role primaryRole = user.getRoles().stream().findFirst().orElse(null);
            UserRoleDto roleDto = primaryRole != null 
                    ? new UserRoleDto(primaryRole.getId().toString(), primaryRole.getName(), primaryRole.getDescription())
                    : null;
            
            // Create response
            return new AuthenticationResponse(
                    token,
                    issuedAt,
                    expiresInSeconds,
                    user.getId().toString(),
                    roleDto
            );
        } catch (BadCredentialsException e) {
            throw new AuthenticationException("Invalid username or password");
        } catch (Exception e) {
            throw new ApplicationException("Authentication failed", e);
        }
    }
    
    /**
     * Validates a JWT token.
     *
     * @param token the token to validate
     * @return the validation response
     */
    public TokenValidationResponse validateToken(String token) {
        try {
            String username = jwtTokenProvider.validateToken(token);
            if (username == null) {
                return new TokenValidationResponse(false, 0, "Invalid token");
            }
            
            // Get token expiration
            Instant expiration = jwtTokenProvider.getExpirationFromToken(token);
            long expiresInSeconds = Duration.between(
                    LocalDateTime.now(), 
                    expiration.atZone(ZoneId.systemDefault()).toLocalDateTime()
            ).getSeconds();
            
            return new TokenValidationResponse(true, expiresInSeconds, "Token is valid");
        } catch (Exception e) {
            return new TokenValidationResponse(false, 0, "Invalid token: " + e.getMessage());
        }
    }
    
    /**
     * Refreshes a JWT token.
     *
     * @param token the token to refresh
     * @return the authentication response with the new token
     */
    public AuthenticationResponse refreshToken(String token) {
        Optional<String> refreshedTokenOpt = jwtTokenProvider.refreshToken(token);
        if (refreshedTokenOpt.isEmpty()) {
            throw new AuthenticationException("Could not refresh token");
        }
        
        String refreshedToken = refreshedTokenOpt.get();
        String username = jwtTokenProvider.validateToken(refreshedToken);
        
        Optional<User> userOpt = userService.findByUsername(username);
        if (userOpt.isEmpty()) {
            throw new AuthenticationException("User not found");
        }
        
        User user = userOpt.get();
        
        // Get token expiration
        Instant expiration = jwtTokenProvider.getExpirationFromToken(refreshedToken);
        LocalDateTime issuedAt = LocalDateTime.now();
        long expiresInSeconds = Duration.between(
                issuedAt, 
                expiration.atZone(ZoneId.systemDefault()).toLocalDateTime()
        ).getSeconds();
        
        // Get user role
        Role primaryRole = user.getRoles().stream().findFirst().orElse(null);
        UserRoleDto roleDto = primaryRole != null 
                ? new UserRoleDto(primaryRole.getId().toString(), primaryRole.getName(), primaryRole.getDescription())
                : null;
        
        return new AuthenticationResponse(
                refreshedToken,
                issuedAt,
                expiresInSeconds,
                user.getId().toString(),
                roleDto
        );
    }
    
    /**
     * Gets the current authenticated user.
     *
     * @return the current user information
     */
    public Object getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            throw new AuthenticationException("Not authenticated");
        }
        
        String username = authentication.getName();
        Optional<User> userOpt = userService.findByUsername(username);
        if (userOpt.isEmpty()) {
            throw new AuthenticationException("User not found");
        }
        
        User user = userOpt.get();
        
        // Return user details (could be converted to a DTO here)
        return user;
    }
}
