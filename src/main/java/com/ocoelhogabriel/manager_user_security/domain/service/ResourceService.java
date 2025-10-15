package com.ocoelhogabriel.manager_user_security.domain.service;

import java.util.List;
import java.util.Optional;

import com.ocoelhogabriel.manager_user_security.domain.entity.Resource;

/**
 * Service interface for managing resources.
 */
public interface ResourceService {

    /**
     * Find all resources.
     *
     * @return a list of resources
     */
    List<Resource> findAll();

    /**
     * Find a resource by ID.
     *
     * @param id the resource ID
     * @return the resource
     * @throws com.ocoelhogabriel.manager_user_security.domain.exception.ResourceNotFoundException if resource not found
     */
    Resource findById(Long id);

    /**
     * Find a resource by name.
     *
     * @param name the resource name
     * @return an Optional containing the resource or empty if not found
     */
    Optional<Resource> findByName(String name);

    /**
     * Create a new resource.
     *
     * @param resource the resource to create
     * @return the created resource
     * @throws com.ocoelhogabriel.manager_user_security.domain.exception.DuplicateResourceException if resource already exists
     */
    Resource create(Resource resource);

    /**
     * Update an existing resource.
     *
     * @param resource the resource to update
     * @return the updated resource
     * @throws com.ocoelhogabriel.manager_user_security.domain.exception.ResourceNotFoundException if resource not found
     */
    Resource update(Resource resource);

    /**
     * Delete a resource by ID.
     *
     * @param id the resource ID
     * @throws com.ocoelhogabriel.manager_user_security.domain.exception.ResourceNotFoundException if resource not found
     */
    void delete(Long id);

    /**
     * Find resources that match a URL pattern and HTTP method.
     *
     * @param url the URL pattern to match
     * @param method the HTTP method
     * @return a list of matching resources
     */
    List<Resource> findMatchingResources(String url, String method);

    /**
     * Find resources by version.
     * @param version the version to search for
     * @return a list of resources for the given version
     */
    List<Resource> findByVersion(String version);

    /**
     * Find a resource by URL pattern.
     * @param urlPattern the URL pattern to search for
     * @return an Optional containing the resource or empty if not found
     */
    Optional<Resource> findByUrlPattern(String urlPattern);

    /**
     * Checks if a resource with the given URL pattern exists.
     * @param urlPattern the URL pattern to check
     * @return true if a resource with the given URL pattern exists, false otherwise
     */
    boolean existsByUrlPattern(String urlPattern);
}
