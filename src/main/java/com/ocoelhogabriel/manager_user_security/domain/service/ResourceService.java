package com.ocoelhogabriel.manager_user_security.domain.service;

import com.ocoelhogabriel.manager_user_security.domain.entity.Resource;

/**
 * Service interface for managing resources
 */
public interface ResourceService {
    
    /**
     * Find a resource by its name
     * 
     * @param name The resource name
     * @return The found resource
     */
    Resource findByName(String name);
}