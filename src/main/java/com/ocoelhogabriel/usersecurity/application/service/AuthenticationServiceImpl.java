package com.ocoelhogabriel.usersecurity.application.service;

import java.time.Instant;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.ocoelhogabriel.usersecurity.domain.entity.User;
import com.ocoelhogabriel.usersecurity.domain.exception.AuthenticationException;
import com.ocoelhogabriel.usersecurity.domain.repository.UserRepository;
import com.ocoelhogabriel.usersecurity.domain.service.AuthenticationService;
import com.ocoelhogabriel.usersecurity.infrastructure.auth.jwt.JwtTokenProvider;

/**
 * Implementation of the AuthenticationService.
 */
@Service
public class AuthenticationServiceImpl implements AuthenticationService {

    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private PasswordEncoder passwordEncoder;
    
    @Autowired
    private JwtTokenProvider jwtTokenProvider;
    
    @Override
    public Optional<User> authenticate(String username, String password) {
        Optional<User> userOpt = userRepository.findByUsername(username);
        
        if (userOpt.isEmpty()) {
            return Optional.empty();
        }
        
        User user = userOpt.get();
        
        // Check if the user is active
        if (!user.isActive()) {
            return Optional.empty();
        }
        
        // Verify password
        if (!passwordEncoder.matches(password, user.getPasswordHash())) {
            return Optional.empty();
        }
        
        return Optional.of(user);
    }

    @Override
    public Optional<User> validateToken(String token) {
        try {
            String username = jwtTokenProvider.validateToken(token);
            return userRepository.findByUsername(username);
        } catch (Exception e) {
            return Optional.empty();
        }
    }

    @Override
    public String generateToken(User user) {
        if (user == null) {
            throw new AuthenticationException("User cannot be null");
        }
        
        return jwtTokenProvider.generateToken(user);
    }

    @Override
    public Optional<String> refreshToken(String token) {
        return jwtTokenProvider.refreshToken(token);
    }

    @Override
    public void invalidateToken(String token) {
        // This would typically be implemented with a token blacklist
        // For now, we don't need to implement this as JWT tokens are stateless
        // and can't be invalidated unless we maintain a blacklist
    }
    
    /**
     * Gets the expiration time from a token.
     *
     * @param token the token to check
     * @return the expiration time as an Instant
     */
    public Instant getTokenExpiration(String token) {
        return jwtTokenProvider.getExpirationFromToken(token);
    }
    
    /**
     * Gets the user ID from a token.
     *
     * @param token the token to check
     * @return an Optional containing the user ID, or empty if not found
     */
    public Optional<String> getUserIdFromToken(String token) {
        return jwtTokenProvider.getUserIdFromToken(token);
    }
    
    /**
     * Gets the roles from a token.
     *
     * @param token the token to check
     * @return the roles as a string
     */
    public String getRolesFromToken(String token) {
        return jwtTokenProvider.getRolesFromToken(token);
    }
}