package com.ocoelhogabriel.manager_user_security.application.services;

import com.ocoelhogabriel.manager_user_security.domain.entities.EmpresaModel;
import java.util.List;

/**
 * Interface de serviço para Empresa
 * Aplica SOLID principles - Interface Segregation Principle
 * Segue Clean Architecture - Application Layer
 */
public interface EmpresaService {
    
    List<EmpresaModel> findAll();
    
    EmpresaModel findById(Long id);
    
    EmpresaModel save(EmpresaModel empresa);
    
    void deleteById(Long id);
    
    EmpresaModel findByCnpj(String cnpj);
    
    List<EmpresaModel> findByNome(String nome);
    
    boolean existsByCnpj(String cnpj);
}
