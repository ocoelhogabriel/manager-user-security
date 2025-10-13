package com.ocoelhogabriel.manager_user_security.services.impl;

import static com.ocoelhogabriel.manager_user_security.utils.Utils.sdfStringforDate;

import java.io.IOException;
import java.text.ParseException;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

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

import com.ocoelhogabriel.manager_user_security.model.MedicaoPayloadModel;
import com.ocoelhogabriel.manager_user_security.model.MedicaoModel;
import com.ocoelhogabriel.manager_user_security.model.dto.MedicaoDTO;
import com.ocoelhogabriel.manager_user_security.model.entity.Medicao;
import com.ocoelhogabriel.manager_user_security.model.entity.SiloModulo;
import com.ocoelhogabriel.manager_user_security.repository.MedicaoRepository;
import com.ocoelhogabriel.manager_user_security.services.MedicaoServInterface;
import com.ocoelhogabriel.manager_user_security.utils.Utils;
import com.ocoelhogabriel.manager_user_security.utils.message.MessageResponse;
import jakarta.persistence.EntityNotFoundException;

@Service
public class MedicaoServiceImpl implements MedicaoServInterface {

	private static final Logger logger = LoggerFactory.getLogger(MedicaoServiceImpl.class);

	@Autowired
	private MedicaoRepository medicaoRepository;

	@Autowired
	@Lazy
	private SiloModuloServiceImpl siloModuloServiceImpl;

	@Override
	public ResponseEntity<MedicaoDTO> save(MedicaoModel medicaoModel) throws IOException, ParseException {
		checkDataMedicao(medicaoModel);
		Date dateMedicao = sdfStringforDate(medicaoModel.getDataMedicao());
		var siloModulo = siloModuloServiceImpl.findEntity(medicaoModel.getSilo());
		if (siloModulo == null)
			throw new EntityNotFoundException("Módulo não encontrado.");

		siloModuloServiceImpl.registerMedicaoInModulo(siloModulo, new Date());
		Medicao medicao = new Medicao(dateMedicao, siloModulo, medicaoModel.getUmidade(), medicaoModel.getAnalogico(),
				medicaoModel.getBarometro(), medicaoModel.getTemperatura(), medicaoModel.getDistancia());
		Medicao savedMedicao = medicaoRepository.save(medicao);
		logger.info("Medição salva com sucesso: " + savedMedicao);
		return MessageResponse.success(new MedicaoDTO(savedMedicao));
	}

	@Override
	public ResponseEntity<MedicaoDTO> saveData(MedicaoPayloadModel medicaoModel) throws IOException, ParseException {

		Date dateMedicao = Utils.convertTimestampToDate(medicaoModel.getTimestamp());
		var siloModulo = siloModuloServiceImpl.findEntity(Long.valueOf(medicaoModel.getDevEUI()));
		if (siloModulo == null)
			throw new EntityNotFoundException("Módulo não encontrado.");
		siloModuloServiceImpl.registerMedicaoInModulo(siloModulo, new Date());
		Medicao medicao = new Medicao(dateMedicao, siloModulo, medicaoModel.getObject().getHumidity(),
				medicaoModel.getObject().getAnalogInput(), medicaoModel.getObject().getBarometer(),
				medicaoModel.getObject().getTemperature(), medicaoModel.getObject().getIlluminance());

		Medicao savedMedicao = medicaoRepository.save(medicao);
		logger.info("Medição salva com sucesso: " + savedMedicao);
		return MessageResponse.success(new MedicaoDTO(savedMedicao));

	}

	@Override
	public ResponseEntity<MedicaoDTO> saveDataByNSE(MedicaoPayloadModel medicaoModel)
			throws IOException, ParseException {

		Date dateMedicao = Utils.convertTimestampToDate(medicaoModel.getTimestamp());
		var siloModulo = siloModuloServiceImpl.findEntityNSE(medicaoModel.getDevEUI());
		if (siloModulo == null)
			throw new EntityNotFoundException("Módulo não encontrado.");
		siloModuloServiceImpl.registerMedicaoInModulo(siloModulo, new Date());
		Medicao medicao = new Medicao(dateMedicao, siloModulo, medicaoModel.getObject().getHumidity(),
				medicaoModel.getObject().getAnalogInput(), medicaoModel.getObject().getBarometer(),
				medicaoModel.getObject().getTemperature(), medicaoModel.getObject().getIlluminance());

		Medicao savedMedicao = medicaoRepository.save(medicao);
		logger.info("Medição salva com sucesso: " + savedMedicao);
		return MessageResponse.success(new MedicaoDTO(savedMedicao));

	}

	@Override
	public ResponseEntity<Void> deleteByMsidth(String msidth) throws IOException, ParseException {
		Objects.requireNonNull(msidth, "Data da Medição está nula.");
		try {
			Date dateMedicao = sdfStringforDate(msidth);
			Medicao existingMedicao = medicaoRepository.findByMsidth(dateMedicao)
					.orElseThrow(() -> new EntityNotFoundException("Medição na data " + msidth + " não encontrado."));

			medicaoRepository.deleteById(existingMedicao.getMsidth());
			return MessageResponse.noContent();
		} catch (EmptyResultDataAccessException e) {
			logger.error("Não foi possível encontrar a Medição com o ID fornecido. Erro: ", e);
			throw new EntityNotFoundException("Planta não encontrada com a data: " + msidth, e);
		}
	}

	@Override
	public ResponseEntity<MedicaoDTO> update(MedicaoModel medicaoModel) throws IOException, ParseException {
		checkDataMedicao(medicaoModel);

		Date dateMedicao = sdfStringforDate(medicaoModel.getDataMedicao());
		Medicao existingMedicao = medicaoRepository.findByMsidth(dateMedicao)
				.orElseThrow(() -> new EntityNotFoundException(
						"Medição na data " + medicaoModel.getDataMedicao() + " não encontrado."));

		var siloModulo = siloModuloServiceImpl.findEntity(medicaoModel.getSilo());
		siloModuloServiceImpl.registerMedicaoInModulo(siloModulo, new Date());

		existingMedicao.updateMedicao(siloModulo, medicaoModel.getUmidade(), medicaoModel.getAnalogico(),
				medicaoModel.getBarometro(), medicaoModel.getTemperatura(), medicaoModel.getDistancia());

		Medicao updatedMedicao = medicaoRepository.save(existingMedicao);
		logger.info("Medição atualizada com sucesso: " + updatedMedicao);
		return MessageResponse.success(new MedicaoDTO(updatedMedicao));

	}

	@Override
	public List<Medicao> findAll() throws IOException {
		return medicaoRepository.findAll();
	}

	@Override
	public ResponseEntity<List<MedicaoDTO>> findAllMedicaoDTO() throws IOException {
		List<MedicaoDTO> medicaoDTOs = medicaoRepository.findAll().stream().map(this::convertToMedicaoDTO).toList();
		return MessageResponse.success(medicaoDTOs);
	}

	@Override
	public ResponseEntity<MedicaoDTO> findByData(String dateSTR) throws IOException, ParseException {
		Date date = sdfStringforDate(dateSTR);

		return MessageResponse.success(new MedicaoDTO(medicaoRepository.findByMsidth(date)
				.orElseThrow(() -> new EntityNotFoundException("Medição na data " + dateSTR + " não encontrado."))));
	}

	@Override
	public ResponseEntity<Page<MedicaoDTO>> medicaoFindAllPaginado(String searchTerm, Long modulo, String dataInicio,
			String dataFim, Pageable pageable) {
		Specification<Medicao> spec = Medicao.filterByFields(searchTerm, modulo, dataInicio, dataFim, null);
		Page<Medicao> result = medicaoRepository.findAll(spec, pageable);
		return ResponseEntity.ok(result.map(MedicaoDTO::new));
	}

	@Override
	public ResponseEntity<List<MedicaoDTO>> medicaoFindAllForModule(Long modulo, String dataInicio, String dataFim,
			String direcao) {
		Specification<Medicao> spec = Medicao.filterByFields(null, modulo, dataInicio, dataFim, direcao);
		List<Medicao> result = medicaoRepository.findAll(spec);
		return ResponseEntity.ok(result.stream().map(MedicaoDTO::new).toList());
	}

	private MedicaoDTO convertToMedicaoDTO(Medicao medicaoEntity) {
		return new MedicaoDTO(medicaoEntity);
	}

	Medicao ultimaMedicao(SiloModulo siloModulo) {
		Optional<Medicao> medicao = medicaoRepository.findFirstByModuloOrderByMsidthDesc(siloModulo);
		if (medicao.isEmpty())
			return null;
		return medicao.get();
	}

	private void checkDataMedicao(MedicaoModel model) {
		Objects.requireNonNull(model.getDataMedicao(), "Data da Medição está nula.");
		Objects.requireNonNull(model.getSilo(), "Código do Silo está nulo.");
		Objects.requireNonNull(model.getUmidade(), "Umidade está nula.");
		Objects.requireNonNull(model.getTemperatura(), "Temperatura está nula.");
		Objects.requireNonNull(model.getAnalogico(), "Ana está nula.");
		Objects.requireNonNull(model.getBarometro(), "Barômetro está nulo.");
		Objects.requireNonNull(model.getDistancia(), "Distância está nula.");
	}
}
