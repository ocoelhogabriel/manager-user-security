package com.ocoelhogabriel.manager_user_security.domain.repository;

import java.util.List;
import java.util.Optional;

import com.ocoelhogabriel.manager_user_security.domain.entity.Resource;

/**
 * Repository interface for Resource entities.
 * Extends the generic Repository interface with Resource-specific methods.
 */
public interface ResourceRepository extends Repository<Resource, Long> {
    
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

    /**
     * Finds a resource by URL pattern and HTTP method.
     *
     * @param urlPattern the URL pattern of the resource
     * @param method the HTTP method
     * @return an Optional containing the found resource, or empty if not found
     */
    Optional<Resource> findByUrlPatternAndMethod(String urlPattern, String method);

    /**
     * Finds resources that match a given URL and HTTP method.
     * This method should implement URL pattern matching logic to find all resources
     * whose URL patterns match the given URL.
     *
     * @param url the URL to match against resource patterns
     * @param method the HTTP method to match
     * @return a list of matching resources
     */
    List<Resource> findMatchingResources(String url, String method);
}
