package com.ocoelhogabriel.manager_user_security.domain.services;

import com.ocoelhogabriel.manager_user_security.domain.entities.UsuarioModel;
import com.ocoelhogabriel.manager_user_security.interfaces.web.dtos.UsuarioDTO;
import com.ocoelhogabriel.manager_user_security.interfaces.web.dtos.UsuarioPermissaoDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import jakarta.persistence.EntityNotFoundException;

import java.io.IOException;
import java.util.List;

public interface UsuarioServInterface {
    
    ResponseEntity<UsuarioDTO> saveUpdateEncodePassword(UsuarioModel usuario) throws EntityNotFoundException, IOException;
    
    ResponseEntity<UsuarioDTO> saveUpdateEncodePassword(Long codigo, UsuarioModel usuario) throws EntityNotFoundException, IOException;
    
    ResponseEntity<UsuarioDTO> findById(Long codigo) throws EntityNotFoundException, IOException;
    
    ResponseEntity<UsuarioPermissaoDTO> findByIdPermission(Long codigo) throws EntityNotFoundException, IOException;
    
    ResponseEntity<List<UsuarioDTO>> findAll() throws EntityNotFoundException, IOException;
    
    ResponseEntity<Page<UsuarioDTO>> findAll(String filtro, Pageable pageable) throws EntityNotFoundException, IOException;
    
    ResponseEntity<Void> delete(Long codigo) throws IOException;
}