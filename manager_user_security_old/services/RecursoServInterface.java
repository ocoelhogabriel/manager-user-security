package com.ocoelhogabriel.manager_user_security.services;

import java.io.IOException;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.NonNull;

import com.ocoelhogabriel.manager_user_security.model.RecursoModel;
import com.ocoelhogabriel.manager_user_security.model.dto.RecursoDTO;
import jakarta.persistence.EntityNotFoundException;

public interface RecursoServInterface {

	ResponseEntity<RecursoDTO> save(RecursoModel perModel);

	ResponseEntity<RecursoDTO> update(Long codigo, RecursoModel perModel);

	ResponseEntity<List<RecursoDTO>> findAll() throws EntityNotFoundException, IOException;

	ResponseEntity<RecursoDTO> findById(@NonNull Long codigo) throws EntityNotFoundException, IOException;

	ResponseEntity<Void> delete(@NonNull Long perfil) throws IOException;

	ResponseEntity<Page<RecursoDTO>> findAll(String nome, Pageable pageable) throws EntityNotFoundException, IOException;

	ResponseEntity<RecursoDTO> findByString(@NonNull String nome) throws EntityNotFoundException, IOException;

}
