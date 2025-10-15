package com.ocoelhogabriel.manager_user_security.application.service;

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
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Optional;

@Service
public class AuthenticationServiceImpl implements AuthenticationService {

    private final UserService userService;
    private final JwtTokenProvider jwtTokenProvider;
    private final AuthenticationManager authenticationManager;

    
    public AuthenticationServiceImpl(UserService userService, JwtTokenProvider jwtTokenProvider, AuthenticationManager authenticationManager) {
        this.userService = userService;
        this.jwtTokenProvider = jwtTokenProvider;
        this.authenticationManager = authenticationManager;
    }

    @Override
    public AuthenticationResponse authenticate(String username, String password) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(username, password));
            SecurityContextHolder.getContext().setAuthentication(authentication);

            Optional<User> userOpt = userService.findByUsername(username);
            if (userOpt.isEmpty()) {
                throw new AuthenticationException("User not found");
            }

            User user = userOpt.get();

            String token = jwtTokenProvider.generateToken(user);

            Instant expiration = jwtTokenProvider.getExpirationFromToken(token);
            LocalDateTime issuedAt = LocalDateTime.now();
            long expiresInSeconds = Duration.between(issuedAt, expiration.atZone(ZoneId.systemDefault()).toLocalDateTime()).getSeconds();

            Role primaryRole = user.getRoles().stream().findFirst().orElse(null);
            UserRoleDto roleDto = primaryRole != null
                    ? new UserRoleDto(primaryRole.getId().toString(), primaryRole.getName(), primaryRole.getDescription())
                    : null;

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

    @Override
    public TokenValidationResponse validateToken(String token) {
        try {
            String username = jwtTokenProvider.validateToken(token);
            if (username == null) {
                return new TokenValidationResponse(false, 0, "Invalid token");
            }

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

    @Override
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

        Instant expiration = jwtTokenProvider.getExpirationFromToken(refreshedToken);
        LocalDateTime issuedAt = LocalDateTime.now();
        long expiresInSeconds = Duration.between(
                issuedAt,
                expiration.atZone(ZoneId.systemDefault()).toLocalDateTime()
        ).getSeconds();

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

    @Override
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

        return userOpt.get();
    }

    @Override
    public void invalidateToken(String token) {
        // TODO: Implement token invalidation, e.g., by adding to a blacklist
    }
}
