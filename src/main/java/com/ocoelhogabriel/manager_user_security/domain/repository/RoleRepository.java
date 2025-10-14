package com.ocoelhogabriel.manager_user_security.domain.repository;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import com.ocoelhogabriel.manager_user_security.domain.entity.Role;

/**
 * Repository interface for Role entities. Extends the generic Repository interface with Role-specific methods.
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

    /**
     * Finds roles by active status.
     *
     * @param active the active status
     * @return a list of roles with the given active status
     */
    List<Role> findByActive(boolean active);

    /**
     * Finds roles for a user.
     *
     * @param userId the user ID
     * @return a set of roles for the given user
     */
    Set<Role> findByUserId(Long userId);
}
