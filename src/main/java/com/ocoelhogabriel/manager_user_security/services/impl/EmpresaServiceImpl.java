package com.ocoelhogabriel.manager_user_security.services.impl;

import java.io.IOException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;

import com.ocoelhogabriel.manager_user_security.handler.AbrangenciaHandler;
import com.ocoelhogabriel.manager_user_security.model.EmpresaModel;
import com.ocoelhogabriel.manager_user_security.model.dto.EmpresaDTO;
import com.ocoelhogabriel.manager_user_security.model.entity.Empresa;
import com.ocoelhogabriel.manager_user_security.records.CheckAbrangenciaRec;
import com.ocoelhogabriel.manager_user_security.repository.EmpresaRepository;
import com.ocoelhogabriel.manager_user_security.services.EmpresaServInterface;
import com.ocoelhogabriel.manager_user_security.utils.message.MessageResponse;
import jakarta.persistence.EntityNotFoundException;

@Service
public class EmpresaServiceImpl implements EmpresaServInterface {

	private static final Logger log = LoggerFactory.getLogger(EmpresaServiceImpl.class);

	@Autowired
	private EmpresaRepository empresaRepository;

	@Autowired
	private AbrangenciaHandler abrangenciaHandler;

	private static final String EMPRESA = "EMPRESA";

	private CheckAbrangenciaRec findAbrangencia() {
		return abrangenciaHandler.checkAbrangencia(EMPRESA);
	}

	@Override
	public ResponseEntity<Void> empresaDeleteById(Long codigo) throws IOException {

		try {
			var empresa = findByIdEntity(codigo);
			if (empresa == null)
				throw new EntityNotFoundException("Empresa não encontrada com o código: " + codigo);

			empresaRepository.deleteById(empresa.getEmpcod());
			return MessageResponse.noContent();
		} catch (EmptyResultDataAccessException e) {
			log.error("Erro ao deletar a empresa. Erro: ", e);
			throw new EntityNotFoundException("Empresa não encontrada com o código: " + codigo, e);
		}
	}

	@Override
	public ResponseEntity<Page<EmpresaDTO>> empresaFindAllPaginado(String nome, Pageable pageable) throws IOException {
		Objects.requireNonNull(pageable, "Pageable da Empresa está nulo.");

		Specification<Empresa> spec = Specification.where(null);

		if (findAbrangencia().isHier() == 0) {
			spec = spec.and(Empresa.filterByFields(nome, null));
		} else {
			spec = spec.and(Empresa.filterByFields(nome, findAbrangencia().listAbrangencia()));
		}

		Page<Empresa> result = empresaRepository.findAll(spec, pageable);
		return MessageResponse.success(result.map(EmpresaDTO::new));
	}

	@Override
	public List<EmpresaDTO> sendListAbrangenciaEmpresaDTO() {
		return empresaRepository.findAll().stream().map(EmpresaDTO::new).toList();
	}

	@Override
	public ResponseEntity<List<EmpresaDTO>> empresaFindAll() throws IOException {

		Specification<Empresa> spec = Specification.where(null);

		if (findAbrangencia().isHier() == 0) {
			spec = spec.and(Empresa.filterByFields(null, null));
		} else {
			spec = spec.and(Empresa.filterByFields(null, findAbrangencia().listAbrangencia()));
		}
		List<Empresa> result = empresaRepository.findAll(spec);
		return MessageResponse.success(result.stream().map(EmpresaDTO::new).toList());
	}

	@Override
	public ResponseEntity<EmpresaDTO> empresaUpdate(Long codigo, EmpresaModel empresaModel) throws IOException {
		Objects.requireNonNull(empresaModel.getCnpj(), "CNPJ da Empresa está nulo.");
		Objects.requireNonNull(empresaModel.getNome(), "Nome da Empresa está nulo.");

		var empresa = findByIdEntity(codigo);

		String nomeFantasia = Optional.ofNullable(empresaModel.getNomeFantasia()).orElse(empresa.getEmpfan());
		String telefone = Optional.ofNullable(empresaModel.getTelefone()).orElse(empresa.getEmptel());

		empresa.setEmpcnp(empresaModel.getCnpj());
		empresa.setEmpnom(empresaModel.getNome());
		empresa.setEmpfan(nomeFantasia);
		empresa.setEmptel(telefone);

		return MessageResponse.success(new EmpresaDTO(empresaRepository.save(empresa)));
	}

	@Override
	public ResponseEntity<EmpresaDTO> empresaSave(EmpresaModel empresaModel) throws IOException {
		Objects.requireNonNull(empresaModel.getCnpj(), "CNPJ da Empresa está nulo.");
		Objects.requireNonNull(empresaModel.getNome(), "Nome da Empresa está nulo.");

		try {
			Empresa empresa = new Empresa(null, empresaModel.getCnpj(), empresaModel.getNome(), empresaModel.getNomeFantasia(), empresaModel.getTelefone());
			Empresa savedEmpresa = empresaRepository.save(empresa);
			return MessageResponse.create(new EmpresaDTO(savedEmpresa));
		} catch (Exception e) {
			log.error("Erro ao realizar o cadastro de uma empresa.", e);
			throw new IOException("Erro ao realizar o cadastro de uma empresa.", e);
		}
	}

	@Override
	public ResponseEntity<EmpresaDTO> findByIdApi(Long codigo) throws IOException {


		var empresa = empresaRepository.findById(codigo).orElse(null);
		if (empresa == null) {
			throw new EntityNotFoundException("Empresa não encontrada.");
		}

		Long idPermitted = abrangenciaHandler.findIdAbrangenciaPermi(findAbrangencia(), empresa.getEmpcod());
		if (idPermitted == null) {
			throw new EntityNotFoundException("Sem Abrangência para essa empresa.");
		}
		return MessageResponse.success(new EmpresaDTO(empresa));
	}

	@Override
	public ResponseEntity<EmpresaDTO> empresaFindByCnpjApi(Long codigo) throws IOException {
		Empresa empresa = empresaFindByCnpjEntity(codigo);
		if (empresa == null) {
			throw new EntityNotFoundException("Empresa não encontrada.");
		}

		Long idPermitted = abrangenciaHandler.findIdAbrangenciaPermi(findAbrangencia(), empresa.getEmpcod());
		if (idPermitted == null) {
			throw new EntityNotFoundException("Sem Abrangência para essa empresa.");
		}

		return MessageResponse.success(new EmpresaDTO(empresa));
	}

	public Empresa findById(Long codigo) {
		Objects.requireNonNull(codigo, "Código da Empresa está nulo.");
		Empresa emp = empresaRepository.findById(codigo).orElse(null);
		if (emp == null) {
			return null;
		}
		return findByIdAbrangencia(emp);
	}

	public Empresa findByIdAbrangencia(Empresa emp) {

		var findIdAbrangencia = abrangenciaHandler.findIdAbrangenciaPermi(findAbrangencia(), emp.getEmpcod());
		if (findIdAbrangencia == null) {
			return null;
		}
		return emp;
	}

	public Empresa empresaFindByCnpjEntity(Long codigo) {
		Objects.requireNonNull(codigo, "Código da Empresa está nulo.");
		return empresaRepository.findByEmpcnp(codigo).orElse(null);
	}

	public Empresa findByIdEntity(@NonNull Long codigo) {
		Objects.requireNonNull(codigo, "Código da Empresa está nulo.");
		return empresaRepository.findById(codigo).orElse(null);
	}

}
