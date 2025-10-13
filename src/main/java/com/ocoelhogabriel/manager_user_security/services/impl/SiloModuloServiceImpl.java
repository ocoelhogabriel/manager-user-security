package com.ocoelhogabriel.manager_user_security.services.impl;

import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.Objects;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.ocoelhogabriel.manager_user_security.handler.AbrangenciaHandler;
import com.ocoelhogabriel.manager_user_security.model.SiloModuloModel;
import com.ocoelhogabriel.manager_user_security.model.dto.SiloDTO;
import com.ocoelhogabriel.manager_user_security.model.dto.SiloModuloDTO;
import com.ocoelhogabriel.manager_user_security.model.entity.Medicao;
import com.ocoelhogabriel.manager_user_security.model.entity.SiloModulo;
import com.ocoelhogabriel.manager_user_security.model.entity.TipoSilo;
import com.ocoelhogabriel.manager_user_security.records.CheckAbrangenciaRec;
import com.ocoelhogabriel.manager_user_security.repository.SiloModuloRepository;
import com.ocoelhogabriel.manager_user_security.services.SiloModuloServInterface;
import com.ocoelhogabriel.manager_user_security.utils.message.MessageResponse;
import jakarta.persistence.EntityNotFoundException;

@Service
public class SiloModuloServiceImpl implements SiloModuloServInterface {

	private static final Logger logger = LoggerFactory.getLogger(SiloModuloServiceImpl.class);

	@Autowired
	private SiloModuloRepository siloModuloRepository;

	@Autowired
	private SiloServiceImpl siloServiceImpl;
	@Autowired
	@Lazy
	private MedicaoServiceImpl medicaoServiceImpl;

	@Autowired
	private AbrangenciaHandler abrangenciaHandler;

	private static final String MODULO = "MODULO";

	private CheckAbrangenciaRec findAbrangencia() {
		return abrangenciaHandler.checkAbrangencia(MODULO);
	}

	@Override
	public ResponseEntity<SiloModuloDTO> save(SiloModuloModel object) throws IOException {
		var silo = siloServiceImpl.findCodigo(object.getSilo());
		if (silo == null)
			throw new EntityNotFoundException("Silo não encontrado.");
		if (findEntityNSE(object.getNumSerie()) != null)
			throw new EntityNotFoundException("Número de Série já cadastrado.");

		var entity = new SiloModulo(null, silo, object.getDescricao(), object.getTotalSensor(), object.getNumSerie(), object.getTimeoutKeepAlive(), object.getTimeoutMedicao(), null, null, object.getGmt(), object.getCorKeepAlive(), object.getCorMedicao(), object.getStatus().getStatus());

		SiloModulo result = siloModuloRepository.save(entity);
		logger.info("Módulo do Silo salvo com sucesso: " + result);
		return MessageResponse.create(new SiloModuloDTO(result));

	}

	@Override
	public ResponseEntity<Void> delete(Long codigo) throws IOException {
		Objects.requireNonNull(codigo, "Código do Módulo do Silo está nulo.");
		try {
			var entity = siloModuloRepository.findById(codigo).orElseThrow(() -> new EntityNotFoundException("Modulo do Silo não encontrado com o código: " + codigo));

			siloModuloRepository.delete(entity);
			logger.info("Módulo do Silo deletado com sucesso: " + entity);
			return MessageResponse.noContent();
		} catch (EmptyResultDataAccessException e) {
			logger.error("Erro ao deletar o Módulo do Silo: ", e);
			throw new EntityNotFoundException("Modulo do Silo não encontrado com o código: " + codigo, e);
		}
	}

	@Override
	public ResponseEntity<SiloModuloDTO> update(Long codigo, SiloModuloModel object) throws IOException {
		var silo = siloServiceImpl.findCodigo(object.getSilo());
		if (silo == null)
			throw new EntityNotFoundException("Silo não encontrado.");
		var siloModulo = siloModuloRepository.findById(codigo).orElseThrow(() -> new EntityNotFoundException("Não foi possível encontrar o módulo do silo com o ID fornecido: " + codigo));

		var entity = new SiloModulo(siloModulo.getSmocod(), silo, object.getDescricao(), object.getTotalSensor(), object.getNumSerie(), object.getTimeoutKeepAlive(), object.getTimeoutMedicao(), null, null, object.getGmt(), object.getCorKeepAlive(), object.getCorMedicao(),
				object.getStatus().getStatus());

		SiloModulo result = siloModuloRepository.save(entity);
		logger.info("Módulo do Silo atualizado com sucesso: " + result);
		return MessageResponse.success(new SiloModuloDTO(result));

	}

	@Override
	public ResponseEntity<List<SiloModuloDTO>> findAll() {
		Specification<SiloModulo> spec = Specification.where(null);

		if (findAbrangencia().isHier() == 0) {
			spec = spec.and(SiloModulo.filterByFields(null, null));
		} else {
			spec = spec.and(SiloModulo.filterByFields(null, findAbrangencia().listAbrangencia()));
		}
		List<SiloModulo> modulos = siloModuloRepository.findAll(spec);
		List<SiloModuloDTO> dtoList = modulos.stream().map(this::dtoCalc).toList();
		return MessageResponse.success(dtoList);
	}

	@Override
	public List<SiloModuloDTO> sendListAbrangenciaModuloDTO() {
		return siloModuloRepository.findAll().stream().map(SiloModuloDTO::new).toList();
	}

	@Override
	public ResponseEntity<Page<SiloModuloDTO>> siloModuloFindAllPaginado(String searchTerm, Pageable pageable) throws EntityNotFoundException, IOException {
		Specification<SiloModulo> spec = Specification.where(null);
		if (findAbrangencia().isHier() == 0) {
			spec = spec.and(SiloModulo.filterByFields(searchTerm, null));
		} else {
			spec = spec.and(SiloModulo.filterByFields(searchTerm, findAbrangencia().listAbrangencia()));
		}
		Page<SiloModulo> result = siloModuloRepository.findAll(spec, pageable);
		return ResponseEntity.ok(result.map(this::dtoCalc));
	}

	@Override
	public ResponseEntity<SiloModuloDTO> findId(Long codigo) {
		var siloModulo = findEntity(codigo);
		if (siloModulo == null)
			throw new EntityNotFoundException("Módulo do Silo não encontrado.");
		Long idPermitted = abrangenciaHandler.findIdAbrangenciaPermi(findAbrangencia(), codigo);
		if (idPermitted == null) 
			throw new EntityNotFoundException("Sem Abrangência para esse Módulo do Silo.");
		return MessageResponse.success(dtoCalc(siloModulo));
	}

	SiloModulo findEntity(Long codigo) {
		Objects.requireNonNull(codigo, "Código do Módulo está nulo.");
		return siloModuloRepository.findById(codigo).orElse(null);
	}

	SiloModulo findEntityNSE(String nse) {
		Objects.requireNonNull(nse, "Número de Série do Módulo está nulo.");
		return siloModuloRepository.findBySmonse(nse).orElse(null);
	}

	void registerKeepAliveInModulo(SiloModulo modulo, Date date) throws EntityNotFoundException {
		var mod = siloModuloRepository.save(modulo.sireneModuloRegisterKeep(date));
		logger.info("Registro de último KeepAlive efetuado com sucesso: " + mod);
	}

	void registerMedicaoInModulo(SiloModulo modulo, Date date) throws EntityNotFoundException {
		var mod = siloModuloRepository.save(modulo.sireneModuloRegisterMedicao(date));
		logger.info("Registro de última Medição efetuado com sucesso: " + mod);
	}

	public SiloModuloDTO dtoCalc(SiloModulo siloModulo) {
		TipoSilo tipoSilo = siloModulo.getSilo().getTipoSilo();
		SiloModuloDTO siloModuloDTO = new SiloModuloDTO(siloModulo);
		SiloDTO silo = siloServiceImpl.abrangenciaSilo(siloModulo.getSilo());
		siloModuloDTO.setSilo(silo);

		Medicao ultimaMedicao = medicaoServiceImpl.ultimaMedicao(siloModulo);
		if (ultimaMedicao == null) {
			return siloModuloDTO;
		}
		siloModuloDTO.calcVolumeSilo(tipoSilo, ultimaMedicao);

		return siloModuloDTO;
	}


}
