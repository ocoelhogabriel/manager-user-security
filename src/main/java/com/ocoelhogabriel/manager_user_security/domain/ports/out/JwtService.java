package com.ocoelhogabriel.manager_user_security.domain.ports.out;

import com.ocoelhogabriel.manager_user_security.domain.model.User;

import java.util.Optional;

/**
 * Output Port for JWT (JSON Web Token) operations.
 * Defines the contract for generating and validating tokens.
 */
public interface JwtService {

    /**
     * Generates a JWT for the given user.
     *
     * @param user The user entity.
     * @return The generated JWT string.
     */
    String generateToken(User user);

    /**
     * Validates the given JWT and extracts the subject (username).
     *
     * @param token The JWT string.
     * @return An Optional containing the username if the token is valid, otherwise empty.
     */
    Optional<String> getUsernameFromToken(String token);
}
