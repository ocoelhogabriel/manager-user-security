package com.ocoelhogabriel.manager_user_security.infrastructure.security.jwt;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class JwtTokenProvider {

    @Value("${api.security.token.secret}")
    private String secret;

    @Value("${api.security.expiration.time.minutes}")
    private long expirationInMinutes;

    public String generateToken(String username) {
        Date now = new Date();
        long expirationInMilliseconds = expirationInMinutes * 60 * 1000;
        Date expiryDate = new Date(now.getTime() + expirationInMilliseconds);

        return JWT.create()
                .withSubject(username)
                .withIssuedAt(now)
                .withExpiresAt(expiryDate)
                .sign(Algorithm.HMAC256(secret));
    }

    public boolean validateToken(String token) {
        try {
            Algorithm algorithm = Algorithm.HMAC256(secret);
            JWTVerifier verifier = JWT.require(algorithm).build();
            verifier.verify(token);
            return true;
        } catch (JWTVerificationException exception) {
            // Invalid signature or claims
            return false;
        }
    }

    public String getUsernameFromToken(String token) {
        DecodedJWT decodedJWT = JWT.decode(token);
        return decodedJWT.getSubject();
    }

    public DecodedJWT decodeToken(String token) {
        return JWT.decode(token);
    }
}
