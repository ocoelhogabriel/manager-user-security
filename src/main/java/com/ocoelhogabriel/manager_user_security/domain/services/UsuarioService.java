package com.ocoelhogabriel.manager_user_security.domain.services;

import com.ocoelhogabriel.manager_user_security.domain.entities.UsuarioModel;
import java.util.List;

public interface UsuarioService {
    
    List<UsuarioModel> findAll();
    
    UsuarioModel findById(Long id);
    
    UsuarioModel save(UsuarioModel usuario);
    
    void deleteById(Long id);
    
    UsuarioModel findByLogin(String login);
    
    UsuarioModel findByEmail(String email);
    
    boolean existsByLogin(String login);
    
    boolean existsByEmail(String email);
}