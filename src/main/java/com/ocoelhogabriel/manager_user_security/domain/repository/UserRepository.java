package com.ocoelhogabriel.manager_user_security.domain.repository;

import java.util.Optional;

import com.ocoelhogabriel.manager_user_security.domain.entity.User;

/**
 * Repository interface for User entities. Extends the generic Repository interface with User-specific methods.
 */
public interface UserRepository extends Repository<User, Long> {

    /**
     * Finds a user by username.
     *
     * @param username the username to search for
     * @return an Optional containing the found user, or empty if not found
     */
    Optional<User> findByUsername(String username);

    /**
     * Finds a user by email.
     *
     * @param email the email to search for
     * @return an Optional containing the found user, or empty if not found
     */
    Optional<User> findByEmail(String email);

    /**
     * Checks if a user with the given username exists.
     *
     * @param username the username to check
     * @return true if a user with the given username exists, false otherwise
     */
    boolean existsByUsername(String username);

    /**
     * Checks if a user with the given email exists.
     *
     * @param email the email to check
     * @return true if a user with the given email exists, false otherwise
     */
    boolean existsByEmail(String email);

    Optional<User> findByIdWithRoles(Long userId);
}
