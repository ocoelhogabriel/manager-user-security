package com.ocoelhogabriel.manager_user_security.domain.services;

import com.ocoelhogabriel.manager_user_security.domain.entities.EmpresaModel;
import java.util.List;

public interface EmpresaService {
    
    List<EmpresaModel> findAll();
    
    EmpresaModel findById(Long id);
    
    EmpresaModel save(EmpresaModel empresa);
    
    void deleteById(Long id);
    
    EmpresaModel findByCnpj(String cnpj);
    
    List<EmpresaModel> findByNome(String nome);
    
    boolean existsByCnpj(String cnpj);
}