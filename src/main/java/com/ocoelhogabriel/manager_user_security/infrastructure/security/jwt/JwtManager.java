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
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
public class JwtManager {

    private static final String TOKEN_ISSUER = "user-security-api";
    private static final String ROLES_CLAIM = "roles";
    private static final String USER_ID_CLAIM = "userId";

    @Value("${api.security.token.secret}")
    private String secret;

    @Value("${api.security.expiration.time.minutes}")
    private long expirationTimeInMinutes;

    private final UserDetailsServiceImpl userDetailsService;

    public JwtManager(UserDetailsServiceImpl userDetailsService) {
        this.userDetailsService = userDetailsService;
    }

    public TokenDetails generateToken(User user) {
        try {
            Algorithm algorithm = Algorithm.HMAC256(secret);
            Instant now = Instant.now();
            Instant expirationInstant = now.plusSeconds(expirationTimeInMinutes * 60);

            String roles = user.getRoles().stream()
                    .map(Role::getName)
                    .collect(Collectors.joining(","));

            String token = JWT.create()
                    .withIssuer(TOKEN_ISSUER)
                    .withSubject(user.getUsername())
                    .withIssuedAt(Date.from(now))
                    .withExpiresAt(Date.from(expirationInstant))
                    .withClaim(ROLES_CLAIM, roles)
                    .withClaim(USER_ID_CLAIM, user.getId().toString())
                    .sign(algorithm);

            return new TokenDetails(
                    user.getUsername(),
                    token,
                    LocalDateTime.ofInstant(now, ZoneId.systemDefault()),
                    LocalDateTime.ofInstant(expirationInstant, ZoneId.systemDefault())
            );
        } catch (JWTCreationException | IllegalArgumentException e) {
            throw new JWTCreationException("Error while generating token", e);
        }
    }

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

    public Optional<TokenDetails> refreshToken(String token) {
        try {
            Algorithm algorithm = Algorithm.HMAC256(secret);
            JWTVerifier verifier = JWT.require(algorithm)
                    .withIssuer(TOKEN_ISSUER)
                    .build();
            DecodedJWT decodedJWT = verifier.verify(token);
            String username = decodedJWT.getSubject();
            UserDetails userDetails = userDetailsService.loadUserByUsername(username);
            User user = (User) userDetails; // Assuming UserDetailsImpl returns your domain User
            return Optional.of(generateToken(user));
        } catch (TokenExpiredException e) {
            // If token is expired, we can still refresh it
            DecodedJWT decodedJWT = JWT.decode(token);
            String username = decodedJWT.getSubject();
            UserDetails userDetails = userDetailsService.loadUserByUsername(username);
            // This assumes your UserDetails implementation can be cast to your domain User
            if (userDetails instanceof User) {
                return Optional.of(generateToken((User) userDetails));
            } else {
                 // Or fetch the user from the database if the cast is not possible
                 // This part depends on your UserDetailsServiceImpl implementation
                 return Optional.empty(); 
            }
        } catch (JWTVerificationException e) {
            return Optional.empty();
        }
    }

    public Authentication getAuthentication(String token) {
        String username = validateToken(token);
        UserDetails userDetails = userDetailsService.loadUserByUsername(username);
        return new UsernamePasswordAuthenticationToken(userDetails, "", userDetails.getAuthorities());
    }

    public Optional<String> getUserIdFromToken(String token) {
        try {
            DecodedJWT jwt = JWT.decode(token);
            return Optional.ofNullable(jwt.getClaim(USER_ID_CLAIM).asString());
        } catch (JWTVerificationException e) {
            return Optional.empty();
        }
    }
}
