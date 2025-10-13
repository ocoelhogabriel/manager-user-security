package com.ocoelhogabriel.manager_user_security.domain.service;

import com.ocoelhogabriel.manager_user_security.infrastructure.security.authorization.Resource;

/**
 * Service interface for managing resources
 */
public interface ResourceService {
    
    /**
     * Find resource by name
     * 
     * @param name The resource name
     * @return The resource with the given name
     */
    Resource findByName(String name);
}