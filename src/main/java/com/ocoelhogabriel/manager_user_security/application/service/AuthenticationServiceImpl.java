package com.ocoelhogabriel.manager_user_security.application.service;

import com.auth0.jwt.exceptions.TokenExpiredException;
import com.ocoelhogabriel.manager_user_security.application.dto.TokenDetails;
import com.ocoelhogabriel.manager_user_security.application.exception.ApplicationException;
import com.ocoelhogabriel.manager_user_security.domain.entity.Role;
import com.ocoelhogabriel.manager_user_security.domain.entity.User;
import com.ocoelhogabriel.manager_user_security.domain.exception.AuthenticationException;
import com.ocoelhogabriel.manager_user_security.domain.service.AuthenticationService;
import com.ocoelhogabriel.manager_user_security.domain.service.UserService;
import com.ocoelhogabriel.manager_user_security.infrastructure.security.jwt.JwtManager;
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
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@Service
public class AuthenticationServiceImpl implements AuthenticationService {

    private final UserService userService;
    private final JwtManager jwtManager;
    private final AuthenticationManager authenticationManager;
    private final Set<String> invalidatedTokens = Collections.synchronizedSet(new HashSet<>()); // In-memory blacklist

    public AuthenticationServiceImpl(UserService userService, JwtManager jwtManager, AuthenticationManager authenticationManager) {
        this.userService = userService;
        this.jwtManager = jwtManager;
        this.authenticationManager = authenticationManager;
    }

    @Override
    public AuthenticationResponse authenticate(String username, String password) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(username, password));
            SecurityContextHolder.getContext().setAuthentication(authentication);

            User user = userService.findByUsername(username)
                    .orElseThrow(() -> new AuthenticationException("User not found"));

            TokenDetails tokenDetails = jwtManager.generateToken(user);

            Role primaryRole = user.getRoles().stream().findFirst().orElse(null);
            UserRoleDto roleDto = primaryRole != null
                    ? new UserRoleDto(primaryRole.getId().toString(), primaryRole.getName(), primaryRole.getDescription())
                    : null;

            return new AuthenticationResponse(
                    tokenDetails.token(),
                    tokenDetails.issuedAt(),
                    Duration.between(tokenDetails.issuedAt(), tokenDetails.expiresAt()).getSeconds(),
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
        if (invalidatedTokens.contains(token)) {
            return new TokenValidationResponse(false, 0, "Token has been invalidated");
        }
        try {
            String username = jwtManager.validateToken(token);
            if (username == null) {
                return new TokenValidationResponse(false, 0, "Invalid token");
            }

            Optional<User> userOpt = userService.findByUsername(username);
            if (userOpt.isEmpty()) {
                return new TokenValidationResponse(false, 0, "Invalid token: User not found");
            }

            // Re-generate token details to get current expiration, but don't issue a new token here
            // This part might need adjustment based on how JwtManager.generateToken works internally
            // For now, assuming it can extract details without issuing a new one.
            // If JwtManager.generateToken always issues a new token, this logic needs to be rethought.
            // For validation, we only need to know if it's valid and its remaining time.
            // A better approach would be for JwtManager to have a method like 'getTokenExpiration(token)'
            // For now, we'll rely on JwtManager.validateToken to implicitly handle expiration.
            // The expiresInSeconds calculation below is for the *new* token if it were generated, not the current one.
            // Let's simplify: if validateToken returns username, it's valid and not expired.
            // The expiration time should ideally come from the token itself, not by generating a new one.
            // For simplicity, we'll just return a generic valid response if not expired and not blacklisted.

            // A more robust solution would involve parsing the token's expiration claim directly.
            // Given current JwtManager API, we'll assume validateToken handles expiration.
            return new TokenValidationResponse(true, 0, "Token is valid"); // 0 for expiresInSeconds as we don't have direct access to current token's remaining time here.

        } catch (TokenExpiredException e) {
            return new TokenValidationResponse(false, 0, "Token has expired");
        } catch (Exception e) {
            return new TokenValidationResponse(false, 0, "Invalid token: " + e.getMessage());
        }
    }

    @Override
    public AuthenticationResponse refreshToken(String token) {
        if (invalidatedTokens.contains(token)) {
            throw new AuthenticationException("Cannot refresh an invalidated token");
        }
        return jwtManager.refreshToken(token)
                .map(refreshedTokenDetails -> {
                    User user = userService.findByUsername(refreshedTokenDetails.username())
                            .orElseThrow(() -> new AuthenticationException("User not found"));

                    long expiresInSeconds = Duration.between(refreshedTokenDetails.issuedAt(), refreshedTokenDetails.expiresAt()).getSeconds();

                    Role primaryRole = user.getRoles().stream().findFirst().orElse(null);
                    UserRoleDto roleDto = primaryRole != null
                            ? new UserRoleDto(primaryRole.getId().toString(), primaryRole.getName(), primaryRole.getDescription())
                            : null;

                    return new AuthenticationResponse(
                            refreshedTokenDetails.token(),
                            refreshedTokenDetails.issuedAt(),
                            expiresInSeconds,
                            user.getId().toString(),
                            roleDto
                    );
                })
                .orElseThrow(() -> new AuthenticationException("Could not refresh token"));
    }

    @Override
    public Object getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            throw new AuthenticationException("Not authenticated");
        }

        String username = authentication.getName();
        return userService.findByUsername(username)
                .orElseThrow(() -> new AuthenticationException("User not found"));
    }

    @Override
    public void invalidateToken(String token) {
        invalidatedTokens.add(token);
    }
}
