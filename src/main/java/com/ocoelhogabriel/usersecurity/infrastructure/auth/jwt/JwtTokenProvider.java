package com.ocoelhogabriel.usersecurity.infrastructure.auth.jwt;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.Optional;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.ocoelhogabriel.usersecurity.domain.entity.Role;
import com.ocoelhogabriel.usersecurity.domain.entity.User;

/**
 * Component responsible for JWT token generation and validation.
 */
@Component
public class JwtTokenProvider {

    private static final Logger logger = LoggerFactory.getLogger(JwtTokenProvider.class);
    
    @Value("${api.security.token.secret}")
    private String jwtSecret;
    
    @Value("${api.security.expiration.time.minutes}")
    private long expirationMinutes;

    /**
     * Generates a JWT token for the given user.
     *
     * @param user the user to generate a token for
     * @return the generated JWT token
     * @throws JWTCreationException if there is an error during token creation
     */
    public String generateToken(User user) {
        try {
            Algorithm algorithm = Algorithm.HMAC256(jwtSecret);
            Instant issuedAt = Instant.now();
            Instant expiresAt = issuedAt.plus(expirationMinutes, ChronoUnit.MINUTES);
            
            String roles = user.getRoles().stream()
                    .map(Role::getName)
                    .collect(Collectors.joining(","));
            
            return JWT.create()
                    .withIssuer("user-security-api")
                    .withSubject(user.getUsername())
                    .withIssuedAt(Date.from(issuedAt))
                    .withExpiresAt(Date.from(expiresAt))
                    .withClaim("roles", roles)
                    .withClaim("userId", user.getId().toString())
                    .sign(algorithm);
        } catch (JWTCreationException e) {
            logger.error("Error generating JWT token", e);
            throw new JWTCreationException("Error generating token", e);
        }
    }
    
    /**
     * Validates a JWT token.
     *
     * @param token the token to validate
     * @return the username from the token if validation is successful
     * @throws JWTVerificationException if the token is invalid
     */
    public String validateToken(String token) {
        try {
            Algorithm algorithm = Algorithm.HMAC256(jwtSecret);
            return JWT.require(algorithm)
                    .withIssuer("user-security-api")
                    .build()
                    .verify(token)
                    .getSubject();
        } catch (JWTVerificationException e) {
            logger.error("Error validating JWT token", e);
            throw new JWTVerificationException("Invalid token");
        }
    }
    
    /**
     * Gets the expiration date from a JWT token.
     *
     * @param token the token to get the expiration date from
     * @return the expiration date as an Instant
     * @throws JWTVerificationException if the token is invalid
     */
    public Instant getExpirationFromToken(String token) {
        try {
            Algorithm algorithm = Algorithm.HMAC256(jwtSecret);
            DecodedJWT jwt = JWT.require(algorithm)
                    .withIssuer("user-security-api")
                    .build()
                    .verify(token);
            return jwt.getExpiresAt().toInstant();
        } catch (JWTVerificationException e) {
            logger.error("Error getting expiration from JWT token", e);
            throw new JWTVerificationException("Invalid token");
        }
    }
    
    /**
     * Gets user roles from a JWT token.
     *
     * @param token the token to get the roles from
     * @return the roles as a string
     * @throws JWTVerificationException if the token is invalid
     */
    public String getRolesFromToken(String token) {
        try {
            Algorithm algorithm = Algorithm.HMAC256(jwtSecret);
            DecodedJWT jwt = JWT.require(algorithm)
                    .withIssuer("user-security-api")
                    .build()
                    .verify(token);
            return jwt.getClaim("roles").asString();
        } catch (JWTVerificationException e) {
            logger.error("Error getting roles from JWT token", e);
            throw new JWTVerificationException("Invalid token");
        }
    }
    
    /**
     * Gets the user ID from a JWT token.
     *
     * @param token the token to get the user ID from
     * @return an Optional containing the user ID, or empty if not found
     */
    public Optional<String> getUserIdFromToken(String token) {
        try {
            Algorithm algorithm = Algorithm.HMAC256(jwtSecret);
            DecodedJWT jwt = JWT.require(algorithm)
                    .withIssuer("user-security-api")
                    .build()
                    .verify(token);
            return Optional.ofNullable(jwt.getClaim("userId").asString());
        } catch (JWTVerificationException e) {
            logger.error("Error getting user ID from JWT token", e);
            return Optional.empty();
        }
    }
    
    /**
     * Refreshes a JWT token if it's valid and not expired.
     *
     * @param token the token to refresh
     * @return an Optional containing the refreshed token, or empty if the token couldn't be refreshed
     */
    public Optional<String> refreshToken(String token) {
        try {
            Algorithm algorithm = Algorithm.HMAC256(jwtSecret);
            DecodedJWT jwt = JWT.require(algorithm)
                    .withIssuer("user-security-api")
                    .build()
                    .verify(token);
            
            // Create a new token with the same details but extended expiration
            Instant issuedAt = Instant.now();
            Instant expiresAt = issuedAt.plus(expirationMinutes, ChronoUnit.MINUTES);
            
            String refreshedToken = JWT.create()
                    .withIssuer("user-security-api")
                    .withSubject(jwt.getSubject())
                    .withIssuedAt(Date.from(issuedAt))
                    .withExpiresAt(Date.from(expiresAt))
                    .withClaim("roles", jwt.getClaim("roles").asString())
                    .withClaim("userId", jwt.getClaim("userId").asString())
                    .sign(algorithm);
            
            return Optional.of(refreshedToken);
        } catch (JWTVerificationException e) {
            logger.error("Error refreshing JWT token", e);
            return Optional.empty();
        }
    }
}