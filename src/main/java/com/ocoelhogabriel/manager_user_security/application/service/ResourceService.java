package com.ocoelhogabriel.manager_user_security.application.service;

import java.util.List;

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
     * Delete a resource.
     *
     * @param id the ID of the resource to delete
     * @throws com.ocoelhogabriel.manager_user_security.domain.exception.ResourceNotFoundException if resource not found
     */
    void delete(Long id);

    /**
     * Find resources by path pattern and method.
     *
     * @param path the path pattern to match
     * @param method the HTTP method
     * @return matching resources
     */
    List<Resource> findByPathAndMethod(String path, String method);
}
