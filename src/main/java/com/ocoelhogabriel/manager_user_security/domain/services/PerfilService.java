package com.ocoelhogabriel.manager_user_security.domain.services;

import com.ocoelhogabriel.manager_user_security.domain.entities.PerfilModel;
import com.ocoelhogabriel.manager_user_security.domain.entities.PermissaoModel;

public interface PerfilService {
    
    PerfilModel findByNome(String nome);
    
    PerfilModel createOrUpdate(PerfilModel perfil);
    
    PermissaoModel findByPerfilAndRecurso(Long perfilId, Long recursoId);
    
    void savePermissao(Long perfilId, PermissaoModel permissao);
    
    boolean hasPermission(Long perfilId, Long recursoId);
    
    void grantPermission(Long perfilId, Long recursoId);
    
    void revokePermission(Long perfilId, Long recursoId);
    
    void revokeAllPermissions(Long perfilId);
}