package com.ocoelhogabriel.manager_user_security.application.services;

import com.ocoelhogabriel.manager_user_security.domain.entities.AbrangenciaModel;
import java.util.List;

/**
 * Interface de serviço para Abrangência
 * Aplica SOLID principles - Interface Segregation Principle
 * Segue Clean Architecture - Application Layer
 */
public interface AbrangenciaService {
    
    List<AbrangenciaModel> findAll();
    
    AbrangenciaModel findById(Long id);
    
    AbrangenciaModel save(AbrangenciaModel abrangencia);
    
    void deleteById(Long id);
    
    List<AbrangenciaModel> findByUsuarioId(Long usuarioId);
    
    List<AbrangenciaModel> findByEmpresaId(Long empresaId);
    
    boolean existsByUsuarioIdAndEmpresaId(Long usuarioId, Long empresaId);
    
    com.ocoelhogabriel.manager_user_security.infrastructure.adapters.persistence.entities.AbrangenciaDetalhes findByAbrangenciaAndRecursoContainingAbrangencia(
            com.ocoelhogabriel.manager_user_security.infrastructure.adapters.persistence.entities.Abrangencia abrangencia, 
            Object recurso);
}
