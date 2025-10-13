package com.ocoelhogabriel.manager_user_security.domain.repositories;

import java.util.List;
import java.util.Optional;

import com.ocoelhogabriel.manager_user_security.domain.entities.EmpresaModel;

/**
 * Repository interface do domínio para Empresa
 * Aplica Dependency Inversion Principle (DIP)
 * Define contratos sem depender de detalhes de implementação
 */
public interface EmpresaRepository {
    
    /**
     * Salva ou atualiza uma empresa
     */
    EmpresaModel save(EmpresaModel empresa);
    
    /**
     * Busca empresa por ID
     */
    Optional<EmpresaModel> findById(Long id);
    
    /**
     * Busca empresa por CNPJ
     */
    Optional<EmpresaModel> findByCnpj(String cnpj);
    
    /**
     * Busca empresas por nome
     */
    List<EmpresaModel> findByNome(String nome);
    
    /**
     * Lista todas as empresas ativas
     */
    List<EmpresaModel> findAllActive();
    
    /**
     * Remove uma empresa (soft delete)
     */
    void delete(Long id);
    
    /**
     * Verifica se existe empresa com o CNPJ
     */
    boolean existsByCnpj(String cnpj);
    
    /**
     * Verifica se existe empresa com o nome
     */
    boolean existsByNome(String nome);
    
    /**
     * Conta total de empresas ativas
     */
    long countActive();
}
