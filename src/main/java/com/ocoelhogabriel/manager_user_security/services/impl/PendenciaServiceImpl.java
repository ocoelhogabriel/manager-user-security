package com.ocoelhogabriel.manager_user_security.services.impl;

import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.ocoelhogabriel.manager_user_security.model.PendenciaDelete;
import com.ocoelhogabriel.manager_user_security.model.PendenciaModel;
import com.ocoelhogabriel.manager_user_security.model.dto.ItensPendenciasDTO;
import com.ocoelhogabriel.manager_user_security.model.dto.KeepAliveDTO;
import com.ocoelhogabriel.manager_user_security.model.dto.PendenciasDTO;
import com.ocoelhogabriel.manager_user_security.model.entity.Pendencia;
import com.ocoelhogabriel.manager_user_security.model.enums.StatusEnum;
import com.ocoelhogabriel.manager_user_security.repository.PendenciaRepository;
import com.ocoelhogabriel.manager_user_security.services.PendenciaServInterface;
import com.ocoelhogabriel.manager_user_security.utils.Utils;
import com.ocoelhogabriel.manager_user_security.utils.message.MessageResponse;
import jakarta.persistence.EntityNotFoundException;

@Service
public class PendenciaServiceImpl implements PendenciaServInterface {

	@Autowired
	private PendenciaRepository pendenciaRepository;

	@Autowired
	private SiloModuloServiceImpl siloModuloServiceImpl;

	@Override
	public ResponseEntity<KeepAliveDTO> findByKeepAlive(String num) throws EntityNotFoundException, IOException {
		Objects.requireNonNull(num, "Número de Série está nulo.");
		ItensPendenciasDTO itensPendencias = new ItensPendenciasDTO();
		List<Long> listFirmware = new ArrayList<>();

		var modulo = siloModuloServiceImpl.findEntityNSE(num);
		Date dateKeep = Utils.addGmtToDateTime(modulo.getSmogmt());
		String dateKeepString = Utils.addGmtToDateTimeSendString(dateKeep);
		siloModuloServiceImpl.registerKeepAliveInModulo(modulo, dateKeep);

		var listaPendenciaNova = findByPenStaAndpendel(StatusEnum.PENDENCIA.toString());
		listaPendenciaNova.stream().map(pendencia -> listFirmware.add(pendencia.getId())).toList();
		itensPendencias.setFirmware(listFirmware);

		return MessageResponse.success(new KeepAliveDTO(dateKeepString, itensPendencias));
	}

	@Override
	public ResponseEntity<List<PendenciasDTO>> findByAll() {
		List<Pendencia> pendList = pendenciaRepository.findByPendel(1);
		return MessageResponse.success(pendList.stream().map(PendenciasDTO::new).toList());
	}

	@Override
	public ResponseEntity<List<PendenciasDTO>> findByPentipAndpendel(String tipo) {
		Objects.requireNonNull(tipo, "Tipo está nulo.");
		List<Pendencia> pendList = pendenciaRepository.findByPentipAndPendel(tipo, 1);
		return MessageResponse.success(pendList.stream().map(PendenciasDTO::new).toList());
	}

	public List<PendenciasDTO> findByPenStaAndpendel(String status) {
		Objects.requireNonNull(status, "Status está nulo.");
		List<Pendencia> pendList = pendenciaRepository.findByPenstaAndPendel(status, 1);
		return pendList.stream().map(PendenciasDTO::new).toList();
	}

	@Override
	public ResponseEntity<PendenciasDTO> findById(Long id) {
		Pendencia pendencia = findBy(id);
		return MessageResponse.success(new PendenciasDTO(pendencia));
	}

	@Override
	public ResponseEntity<List<PendenciasDTO>> findByPentipAndPenstaAndpendel(String tipo, String status) {
		Objects.requireNonNull(tipo, "Tipo está nulo.");
		Objects.requireNonNull(status, "Status está nulo.");
		List<Pendencia> pendList = pendenciaRepository.findByPentipAndPenstaAndPendel(tipo, status, 1);
		return MessageResponse.success(pendList.stream().map(PendenciasDTO::new).toList());
	}

	@Override
	public ResponseEntity<Page<PendenciasDTO>> findAllPaginado(String nome, Long modulo, Pageable pageable) {
		Objects.requireNonNull(pageable, "Pageable está nulo.");
		Specification<Pendencia> spec = Pendencia.filterByFields(nome, modulo, 1);

		Page<Pendencia> result = pendenciaRepository.findAll(spec, pageable);
		return MessageResponse.success(result.map(PendenciasDTO::new));
	}

	@Override
	public ResponseEntity<PendenciasDTO> delete(PendenciaDelete delete) throws ParseException, IOException {
		Long id = delete.getIdPendencia();
		Objects.requireNonNull(id, "Id da Pendência está nulo.");

		Pendencia pendencia = findBy(id);
		pendencia.setPensta(delete.getStatus().toString());
		pendencia.setPendes(delete.getDescricao());
		pendencia.setPenfim(new Date());

		Pendencia savedPendencia = pendenciaRepository.save(pendencia);
		return MessageResponse.success(new PendenciasDTO(savedPendencia));
	}

	@Override
	public ResponseEntity<PendenciasDTO> update(Long id, StatusEnum pendenciaStatus) throws ParseException {

		Objects.requireNonNull(pendenciaStatus, "Status da pendência está nulo.");

		Pendencia pendencia = findBy(id);

		pendencia.setPensta(pendenciaStatus.toString());
		if (pendenciaStatus == StatusEnum.FINALIZADO) {
			pendencia.setPenfim(new Date());
		}

		Pendencia updatedPendencia = pendenciaRepository.save(pendencia);
		return MessageResponse.success(new PendenciasDTO(updatedPendencia));
	}

	@Override
	public ResponseEntity<PendenciasDTO> save(PendenciaModel pendenciaModel) throws EntityNotFoundException, IOException {
		String numSerie = pendenciaModel.getNumSerie();
		Objects.requireNonNull(pendenciaModel.getStatus(), "Status da pendência está nulo.");
		Objects.requireNonNull(pendenciaModel.getTipoPendencia(), "Tipo da Pendência está nulo.");
		Objects.requireNonNull(pendenciaModel.getDescricao(), "Descrição da Pendência está nula.");
		Objects.requireNonNull(numSerie, "Número de Série está nulo.");

		Pendencia pendencia = new Pendencia(null, pendenciaModel.getTipoPendencia().toString(), pendenciaModel.getStatus().toString(), pendenciaModel.getDescricao(), new Date(), null, null, null, 1);

		Pendencia savedPendencia = pendenciaRepository.save(pendencia);
		return MessageResponse.success(new PendenciasDTO(savedPendencia));
	}

	public Pendencia findBy(Long id) {
		Objects.requireNonNull(id, "Id da Pendência está nulo.");
		return pendenciaRepository.findByPencodAndPendel(id, 1).orElseThrow(() -> new EntityNotFoundException("Pendência não encontrada com o ID: " + id));
	}
}
