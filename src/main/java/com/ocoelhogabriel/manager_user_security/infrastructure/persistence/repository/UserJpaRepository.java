package com.ocoelhogabriel.manager_user_security.infrastructure.persistence.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.ocoelhogabriel.manager_user_security.infrastructure.persistence.entity.UserEntity;

/**
 * JPA repository for UserEntity.
 */
@Repository
public interface UserJpaRepository extends JpaRepository<UserEntity, Long> {
    
    /**
     * Find a user by username.
     * 
     * @param username the username to search for
     * @return an Optional containing the user if found, empty otherwise
     */
    Optional<UserEntity> findByUsername(String username);
    
    /**
     * Find a user by email.
     * 
     * @param email the email to search for
     * @return an Optional containing the user if found, empty otherwise
     */
    Optional<UserEntity> findByEmail(String email);
    
    /**
     * Check if a user exists with the given username.
     * 
     * @param username the username to check
     * @return true if a user exists with the given username, false otherwise
     */
    boolean existsByUsername(String username);
    
    /**
     * Check if a user exists with the given email.
     * 
     * @param email the email to check
     * @return true if a user exists with the given email, false otherwise
     */
    boolean existsByEmail(String email);
    
    /**
     * Find a user by ID and eagerly fetch its roles.
     * 
     * @param id the user ID to search for
     * @return the user with eagerly fetched roles if found, null otherwise
     */
    @Query("SELECT u FROM UserEntity u LEFT JOIN FETCH u.roles WHERE u.id = :id")
    Optional<UserEntity> findByIdWithRoles(@Param("id") Long id);
}
