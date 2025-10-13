package com.ocoelhogabriel.manager_user_security.services;

import java.io.IOException;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.NonNull;

import com.ocoelhogabriel.manager_user_security.model.AbrangenciaModel;
import com.ocoelhogabriel.manager_user_security.model.dto.AbrangenciaListaDetalhesDTO;
import com.ocoelhogabriel.manager_user_security.records.ItensAbrangentes;
import jakarta.persistence.EntityNotFoundException;

public interface AbrangenciaServInterface {

	ResponseEntity<AbrangenciaListaDetalhesDTO> update(@NonNull Long codigo, AbrangenciaModel abrangenciaModel);

	ResponseEntity<AbrangenciaListaDetalhesDTO> save(AbrangenciaModel abrangenciaModel) throws IOException;

	ResponseEntity<List<AbrangenciaListaDetalhesDTO>> findAll() throws EntityNotFoundException, IOException;

	ResponseEntity<AbrangenciaListaDetalhesDTO> findById(@NonNull Long codigo) throws EntityNotFoundException, IOException;

	ResponseEntity<Void> delete(@NonNull Long perfil) throws IOException;

	ResponseEntity<Page<AbrangenciaListaDetalhesDTO>> findAll(String nome, Pageable pageable) throws EntityNotFoundException, IOException;

	ResponseEntity<ItensAbrangentes> findByItemAbrangence() throws IOException;

}
