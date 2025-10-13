package com.ocoelhogabriel.usersecurity.infrastructure.persistence.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.ocoelhogabriel.usersecurity.infrastructure.persistence.entity.ResourceEntity;

/**
 * Repository for managing ResourceEntity entities.
 */
@Repository
public interface ResourceRepository extends JpaRepository<ResourceEntity, Long> {

    /**
     * Find a resource by its path and HTTP method.
     *
     * @param path the resource path
     * @param method the HTTP method
     * @return the resource, if found
     */
    Optional<ResourceEntity> findByPathAndMethod(String path, String method);
    
    /**
     * Find a resource by name.
     *
     * @param name the name of the resource
     * @return the resource, if found
     */
    Optional<ResourceEntity> findByName(String name);
    
    /**
     * Find a resource by its path and HTTP method, excluding a specific ID.
     *
     * @param path the resource path
     * @param method the HTTP method
     * @param id the ID to exclude
     * @return the resource, if found
     */
    Optional<Resource> findByPathAndMethodAndIdNot(String path, String method, Long id);
    
    /**
     * Find all resources that match a specific path pattern and HTTP method.
     * This uses pattern matching to support path variables and wildcards.
     *
     * @param path the path pattern to match
     * @param method the HTTP method
     * @return list of matching resources
     */
    @Query("SELECT r FROM Resource r WHERE " +
           "(:method = r.method) AND " +
           "(r.path = :path OR " +
           "REPLACE(r.path, '**', '%') LIKE REPLACE(:path, '**', '%') OR " +
           "REPLACE(:path, '**', '%') LIKE REPLACE(r.path, '**', '%'))")
    List<Resource> findMatchingResources(@Param("path") String path, @Param("method") String method);
    
    /**
     * Find all resources by HTTP method.
     *
     * @param method the HTTP method
     * @return list of resources
     */
    List<Resource> findByMethod(String method);
}