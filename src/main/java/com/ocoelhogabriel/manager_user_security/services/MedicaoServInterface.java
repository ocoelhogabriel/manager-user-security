package com.ocoelhogabriel.manager_user_security.services;

import java.io.IOException;
import java.text.ParseException;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;

import com.ocoelhogabriel.manager_user_security.model.MedicaoPayloadModel;
import com.ocoelhogabriel.manager_user_security.model.MedicaoModel;
import com.ocoelhogabriel.manager_user_security.model.dto.MedicaoDTO;
import com.ocoelhogabriel.manager_user_security.model.entity.Medicao;

public interface MedicaoServInterface {

	ResponseEntity<Void> deleteByMsidth(String msidth) throws IOException, ParseException;

	List<Medicao> findAll() throws IOException;

	ResponseEntity<List<MedicaoDTO>> findAllMedicaoDTO() throws IOException;

	ResponseEntity<List<MedicaoDTO>> medicaoFindAllForModule(Long modulo, String dataInicio, String dataFim,
			String direcao);

	ResponseEntity<MedicaoDTO> findByData(String date) throws IOException, ParseException;

	ResponseEntity<MedicaoDTO> save(MedicaoModel medicaoDTO) throws IOException, ParseException;

	ResponseEntity<MedicaoDTO> saveData(MedicaoPayloadModel medicaoDTO) throws IOException, ParseException;

	ResponseEntity<MedicaoDTO> saveDataByNSE(MedicaoPayloadModel medicaoDTO) throws IOException, ParseException;

	ResponseEntity<MedicaoDTO> update(MedicaoModel medicaoDTO) throws IOException, ParseException;

	ResponseEntity<Page<MedicaoDTO>> medicaoFindAllPaginado(String searchTerm, Long modulo, String dataInicio,
			String dataFim, Pageable pageable);

}
