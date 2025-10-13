package com.ocoelhogabriel.usersecurity.infrastructure.persistence.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ocoelhogabriel.usersecurity.infrastructure.persistence.entity.RoleEntity;

/**
 * Repository for managing RoleEntity entities.
 */
@Repository
public interface RoleRepository extends JpaRepository<RoleEntity, Long> {

    /**
     * Find a role by its name.
     *
     * @param name the role name
     * @return the role, if found
     */
    Optional<RoleEntity> findByName(String name);
    
    /**
     * Find a role by name, excluding a specific ID.
     *
     * @param name the role name
     * @param id the ID to exclude
     * @return the role, if found
     */
    Optional<RoleEntity> findByNameAndIdNot(String name, Long id);
    
    /**
     * Check if a role exists with the given name.
     *
     * @param name the name to check
     * @return true if a role exists with the given name, false otherwise
     */
    boolean existsByName(String name);
    
    /**
     * Find roles by user ID.
     *
     * @param userId the user ID
     * @return list of roles
     */
    List<RoleEntity> findByUsersId(Long userId);
}