package com.ocoelhogabriel.manager_user_security.domain.ports.out;

/**
 * Interface (Driven Port) for a password hashing service.
 * This defines the contract for hashing and verifying passwords, abstracting the specific implementation.
 */
public interface PasswordHashingService {

    /**
     * Hashes a raw password.
     *
     * @param rawPassword The plain-text password.
     * @return The hashed password.
     */
    String hash(String rawPassword);

    /**
     * Verifies if a raw password matches a hashed password.
     *
     * @param rawPassword The plain-text password.
     * @param hashedPassword The hashed password to compare against.
     * @return true if the passwords match, false otherwise.
     */
    boolean matches(String rawPassword, String hashedPassword);
}
