package com.ocoelhogabriel.manager_user_security.infrastructure.security.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

// Importando apenas a interface do domínio que vamos implementar
import com.ocoelhogabriel.manager_user_security.domain.service.ResourceService;
import com.ocoelhogabriel.manager_user_security.infrastructure.security.authorization.Resource;

/**
 * Adapter class that implements the domain ResourceService interface
 * using the application ResourceService
 */
@Service
public class ResourceServiceAdapter implements ResourceService {

    @Autowired
    private com.ocoelhogabriel.manager_user_security.application.service.ResourceService appResourceService;
    
    @Override
    public Resource findByName(String name) {
        // This is a simplified implementation
        // Ideally, we would query the repository and map the domain entity to the authorization entity
        return new Resource(name, "Description for " + name);
    }
}