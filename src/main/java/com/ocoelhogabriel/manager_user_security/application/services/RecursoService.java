package com.ocoelhogabriel.manager_user_security.application.services;

import com.ocoelhogabriel.manager_user_security.domain.entities.RecursoModel;
import java.util.List;

/**
 * Interface de serviço para Recurso
 * Aplica SOLID principles - Interface Segregation Principle
 * Segue Clean Architecture - Application Layer
 */
public interface RecursoService {
    
    List<RecursoModel> findAll();
    
    RecursoModel findById(Long id);
    
    RecursoModel save(RecursoModel recurso);
    
    void deleteById(Long id);
    
    List<RecursoModel> findByTipo(String tipo);
    
    boolean existsByNome(String nome);
    
    // Métodos para compatibilidade com código legado
    Object findByIdEntity(String id);
}
