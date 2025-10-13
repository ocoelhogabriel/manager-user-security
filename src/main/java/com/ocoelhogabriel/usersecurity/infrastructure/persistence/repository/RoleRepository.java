package com.ocoelhogabriel.usersecurity.infrastructure.persistence.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ocoelhogabriel.usersecurity.domain.entity.Role;

/**
 * Repository for managing Role entities.
 */
@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {

    /**
     * Find a role by its name.
     *
     * @param name the role name
     * @return the role, if found
     */
    Optional<Role> findByName(String name);
    
    /**
     * Find a role by name, excluding a specific ID.
     *
     * @param name the role name
     * @param id the ID to exclude
     * @return the role, if found
     */
    Optional<Role> findByNameAndIdNot(String name, Long id);
    
    /**
     * Find roles by user ID.
     *
     * @param userId the user ID
     * @return list of roles
     */
    List<Role> findByUsersId(Long userId);
}