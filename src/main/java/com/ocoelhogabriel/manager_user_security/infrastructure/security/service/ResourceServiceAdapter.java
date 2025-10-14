package com.ocoelhogabriel.manager_user_security.infrastructure.security.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ocoelhogabriel.manager_user_security.domain.service.ResourceService;
import com.ocoelhogabriel.manager_user_security.infrastructure.security.authorization.Resource;

/**
 * Adapter class that implements the domain ResourceService interface
 * using the application ResourceService
 */
@Service
public class ResourceServiceAdapter implements ResourceService {

    @Autowired
    private ResourceService appResourceService;
    
    @Override
    public Resource findByName(String name) {
        // Implementação simplificada adaptando a interface de domínio para usar o serviço de aplicação
        // Em uma implementação completa, usaríamos o appResourceService para buscar o recurso e depois mapear
        return new Resource(name, "Description for " + name);
    }
}