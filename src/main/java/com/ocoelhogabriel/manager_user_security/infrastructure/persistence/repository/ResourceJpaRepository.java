package com.ocoelhogabriel.manager_user_security.infrastructure.persistence.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.ocoelhogabriel.manager_user_security.infrastructure.persistence.entity.ResourceEntity;

/**
 * Repository for managing ResourceEntity entities.
 */
@Repository
public interface ResourceJpaRepository extends JpaRepository<ResourceEntity, Long> {

    /**
     * Find a resource by its URL pattern and HTTP method.
     *
     * @param urlPattern the resource URL pattern
     * @param method the HTTP method
     * @return the resource, if found
     */
    Optional<ResourceEntity> findByUrlPatternAndMethod(String urlPattern, String method);

    /**
     * Find a resource by name.
     *
     * @param name the name of the resource
     * @return the resource, if found
     */
    Optional<ResourceEntity> findByName(String name);
    
    /**
     * Find resources by version.
     *
     * @param version the version
     * @return the list of resources with the specified version
     */
    List<ResourceEntity> findByVersion(String version);

    /**
     * Check if a resource with the given URL pattern exists.
     *
     * @param urlPattern the URL pattern to check
     * @return true if a resource with the URL pattern exists, false otherwise
     */
    boolean existsByUrlPattern(String urlPattern);
}
