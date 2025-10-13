package com.ocoelhogabriel.manager_user_security.domain.repositories;

import java.util.List;
import java.util.Optional;

import com.ocoelhogabriel.manager_user_security.domain.entities.UsuarioModel;

/**
 * Repository interface do domínio para Usuario
 * Aplica Dependency Inversion Principle (DIP)
 * Define contratos sem depender de detalhes de implementação
 */
public interface UsuarioRepository {
    
    /**
     * Salva ou atualiza um usuário
     */
    UsuarioModel save(UsuarioModel usuario);
    
    /**
     * Busca usuário por ID
     */
    Optional<UsuarioModel> findById(Long id);
    
    /**
     * Busca usuário por login
     */
    Optional<UsuarioModel> findByLogin(String login);
    
    /**
     * Busca usuário por email
     */
    Optional<UsuarioModel> findByEmail(String email);
    
    /**
     * Lista todos os usuários ativos
     */
    List<UsuarioModel> findAllActive();
    
    /**
     * Remove um usuário (soft delete)
     */
    void delete(Long id);
    
    /**
     * Verifica se existe usuário com o login
     */
    boolean existsByLogin(String login);
    
    /**
     * Verifica se existe usuário com o email
     */
    boolean existsByEmail(String email);
    
    /**
     * Conta total de usuários ativos
     */
    long countActive();
    
    /**
     * Ativa um usuário
     */
    void activate(Long id);
    
    /**
     * Desativa um usuário
     */
    void deactivate(Long id);
    
    /**
     * Altera senha do usuário
     */
    void changePassword(Long id, String newPassword);
}
