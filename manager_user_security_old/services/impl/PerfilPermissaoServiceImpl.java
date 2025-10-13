package com.ocoelhogabriel.manager_user_security.services.impl;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;

import com.ocoelhogabriel.manager_user_security.exception.ResponseGlobalModel;
import com.ocoelhogabriel.manager_user_security.model.PerfilModel;
import com.ocoelhogabriel.manager_user_security.model.PermissaoModel;
import com.ocoelhogabriel.manager_user_security.model.dto.PerfilPermissaoDTO;
import com.ocoelhogabriel.manager_user_security.model.dto.PermissaoDTO;
import com.ocoelhogabriel.manager_user_security.model.dto.RecursoDTO;
import com.ocoelhogabriel.manager_user_security.model.entity.Perfil;
import com.ocoelhogabriel.manager_user_security.model.entity.Permissao;
import com.ocoelhogabriel.manager_user_security.model.entity.Recurso;
import com.ocoelhogabriel.manager_user_security.repository.PerfilRepository;
import com.ocoelhogabriel.manager_user_security.repository.PermissaoRepository;
import com.ocoelhogabriel.manager_user_security.services.PerfilPermServInterface;
import com.ocoelhogabriel.manager_user_security.utils.Utils;
import com.ocoelhogabriel.manager_user_security.utils.message.MessageResponse;
import jakarta.persistence.EntityNotFoundException;

@Service
public class PerfilPermissaoServiceImpl implements PerfilPermServInterface {

	@Autowired
	private PermissaoRepository permissaoRepository;

	@Autowired
	private RecursoServiceImpl recursoService;

	@Autowired
	private PerfilRepository perfilRepository;

	@Override
	public ResponseEntity<PerfilPermissaoDTO> findById(@NonNull Long codigo) throws EntityNotFoundException, IOException {
		return MessageResponse.success(findByIdPerfil(codigo));
	}

	@Override
	public ResponseEntity<List<PerfilPermissaoDTO>> findAll() throws EntityNotFoundException, IOException {
		return MessageResponse.success(perfilRepository.findAll().stream().map(perfil -> {
			List<PermissaoDTO> permissoes = permissaoRepository.findByPerfil_percod(perfil.getPercod()).orElseGet(ArrayList::new).stream().map(PermissaoDTO::new).toList();
			return new PerfilPermissaoDTO(perfil, permissoes);
		}).toList());
	}

	@Override
	public ResponseEntity<Page<PerfilPermissaoDTO>> findAll(String nome, @NonNull Pageable pageable) throws EntityNotFoundException, IOException {
		Objects.requireNonNull(pageable, "Pageable do Perfil está nulo.");
		Specification<Perfil> spec = Perfil.filterByFields(nome);
		Page<Perfil> result = perfilRepository.findAll(spec, pageable);
		return MessageResponse.success(result.map(perfil -> {
			List<PermissaoDTO> permissoes = permissaoRepository.findByPerfil_percod(perfil.getPercod()).orElseGet(ArrayList::new).stream().map(PermissaoDTO::new).toList();
			return new PerfilPermissaoDTO(perfil, permissoes);
		}));
	}

	@Override
	public ResponseEntity<PerfilPermissaoDTO> save(PerfilModel perModel) throws IOException {
		try {
			Objects.requireNonNull(perModel.getNome(), "Nome do Perfil está nulo.");
			Perfil perfil = createUpdatePerfil(new Perfil(null, perModel.getNome().toUpperCase(), perModel.getDescricao()));

			List<PermissaoDTO> permissaoList = perModel.getPermissoes().stream().map(permissao -> saveEntityPermissao(perfil, permissao)).toList();

			return MessageResponse.create(new PerfilPermissaoDTO(perfil, permissaoList));
		} catch (Exception e) {
			throw new IOException("Erro ao criar um Perfil. " + e.getMessage(), e);
		}
	}

	@Override
	public ResponseEntity<PerfilPermissaoDTO> update(@NonNull Long codigo, PerfilModel perModel) throws EntityNotFoundException, IOException {
		Objects.requireNonNull(codigo, "Código do Perfil está nulo.");
		Objects.requireNonNull(perModel.getNome(), "Nome do Perfil está nulo.");

		Perfil perfil = findByIdPerfilEntity(codigo);
		perfil.setPernom(perModel.getNome().toUpperCase());
		perfil.setPerdes(perModel.getDescricao());
		Perfil perfilUp = perfilRepository.save(perfil);

		List<PermissaoDTO> permissaoList = perModel.getPermissoes().stream().map(permissao -> updateEntityPermissao(perfilUp, permissao)).toList();

		return MessageResponse.success(new PerfilPermissaoDTO(perfilUp, permissaoList));
	}

	@Override
	public ResponseEntity<Void> delete(@NonNull Long perfil) throws IOException {
		try {
			deleteEntityPermissao(perfil);
			perfilRepository.deleteById(perfil);
			return MessageResponse.noContent();
		} catch (IOException e) {
			throw new IOException("Erro ao apagar o perfil. Mensagem: " + e.getMessage(), e);
		}
	}

	public PerfilPermissaoDTO findByIdPerfil(Long codigo) throws EntityNotFoundException {
		Objects.requireNonNull(codigo, "Código do Perfil está nulo.");
		Perfil perfil = findByIdPerfilEntity(codigo);
		List<PermissaoDTO> permissoes = permissaoRepository.findByPerfil_percod(perfil.getPercod()).orElseThrow(() -> new EntityNotFoundException("Permissões não encontradas para o perfil com código: " + codigo)).stream().map(PermissaoDTO::new).toList();
		return new PerfilPermissaoDTO(perfil, permissoes);
	}

	public Perfil findByIdPerfilEntity(@NonNull Long codigo) throws EntityNotFoundException {
		return perfilRepository.findById(codigo).orElse(null);
	}

	public Perfil findByIdPerfilEntity(@NonNull String nome) {
		return perfilRepository.findByPernom(nome).orElse(null);
	}

	public Permissao findByPerfilAndRecurso(Perfil perfil, Recurso recurso) {
		return permissaoRepository.findByPerfil_percodAndRecurso_recnom(perfil.getPercod(), recurso.getRecnom()).orElse(null);
	}

	public PermissaoDTO findByIdApi(@NonNull Long codigo) throws EntityNotFoundException {
		Permissao permissao = findByEntity(codigo);
		return new PermissaoDTO(permissao);
	}

	public Permissao findByEntity(@NonNull Long codigo) {
		return permissaoRepository.findById(codigo).orElseThrow(() -> new EntityNotFoundException("Permissão não encontrada com o código: " + codigo));
	}

	public Perfil createUpdatePerfil(@NonNull Perfil perfil) {
		return perfilRepository.save(perfil);
	}

	public PermissaoDTO saveEntityPermissao(Perfil perfil, @NonNull PermissaoModel permissaoModel) {
		validatePermissaoFields(perfil, permissaoModel);

		Recurso recurso = recursoService.findByIdEntity(permissaoModel.getRecurso().getNome());
		Permissao permissao = new Permissao(null, perfil, recurso, permissaoModel.getListar(), permissaoModel.getBuscar(), permissaoModel.getCriar(), permissaoModel.getEditar(), permissaoModel.getDeletar());

		return new PermissaoDTO(permissaoRepository.save(permissao));
	}

	public PermissaoDTO updateEntityPermissao(@NonNull Perfil perfil, @NonNull PermissaoModel permissaoModel) {
		validatePermissaoFields(perfil, permissaoModel);

		Recurso recurso = recursoService.findByIdEntity(permissaoModel.getRecurso().getNome());
		Permissao permissaoExistente = findByPerfilAndRecurso(perfil, recurso);

		Permissao permissaoAtualizada = new Permissao(permissaoExistente.getPemcod(), perfil, recurso, permissaoModel.getListar(), permissaoModel.getBuscar(), permissaoModel.getCriar(), permissaoModel.getEditar(), permissaoModel.getDeletar());

		return new PermissaoDTO(permissaoRepository.save(permissaoAtualizada));
	}

	private void validatePermissaoFields(Perfil perfil, PermissaoModel permissaoModel) {
		Objects.requireNonNull(perfil, "Perfil da Permissão está nulo.");
		Objects.requireNonNull(permissaoModel.getRecurso(), "Recurso da Permissão está nulo.");
		Objects.requireNonNull(permissaoModel.getCriar(), "Campo 'Criar' da Permissão está nulo.");
		Objects.requireNonNull(permissaoModel.getEditar(), "Campo 'Editar' da Permissão está nulo.");
		Objects.requireNonNull(permissaoModel.getDeletar(), "Campo 'Deletar' da Permissão está nulo.");
		Objects.requireNonNull(permissaoModel.getBuscar(), "Campo 'Buscar' da Permissão está nulo.");
		Objects.requireNonNull(permissaoModel.getListar(), "Campo 'Listar' da Permissão está nulo.");
	}

	public ResponseGlobalModel deleteEntityPermissao(@NonNull Long codigo) throws IOException {
		Objects.requireNonNull(codigo, "Código da Permissão está nulo.");
		try {
			permissaoRepository.deleteByPerfil_Percod(codigo);
			return Utils.responseMessageSucess("Apagado com Sucesso.");
		} catch (Exception e) {
			throw new IOException("Erro ao apagar as permissões. Mensagem: " + e.getMessage(), e);
		}
	}

	public RecursoDTO getNomeRecursoDTO(@NonNull String nome) throws EntityNotFoundException {
		return new RecursoDTO(recursoService.findByIdEntity(nome));
	}

}
