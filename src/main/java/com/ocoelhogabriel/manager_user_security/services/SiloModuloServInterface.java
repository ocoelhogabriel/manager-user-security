package com.ocoelhogabriel.manager_user_security.services;

import java.io.IOException;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;

import com.ocoelhogabriel.manager_user_security.model.SiloModuloModel;
import com.ocoelhogabriel.manager_user_security.model.dto.SiloModuloDTO;
import jakarta.persistence.EntityNotFoundException;

public interface SiloModuloServInterface {

	ResponseEntity<SiloModuloDTO> save(SiloModuloModel object) throws IOException;

	ResponseEntity<Void> delete(Long codigo) throws IOException;

	ResponseEntity<SiloModuloDTO> update(Long codigo, SiloModuloModel object) throws IOException;

	ResponseEntity<List<SiloModuloDTO>> findAll();

	ResponseEntity<SiloModuloDTO> findId(Long codigo);

	ResponseEntity<Page<SiloModuloDTO>> siloModuloFindAllPaginado(String searchTerm, Pageable pageable) throws EntityNotFoundException, IOException;

	List<SiloModuloDTO> sendListAbrangenciaModuloDTO();

}
