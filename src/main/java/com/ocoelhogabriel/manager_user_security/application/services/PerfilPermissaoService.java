package com.ocoelhogabriel.manager_user_security.application.services;

import com.ocoelhogabriel.manager_user_security.domain.entities.PerfilModel;

/**
 * Interface de serviço para Perfil Permissão
 * Aplica SOLID principles - Interface Segregation Principle
 * Segue Clean Architecture - Application Layer
 */
public interface PerfilPermissaoService {
    
    boolean hasPermission(Long perfilId, Long recursoId);
    
    void grantPermission(Long perfilId, Long recursoId);
    
    void revokePermission(Long perfilId, Long recursoId);
    
    void revokeAllPermissions(Long perfilId);
    
    PerfilModel findByIdPerfilEntity(String perfil);
    
    PerfilModel findByNome(String nome);
    
    PerfilModel save(PerfilModel perfil);
}
