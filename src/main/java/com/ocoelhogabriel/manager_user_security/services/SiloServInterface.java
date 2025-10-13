package com.ocoelhogabriel.manager_user_security.services;

import java.io.IOException;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;

import com.ocoelhogabriel.manager_user_security.model.SiloModel;
import com.ocoelhogabriel.manager_user_security.model.dto.SiloDTO;

public interface SiloServInterface {

	ResponseEntity<SiloDTO> save(SiloModel siloDTO) throws IOException;

	ResponseEntity<Void> deleteByPlacod(Long codigo) throws IOException;

	ResponseEntity<SiloDTO> update(Long codigo, SiloModel siloDTO) throws IOException;

	List<SiloDTO> sendListAbrangenciaSiloDTO() throws IOException;

	ResponseEntity<List<SiloDTO>> findAllSiloDTO() throws IOException;

	ResponseEntity<SiloDTO> findById(Long codigo);

	ResponseEntity<Page<SiloDTO>> siloFindAllPaginado(String searchTerm, Pageable pageable);
}
