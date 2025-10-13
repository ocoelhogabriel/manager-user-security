package com.ocoelhogabriel.manager_user_security.infrastructure.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.ocoelhogabriel.manager_user_security.domain.model.User;
import com.ocoelhogabriel.manager_user_security.domain.ports.out.JwtService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Optional;

/**
 * Driven Adapter that implements the JwtService port using the auth0-jwt library.
 */
@Service
public class JwtServiceImpl implements JwtService {

    private final Algorithm algorithm;
    private final long expirationMinutes;

    public JwtServiceImpl(
        @Value("${api.security.token.secret}") final String secretKey,
        @Value("${api.security.expiration.time.minutes:1440}") final long expirationMinutes
    ) {
        this.algorithm = Algorithm.HMAC256(secretKey);
        this.expirationMinutes = expirationMinutes;
    }

    @Override
    public String generateToken(User user) {
        return JWT.create()
            .withIssuer("manager-user-security-api")
            .withSubject(user.username().value())
            .withClaim("userId", user.id().value())
            .withExpiresAt(generateExpirationDate())
            .sign(algorithm);
    }

    @Override
    public Optional<String> getUsernameFromToken(String token) {
        try {
            JWTVerifier verifier = JWT.require(algorithm)
                .withIssuer("manager-user-security-api")
                .build();

            String username = verifier.verify(token).getSubject();
            return Optional.ofNullable(username);
        } catch (JWTVerificationException exception) {
            // Invalid token (expired, wrong signature, etc.)
            return Optional.empty();
        }
    }

    private Instant generateExpirationDate() {
        return LocalDateTime.now().plusMinutes(expirationMinutes).toInstant(ZoneOffset.of("-03:00"));
    }
}
