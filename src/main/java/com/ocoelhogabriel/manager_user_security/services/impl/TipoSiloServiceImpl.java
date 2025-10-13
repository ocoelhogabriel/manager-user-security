package com.ocoelhogabriel.manager_user_security.services.impl;

import java.io.IOException;
import java.util.List;
import java.util.Objects;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.ocoelhogabriel.manager_user_security.exception.CustomMessageExcep;
import com.ocoelhogabriel.manager_user_security.handler.AbrangenciaHandler;
import com.ocoelhogabriel.manager_user_security.model.TipoSiloModel;
import com.ocoelhogabriel.manager_user_security.model.dto.TipoSiloDTO;
import com.ocoelhogabriel.manager_user_security.model.entity.TipoSilo;
import com.ocoelhogabriel.manager_user_security.model.enums.TipoSiloEnum;
import com.ocoelhogabriel.manager_user_security.records.CheckAbrangenciaRec;
import com.ocoelhogabriel.manager_user_security.repository.TipoSiloRepository;
import com.ocoelhogabriel.manager_user_security.services.TipoSiloServInterface;
import com.ocoelhogabriel.manager_user_security.utils.Utils;
import com.ocoelhogabriel.manager_user_security.utils.message.MessageResponse;
import jakarta.persistence.EntityNotFoundException;

@Service
public class TipoSiloServiceImpl implements TipoSiloServInterface {

	private static final Logger logger = LoggerFactory.getLogger(TipoSiloServiceImpl.class);
	private static final String RECURSO = "Tipo Silo";

	@Autowired
	private AbrangenciaHandler abrangenciaHandler;

	private static final String TIPOSILO = "TIPOSILO";

	private CheckAbrangenciaRec findAbrangencia() {
		return abrangenciaHandler.checkAbrangencia(TIPOSILO);
	}

	@Autowired
	private TipoSiloRepository tipoSiloRepository;

	@Override
	public ResponseEntity<TipoSiloDTO> save(TipoSiloModel tipoSiloModel) throws RuntimeException, IOException {

		TipoSilo entity = new TipoSilo();
		entity.setTsinom(tipoSiloModel.getNome());
		entity.setTsides(tipoSiloModel.getDescricao());
		entity.setTsitip(tipoSiloModel.getTipoSilo().getTipo());
		entity.setTsiach(Utils.converterMParaMm(tipoSiloModel.getAlturaCheio()));
		entity.setTsidse(Utils.converterMParaMm(tipoSiloModel.getDistanciaSensor()));

		if (tipoSiloModel.getTipoSilo() == TipoSiloEnum.HORIZONTAL)
			entity.tipoSiloHorizontal(tipoSiloModel.getLargura(), tipoSiloModel.getComprimento());
		if (tipoSiloModel.getTipoSilo() == TipoSiloEnum.VERTICAL)
			entity.tipoSiloVertical(tipoSiloModel.getRaio());

		TipoSilo result = tipoSiloRepository.save(entity);

		logger.info("Tipo Silo salvo com sucesso: " + result);
		return MessageResponse.success(new TipoSiloDTO(result));

	}

	@Override
	public ResponseEntity<Void> deleteByTsicod(Long codigo) throws IOException {
		Objects.requireNonNull(codigo, "Código do Tipo do Silo está nulo.");
		// try {
		TipoSilo tipoSilo = findEntity(codigo);
		if (tipoSilo == null)
			throw new EntityNotFoundException("Tipo silo não encontrada com o código: " + codigo);

		tipoSiloRepository.deleteById(tipoSilo.getTsicod());

		logger.info("Tipo Silo com ID " + codigo + " deletado com sucesso.");
		return MessageResponse.noContent();
		// } catch (Exception e) {
		// logger.error("Erro ao deletar o tipo do silo: ", e);
		// throw CustomMessageExcep.exceptionIOException("deletar", RECURSO, codigo, e);
		// }
	}

	@Override
	public ResponseEntity<TipoSiloDTO> update(Long codigo, TipoSiloModel tipoSiloModel) throws IOException {

		TipoSilo resultEntity = tipoSiloRepository.findById(codigo)
				.orElseThrow(() -> CustomMessageExcep.exceptionEntityNotFoundException(codigo, RECURSO, null));

		resultEntity.setTsinom(tipoSiloModel.getNome());
		resultEntity.setTsides(tipoSiloModel.getDescricao());
		resultEntity.setTsitip(tipoSiloModel.getTipoSilo().getTipo());
		resultEntity.setTsiach(Utils.converterMParaMm(tipoSiloModel.getAlturaCheio()));
		resultEntity.setTsidse(Utils.converterMParaMm(tipoSiloModel.getDistanciaSensor()));

		if (tipoSiloModel.getTipoSilo() == TipoSiloEnum.HORIZONTAL)
			resultEntity.tipoSiloHorizontal(tipoSiloModel.getLargura(), tipoSiloModel.getComprimento());
		if (tipoSiloModel.getTipoSilo() == TipoSiloEnum.VERTICAL)
			resultEntity.tipoSiloVertical(tipoSiloModel.getRaio());
		TipoSilo result = tipoSiloRepository.save(resultEntity);

		logger.info("Tipo Silo atualizado com sucesso: " + result);
		return MessageResponse.success(new TipoSiloDTO(result));

	}

	@Override
	public ResponseEntity<List<TipoSiloDTO>> findAllTipoSiloDTO() throws IOException {
		Specification<TipoSilo> spec = Specification.where(null);
		if (findAbrangencia().isHier() == 0) {
			spec = spec.and(TipoSilo.filterByFields(null, null));
		} else {
			spec = spec.and(TipoSilo.filterByFields(null, findAbrangencia().listAbrangencia()));
		}
		List<TipoSiloDTO> tipoSiloDTOList = tipoSiloRepository.findAll(spec).stream().map(this::convertToTipoSiloDTO)
				.toList();
		return MessageResponse.success(tipoSiloDTOList);
	}

	@Override
	public List<TipoSiloDTO> sendListAbrangenciaTipoSiloDTO() throws IOException {
		return tipoSiloRepository.findAll().stream().map(TipoSiloDTO::new).toList();
	}

	@Override
	public ResponseEntity<TipoSiloDTO> findById(Long codigo) throws IOException, EntityNotFoundException {
		Objects.requireNonNull(codigo, "Código do Tipo do Silo está nulo.");

		TipoSilo result = findEntity(codigo);
		if (result == null)
			throw new EntityNotFoundException("Tipo de Silo não encontrado.");

		Long idPermitted = abrangenciaHandler.findIdAbrangenciaPermi(findAbrangencia(), codigo);
		if (idPermitted == null)
			throw new EntityNotFoundException("Sem Abrangência para esse Tipo de Silo.");
		return MessageResponse.success(new TipoSiloDTO(result));
	}

	@Override
	public ResponseEntity<Page<TipoSiloDTO>> tipoSiloFindAllPaginado(String searchTerm, Pageable pageable) {
		Specification<TipoSilo> spec = Specification.where(null);
		if (findAbrangencia().isHier() == 0) {
			spec = spec.and(TipoSilo.filterByFields(searchTerm, null));
		} else {
			spec = spec.and(TipoSilo.filterByFields(searchTerm, findAbrangencia().listAbrangencia()));
		}
		Page<TipoSilo> result = tipoSiloRepository.findAll(spec, pageable);
		return ResponseEntity.ok(result.map(TipoSiloDTO::new));
	}

	private TipoSiloDTO convertToTipoSiloDTO(TipoSilo tipoSilo) {
		return new TipoSiloDTO(tipoSilo);
	}

	TipoSilo findEntity(Long codigo) {
		Objects.requireNonNull(codigo, "Código do Tipo do Silo está nulo.");
		return tipoSiloRepository.findById(codigo).orElse(null);
	}

	TipoSiloDTO findTipoSiloAbrangencia(Long codigo) {
		Long idPermitted = abrangenciaHandler.findIdAbrangenciaPermi(findAbrangencia(), codigo);
		if (idPermitted == null) {
			return null;
		}
		TipoSilo result = findEntity(idPermitted);
		return new TipoSiloDTO(result);
	}

}
