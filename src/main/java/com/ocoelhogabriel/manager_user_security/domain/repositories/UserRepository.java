package com.ocoelhogabriel.manager_user_security.domain.repositories;

import java.util.List;
import java.util.Optional;

import com.ocoelhogabriel.manager_user_security.domain.entities.User;
import com.ocoelhogabriel.manager_user_security.domain.value_objects.Email;
import com.ocoelhogabriel.manager_user_security.domain.value_objects.UserId;
import com.ocoelhogabriel.manager_user_security.domain.value_objects.Username;

/**
 * Repository interface do domínio para User
 * Aplica Dependency Inversion Principle (DIP)
 * Define contratos sem depender de detalhes de implementação
 */
public interface UserRepository {
    
    /**
     * Salva ou atualiza um usuário
     */
    User save(User user);
    
    /**
     * Busca usuário por ID
     */
    Optional<User> findById(UserId id);
    
    /**
     * Busca usuário por username
     */
    Optional<User> findByUsername(Username username);
    
    /**
     * Busca usuário por email
     */
    Optional<User> findByEmail(Email email);
    
    /**
     * Lista todos os usuários ativos
     */
    List<User> findAllActive();
    
    /**
     * Lista usuários com paginação
     */
    List<User> findWithPagination(int page, int size);
    
    /**
     * Remove um usuário (soft delete)
     */
    void delete(UserId id);
    
    /**
     * Verifica se existe usuário com o username
     */
    boolean existsByUsername(Username username);
    
    /**
     * Verifica se existe usuário com o email
     */
    boolean existsByEmail(Email email);
    
    /**
     * Conta total de usuários ativos
     */
    long countActive();
}
