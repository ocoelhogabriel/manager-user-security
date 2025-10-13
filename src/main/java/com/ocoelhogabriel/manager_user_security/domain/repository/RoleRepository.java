package com.ocoelhogabriel.manager_user_security.domain.repository;

import java.util.Optional;

import com.ocoelhogabriel.manager_user_security.domain.entity.Role;

/**
 * Repository interface for Role entities.
 * Extends the generic Repository interface with Role-specific methods.
 */
public interface RoleRepository extends Repository<Role, Long> {
    
    /**
     * Finds a role by name.
     *
     * @param name the name of the role to find
     * @return an Optional containing the found role, or empty if not found
     */
    Optional<Role> findByName(String name);
    
    /**
     * Checks if a role with the given name exists.
     *
     * @param name the name of the role to check
     * @return true if a role with the given name exists, false otherwise
     */
    boolean existsByName(String name);
}
