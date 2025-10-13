package com.ocoelhogabriel.manager_user_security.infrastructure.security.jwt;

import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.exceptions.TokenExpiredException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.auth0.jwt.interfaces.JWTVerifier;
import com.ocoelhogabriel.manager_user_security.application.dto.TokenDetails;
import com.ocoelhogabriel.manager_user_security.domain.entity.User;
import com.ocoelhogabriel.manager_user_security.infrastructure.security.service.UserDetailsServiceImpl;

/**
 * Service responsible for JWT token operations.
 */
@Component
public class JwtService {

    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS");
    private static final String TOKEN_ISSUER = "auth-api";
    private static final String ROLE_CLAIM = "role";

    @Value("${api.security.token.secret}")
    private String secret;

    @Value("${api.security.expiration.time.minutes}")
    private long expirationTimeInMinutes;
    
    private final UserDetailsServiceImpl userDetailsService;
    
    public JwtService(UserDetailsServiceImpl userDetailsService) {
        this.userDetailsService = userDetailsService;
    }
    
    /**
     * Generates a JWT token for a user.
     * 
     * @param user the user for whom to generate the token
     * @return token details containing the token and expiration information
     */
    public TokenDetails generateToken(User user) {
        try {
            Algorithm algorithm = Algorithm.HMAC256(secret);
            Instant expirationInstant = calculateExpirationTime();
            
            // Get the role name from the first role if available
            String roleName = user.getRoles().stream()
                    .findFirst()
                    .map(role -> role.getName())
                    .orElse("USER");
                    
            String token = JWT.create()
                    .withIssuer(TOKEN_ISSUER)
                    .withSubject(user.getUsername())
                    .withExpiresAt(Date.from(expirationInstant))
                    .withClaim(ROLE_CLAIM, roleName)
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
     * Validates a token or refreshes it if it's expired.
     * 
     * @param token the token to validate or refresh
     * @return token details with the current or refreshed token
     * @throws AccessDeniedException if the token is invalid
     */
    public TokenDetails validateOrRefreshToken(String token) {
        try {
            Algorithm algorithm = Algorithm.HMAC256(secret);
            JWTVerifier verifier = JWT.require(algorithm)
                    .withIssuer(TOKEN_ISSUER)
                    .build();
            DecodedJWT jwt = verifier.verify(token);
            
            return new TokenDetails(
                    jwt.getSubject(),
                    token,
                    getCurrentDateTime(),
                    formatExpirationDate(jwt.getExpiresAt().toInstant())
            );
        } catch (TokenExpiredException exception) {
            return refreshToken(token);
        } catch (JWTVerificationException exception) {
            throw new AccessDeniedException("Token verification failed");
        }
    }
    
    /**
     * Refreshes an expired token.
     * 
     * @param expiredToken the expired token
     * @return new token details
     * @throws AccessDeniedException if the token is invalid
     */
    public TokenDetails refreshToken(String expiredToken) {
        try {
            // Even though the token is expired, we can still read the claims
            DecodedJWT jwt = JWT.decode(expiredToken);
            String username = jwt.getSubject();
            
            // Load user details to create a new token
            UserDetails userDetails = userDetailsService.loadUserByUsername(username);
            User user = new User();
            user.setUsername(username);
            
            // Generate a new token
            return generateToken(user);
        } catch (JWTVerificationException e) {
            throw new AccessDeniedException("Refresh token verification failed");
        }
    }
    
    /**
     * Creates an authentication object from a token.
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
     * Gets the expiration date from a token.
     * 
     * @param token the JWT token
     * @return the expiration instant
     */
    public Instant getExpirationDateFromToken(String token) {
        try {
            Algorithm algorithm = Algorithm.HMAC256(secret);
            JWTVerifier verifier = JWT.require(algorithm)
                    .withIssuer(TOKEN_ISSUER)
                    .build();
            DecodedJWT jwt = verifier.verify(token);
            return jwt.getExpiresAt().toInstant();
        } catch (JWTVerificationException exception) {
            throw new JWTVerificationException(exception.getMessage());
        }
    }
    
    /**
     * Extracts the username from a token without validation.
     * 
     * @param token the JWT token
     * @return the username
     */
    public String extractUsername(String token) {
        try {
            DecodedJWT jwt = JWT.decode(token);
            return jwt.getSubject();
        } catch (JWTVerificationException exception) {
            return null;
        }
    }
    
    /**
     * Calculates the token expiration time.
     * 
     * @return expiration instant
     */
    private Instant calculateExpirationTime() {
        return Instant.now().plusSeconds(expirationTimeInMinutes * 60);
    }
    
    /**
     * Gets the current date and time as a formatted string.
     * 
     * @return formatted date-time string
     */
    private String getCurrentDateTime() {
        return DATE_FORMATTER.format(Instant.now().atZone(ZoneId.systemDefault()));
    }
    
    /**
     * Formats an expiration instant to a string.
     * 
     * @param expirationInstant the expiration instant
     * @return formatted date-time string
     */
    private String formatExpirationDate(Instant expirationInstant) {
        return DATE_FORMATTER.format(expirationInstant.atZone(ZoneId.systemDefault()));
    }
}
