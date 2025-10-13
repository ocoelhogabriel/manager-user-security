package com.ocoelhogabriel.manager_user_security.infrastructure.security.jwt;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.Objects;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.exceptions.TokenExpiredException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.auth0.jwt.interfaces.JWTVerifier;

import com.ocoelhogabriel.manager_user_security.domain.entity.User;
import com.ocoelhogabriel.manager_user_security.domain.valueobject.TokenDetails;

/**
 * Service for JWT token generation, validation and parsing.
 */
@Component
public class JwtService {

    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS");

    @Value("${api.security.token.secret}")
    private String secret;

    @Value("${api.security.expiration.time.minutes}")
    private long expirationTimeMinutes;

    /**
     * Generates a JWT token for a user
     * 
     * @param user The user details
     * @return TokenDetails containing the generated token and related information
     * @throws JWTCreationException if token generation fails
     */
    public TokenDetails generateToken(User user) {
        try {
            Algorithm algorithm = Algorithm.HMAC256(secret);
            Instant expirationInstant = generateExpirationDate();
            String token = JWT.create()
                    .withIssuer("auth-api")
                    .withSubject(user.getUsername())
                    .withExpiresAt(Date.from(expirationInstant))
                    .withClaim("role", user.getRole().getName())
                    .sign(algorithm);

            return new TokenDetails(
                    user.getUsername(),
                    token,
                    formatCurrentDateTime(),
                    formatInstantToString(expirationInstant));
        } catch (JWTCreationException | IllegalArgumentException e) {
            throw new JWTCreationException("Error while generating token", e);
        }
    }

    /**
     * Validates a JWT token
     * 
     * @param token The JWT token to validate
     * @return The username from the token
     * @throws AccessDeniedException if token validation fails
     */
    public String validateToken(String token) {
        try {
            Algorithm algorithm = Algorithm.HMAC256(secret);
            JWTVerifier verifier = JWT.require(algorithm)
                    .withIssuer("auth-api")
                    .build();
            return verifier.verify(token).getSubject();
        } catch (TokenExpiredException e) {
            throw new AccessDeniedException("Token has expired", e);
        } catch (JWTVerificationException e) {
            throw new AccessDeniedException("Token verification failed", e);
        }
    }

    /**
     * Validates a token or refreshes it if expired
     * 
     * @param token The JWT token to validate or refresh
     * @return TokenDetails containing the token and related information
     * @throws AccessDeniedException if token validation fails
     */
    public TokenDetails validateOrRefreshToken(String token) {
        try {
            Algorithm algorithm = Algorithm.HMAC256(secret);
            JWTVerifier verifier = JWT.require(algorithm)
                    .withIssuer("auth-api")
                    .build();
            DecodedJWT jwt = verifier.verify(token);

            return new TokenDetails(
                    jwt.getSubject(),
                    token,
                    formatCurrentDateTime(),
                    formatInstantToString(jwt.getExpiresAt().toInstant()));
        } catch (TokenExpiredException exception) {
            return refreshToken(token);
        } catch (JWTVerificationException exception) {
            throw new AccessDeniedException("Token verification failed");
        }
    }

    /**
     * Refreshes an expired JWT token
     * 
     * @param expiredToken The expired token
     * @return TokenDetails containing the new token
     * @throws AccessDeniedException if token refresh fails
     */
    public TokenDetails refreshToken(String expiredToken) {
        try {
            Algorithm refreshAlgorithm = Algorithm.HMAC256(secret);
            JWTVerifier verifier = JWT.require(refreshAlgorithm)
                    .withIssuer("auth-api")
                    .build();
            DecodedJWT decodedToken = verifier.verify(expiredToken);

            String username = decodedToken.getSubject();
            User user = new User();
            user.setUsername(username);
            return generateToken(user);
        } catch (TokenExpiredException e) {
            throw new AccessDeniedException("Refresh token has expired");
        } catch (JWTVerificationException e) {
            throw new AccessDeniedException("Refresh token verification failed");
        }
    }

    /**
     * Gets the expiration date from a JWT token
     * 
     * @param token The JWT token
     * @return The expiration date as an Instant
     * @throws JWTVerificationException if token verification fails
     */
    public Instant getExpirationDateFromToken(String token) {
        try {
            Algorithm algorithm = Algorithm.HMAC256(secret);
            JWTVerifier verifier = JWT.require(algorithm)
                    .withIssuer("auth-api")
                    .build();
            DecodedJWT jwt = verifier.verify(token);
            return jwt.getExpiresAt().toInstant();
        } catch (JWTVerificationException exception) {
            throw new JWTVerificationException(exception.getMessage());
        }
    }

    /**
     * Gets the role claim from a token
     * 
     * @param token The JWT token
     * @return The role claim value
     */
    public String getRoleFromToken(String token) {
        try {
            Algorithm algorithm = Algorithm.HMAC256(secret);
            JWTVerifier verifier = JWT.require(algorithm)
                    .withIssuer("auth-api")
                    .build();
            DecodedJWT jwt = verifier.verify(token);
            return jwt.getClaim("role").asString();
        } catch (JWTVerificationException exception) {
            throw new JWTVerificationException("Failed to extract role from token");
        }
    }

    /**
     * Converts a Date to formatted string
     * 
     * @param date The date to convert
     * @return Formatted date string
     */
    public static String formatDateToString(Date date) {
        Objects.requireNonNull(date, "Date cannot be null");
        LocalDateTime localDateTime = LocalDateTime.ofInstant(date.toInstant(), ZoneId.systemDefault());
        return DATE_FORMATTER.format(localDateTime);
    }

    /**
     * Formats the current date time
     * 
     * @return Formatted current date time string
     */
    private String formatCurrentDateTime() {
        return DATE_FORMATTER.format(LocalDateTime.now());
    }

    /**
     * Formats an Instant to string
     * 
     * @param instant The instant to format
     * @return Formatted instant string
     */
    private String formatInstantToString(Instant instant) {
        return DATE_FORMATTER.format(instant.atZone(ZoneId.systemDefault()));
    }

    /**
     * Generates an expiration date based on the configured expiration time
     * 
     * @return The expiration date as an Instant
     */
    private Instant generateExpirationDate() {
        return Instant.now().plusSeconds(expirationTimeMinutes * 60);
    }
}