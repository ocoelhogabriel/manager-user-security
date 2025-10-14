package com.ocoelhogabriel.manager_user_security.infrastructure.security.jwt;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.exceptions.TokenExpiredException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.auth0.jwt.interfaces.JWTVerifier;
import com.ocoelhogabriel.manager_user_security.application.dto.TokenDetails;
import com.ocoelhogabriel.manager_user_security.domain.entity.Role;
import com.ocoelhogabriel.manager_user_security.domain.entity.User;
import com.ocoelhogabriel.manager_user_security.infrastructure.security.service.UserDetailsServiceImpl;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Centralized service for all JWT-related operations, including creation, validation, and refreshing tokens.
 */
@Component
public class JwtService {

    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS");
    private static final String TOKEN_ISSUER = "user-security-api";
    private static final String ROLES_CLAIM = "roles";
    private static final String USER_ID_CLAIM = "userId";

    @Value("${api.security.token.secret}")
    private String secret;

    @Value("${api.security.expiration.time.minutes}")
    private long expirationTimeInMinutes;

    private final UserDetailsServiceImpl userDetailsService;

    public JwtService(UserDetailsServiceImpl userDetailsService) {
        this.userDetailsService = userDetailsService;
    }

    /**
     * Generates a JWT token for a user, including detailed claims.
     *
     * @param user the user for whom to generate the token
     * @return token details containing the token and expiration information
     */
    public TokenDetails generateToken(User user) {
        try {
            Algorithm algorithm = Algorithm.HMAC256(secret);
            Instant expirationInstant = calculateExpirationTime();

            String roles = user.getRoles().stream()
                    .map(Role::getName)
                    .collect(Collectors.joining(","));

            String token = JWT.create()
                    .withIssuer(TOKEN_ISSUER)
                    .withSubject(user.getUsername())
                    .withExpiresAt(Date.from(expirationInstant))
                    .withClaim(ROLES_CLAIM, roles)
                    .withClaim(USER_ID_CLAIM, user.getId().toString())
                    .sign(algorithm);

            return new TokenDetails(
                    user.getUsername(),
                    token,
                    getCurrentDateTime(),
                    formatExpirationDate(expirationInstant)
            );
        } catch (JWTCreationException | IllegalArgumentException e) {
            throw new JWTCreationException("Error while generating token", e);
        }
    }

    /**
     * Validates a JWT token and returns the subject (username).
     *
     * @param token the token to validate
     * @return the username from the token
     * @throws AccessDeniedException if the token is invalid or expired
     */
    public String validateToken(String token) {
        try {
            Algorithm algorithm = Algorithm.HMAC256(secret);
            JWTVerifier verifier = JWT.require(algorithm)
                    .withIssuer(TOKEN_ISSUER)
                    .build();
            return verifier.verify(token).getSubject();
        } catch (TokenExpiredException e) {
            throw new AccessDeniedException("Token has expired", e);
        } catch (JWTVerificationException e) {
            throw new AccessDeniedException("Token verification failed", e);
        }
    }

    /**
     * Creates an authentication object from a token for Spring Security context.
     *
     * @param token the JWT token
     * @return an authentication object
     */
    public Authentication getAuthentication(String token) {
        String username = validateToken(token);
        UserDetails userDetails = userDetailsService.loadUserByUsername(username);
        return new UsernamePasswordAuthenticationToken(userDetails, "", userDetails.getAuthorities());
    }

    /**
     * Extracts the user ID from the token.
     *
     * @param token The JWT token.
     * @return An Optional containing the user ID, or empty if not found or token is invalid.
     */
    public Optional<String> getUserIdFromToken(String token) {
        try {
            DecodedJWT jwt = JWT.decode(token); // Decode without verification to read claims
            return Optional.ofNullable(jwt.getClaim(USER_ID_CLAIM).asString());
        } catch (JWTVerificationException e) {
            return Optional.empty();
        }
    }

    private Instant calculateExpirationTime() {
        return Instant.now().plusSeconds(expirationTimeInMinutes * 60);
    }

    private String getCurrentDateTime() {
        return DATE_FORMATTER.format(Instant.now().atZone(ZoneId.systemDefault()));
    }

    private String formatExpirationDate(Instant expirationInstant) {
        return DATE_FORMATTER.format(expirationInstant.atZone(ZoneId.systemDefault()));
    }
}