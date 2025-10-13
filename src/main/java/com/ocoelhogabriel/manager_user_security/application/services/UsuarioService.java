package com.ocoelhogabriel.manager_user_security.application.services;

import com.ocoelhogabriel.manager_user_security.domain.entities.UsuarioModel;
import com.ocoelhogabriel.manager_user_security.infrastructure.adapters.persistence.entities.Usuario;

import java.util.List;

/**
 * Interface de serviço para Usuario
 * Aplica SOLID principles - Interface Segregation Principle
 * Segue Clean Architecture - Application Layer
 */
public interface UsuarioService {
    
    List<UsuarioModel> findAll();
    
    UsuarioModel findById(Long id);
    
    UsuarioModel save(UsuarioModel usuario);
    
    void deleteById(Long id);
    
    UsuarioModel findByLogin(String login);
    
    UsuarioModel findByEmail(String email);
    
    boolean existsByLogin(String login);
    
    boolean existsByEmail(String email);
    
    void changePassword(Long userId, String newPassword);
    
    void activateUser(Long userId);
    
    void deactivateUser(Long userId);
    
    // Métodos para compatibilidade com código legado
    Usuario findLoginEntity(String login);
}
