package com.ocoelhogabriel.usersecurity.domain.repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import com.ocoelhogabriel.usersecurity.domain.entity.Resource;

/**
 * Repository interface for Resource entities.
 * Extends the generic Repository interface with Resource-specific methods.
 */
public interface ResourceRepository extends Repository<Resource, UUID> {
    
    /**
     * Finds a resource by name.
     *
     * @param name the name of the resource to find
     * @return an Optional containing the found resource, or empty if not found
     */
    Optional<Resource> findByName(String name);
    
    /**
     * Finds resources by version.
     *
     * @param version the version to search for
     * @return a list of resources for the given version
     */
    List<Resource> findByVersion(String version);
    
    /**
     * Checks if a resource with the given URL pattern exists.
     *
     * @param urlPattern the URL pattern to check
     * @return true if a resource with the given URL pattern exists, false otherwise
     */
    boolean existsByUrlPattern(String urlPattern);
    
    /**
     * Finds a resource that matches the given URL.
     * This method should implement URL pattern matching logic.
     *
     * @param url the URL to match
     * @return an Optional containing the found resource, or empty if not found
     */
    Optional<Resource> findByMatchingUrl(String url);
}