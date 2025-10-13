package com.ocoelhogabriel.manager_user_security.services;

import java.io.IOException;
import java.util.List;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;

import com.ocoelhogabriel.manager_user_security.model.PlantaModel;
import com.ocoelhogabriel.manager_user_security.model.dto.PlantaDTO;
import jakarta.persistence.EntityNotFoundException;

public interface PlantaServInterface {

	ResponseEntity<PlantaDTO> save(PlantaModel planta) throws IOException;

	ResponseEntity<Void> deleteByPlacod(Long codigo) throws IOException;

	ResponseEntity<PlantaDTO> update(Long codigo, PlantaModel planta) throws IOException;

	List<PlantaDTO> sendListAbrangenciaPlantaDTO() throws IOException;

	ResponseEntity<List<PlantaDTO>> findAllPlantaDTO() throws IOException;

	ResponseEntity<PlantaDTO> findById(Long codigo) throws IOException, EmptyResultDataAccessException;

	ResponseEntity<Page<PlantaDTO>> plantaFindAllPaginado(String searchTerm, Pageable pageable) throws EntityNotFoundException, IOException;
}
