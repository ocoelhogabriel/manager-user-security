package com.ocoelhogabriel.manager_user_security.services;

import java.io.IOException;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.NonNull;

import com.ocoelhogabriel.manager_user_security.model.UsuarioModel;
import com.ocoelhogabriel.manager_user_security.model.dto.UsuarioDTO;
import com.ocoelhogabriel.manager_user_security.model.dto.UsuarioPermissaoDTO;
import jakarta.persistence.EntityNotFoundException;

public interface UsuarioServInterface {

	ResponseEntity<UsuarioDTO> findById(@NonNull Long codigo) throws EntityNotFoundException, IOException;

	ResponseEntity<List<UsuarioDTO>> findAll() throws EntityNotFoundException, IOException;

	ResponseEntity<UsuarioDTO> saveUpdateEncodePassword(@NonNull Long codigo, @NonNull UsuarioModel userModel) throws EntityNotFoundException, IOException;

	ResponseEntity<UsuarioDTO> saveUpdateEncodePassword(@NonNull UsuarioModel userModel) throws EntityNotFoundException, IOException;

	ResponseEntity<Page<UsuarioDTO>> findAll(String nome, @NonNull Pageable pageable) throws EntityNotFoundException, IOException;

	ResponseEntity<Void> delete(@NonNull Long perfil) throws IOException;

	ResponseEntity<UsuarioPermissaoDTO> findByIdPermission(@NonNull Long codigo) throws EntityNotFoundException, IOException;

}
