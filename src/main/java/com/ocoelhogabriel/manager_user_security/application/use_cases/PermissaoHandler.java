package com.ocoelhogabriel.manager_user_security.application.use_cases;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Configuration;

import com.ocoelhogabriel.manager_user_security.application.services.PerfilPermissaoService;
import com.ocoelhogabriel.manager_user_security.domain.entities.PerfilModel;
import com.ocoelhogabriel.manager_user_security.domain.entities.PermissaoModel;

@Configuration
public class PermissaoHandler {
    
    private static final Logger LOGGER = LoggerFactory.getLogger(PermissaoHandler.class);
    
    private final PerfilPermissaoService perfilPermissaoService;
    
    /**
     * Constructor with dependency injection
     * 
     * @param perfilPermissaoService Profile and permission service
     */
    public PermissaoHandler(PerfilPermissaoService perfilPermissaoService) {
        this.perfilPermissaoService = perfilPermissaoService;
    }
    
    /**
     * Checks if the profile has permission to access the URL with the specified method
     * 
     * @param perfil - The profile to check
     * @param urlValidator - The URL validator containing the URL to be checked
     * @param method - The HTTP method (GET, POST, PUT, DELETE, etc.)
     * @return true if the profile has permission, false otherwise
     */
    public boolean checkPermission(String perfil, URLValidator urlValidator, String method) {
        if (perfil == null || urlValidator == null || method == null) {
            LOGGER.warn("Invalid parameters in permission verification");
            return false;
        }
        
        try {
            // Find the profile by name
            PerfilModel perfilModel = perfilPermissaoService.findByNome(perfil);
            
            if (perfilModel == null) {
                LOGGER.warn("Profile not found: {}", perfil);
                return false;
            }
            
            // Check if it's an administrative profile (can have full access)
            if (isAdminProfile(perfilModel)) {
                return true;
            }
            
            // If the validator has no mapped resource, it cannot validate permission
            if (urlValidator.getRecursoMapEnum() == null) {
                LOGGER.warn("Resource not mapped for URL");
                return false;
            }
            
            // Verificar permissões específicas com base no método HTTP
            return perfilModel.getPermissoes() != null && 
                   perfilModel.getPermissoes().stream()
                       .anyMatch(permissao -> hasPermission(permissao, urlValidator, method));
                   
        } catch (Exception e) {
            LOGGER.error("Error checking permission: {}", e.getMessage(), e);
            return false;
        }
    }
    
    /**
     * Checks if a specific permission allows access to the resource with the specified method
     * 
     * @param permissao The permission to check
     * @param urlValidator The URL validator
     * @param method The HTTP method
     * @return true if permission is granted, false otherwise
     */
    private boolean hasPermission(PermissaoModel permissao, URLValidator urlValidator, String method) {
        // Verificar se a permissão se aplica ao recurso da URL
        if (permissao.getRecurso() != urlValidator.getRecursoMapEnum()) {
            return false;
        }
        
        // Check permission based on HTTP method
        switch (method.toUpperCase()) {
            case "GET":
                // For GET, we need to check if it's a listing or specific search
                if (urlValidator.getMessage().contains("FIND")) {
                    return permissao.getBuscar() != null && permissao.getBuscar() == 1;
                } else {
                    return permissao.getListar() != null && permissao.getListar() == 1;
                }
            case "POST":
                return permissao.getCriar() != null && permissao.getCriar() == 1;
            case "PUT":
                return permissao.getEditar() != null && permissao.getEditar() == 1;
            case "DELETE":
                return permissao.getDeletar() != null && permissao.getDeletar() == 1;
            default:
                LOGGER.warn("HTTP method not supported: {}", method);
                return false;
        }
    }
    
    /**
     * Checks if it's an administrative profile with full access
     * 
     * @param perfilModel The profile to check
     * @return true if it's an administrative profile, false otherwise
     */
    private boolean isAdminProfile(PerfilModel perfilModel) {
        return "ADMIN".equalsIgnoreCase(perfilModel.getNome()) || 
               "ADMINISTRATOR".equalsIgnoreCase(perfilModel.getNome());
    }
}