package com.ocoelhogabriel.manager_user_security.services.impl;

import java.io.IOException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.NonNull;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.ocoelhogabriel.manager_user_security.model.UsuarioModel;
import com.ocoelhogabriel.manager_user_security.model.dto.UsuarioDTO;
import com.ocoelhogabriel.manager_user_security.model.dto.UsuarioPermissaoDTO;
import com.ocoelhogabriel.manager_user_security.model.entity.Abrangencia;
import com.ocoelhogabriel.manager_user_security.model.entity.Empresa;
import com.ocoelhogabriel.manager_user_security.model.entity.Perfil;
import com.ocoelhogabriel.manager_user_security.model.entity.Usuario;
import com.ocoelhogabriel.manager_user_security.repository.UsuarioRepository;
import com.ocoelhogabriel.manager_user_security.services.UsuarioServInterface;
import com.ocoelhogabriel.manager_user_security.utils.message.MessageResponse;
import jakarta.persistence.EntityNotFoundException;

@Service
public class UsuarioServiceImpl implements UsuarioServInterface {

	private static final Logger log = LoggerFactory.getLogger(UsuarioServiceImpl.class);

	@Autowired
	private UsuarioRepository userRepository;

	@Autowired
	@Lazy
	private PasswordEncoder passwordEncoder;

	@Autowired
	private PerfilPermissaoServiceImpl permissaoService;

	@Autowired
	private AbrangenciaServiceImpl abrangenciaService;

	@Autowired
	private EmpresaServiceImpl empresaService;

	public UsuarioDTO findLogin(String login) throws EntityNotFoundException {
		return userRepository.findByUsulog(login).map(UsuarioDTO::new).orElseThrow(() -> new EntityNotFoundException("Usuário não existe!"));
	}

	public Usuario findLoginEntity(String login) {
		return userRepository.findByUsulog(login).orElseThrow(() -> new EntityNotFoundException("Usuário não existe!"));
	}

	public Usuario findLoginEntityNull(String login) {
		return userRepository.findByUsulog(login).orElse(null);
	}

	public Page<Usuario> findAllEntity(String nome, @NonNull Pageable pageable) throws EntityNotFoundException {
		Objects.requireNonNull(pageable, "Pageable do Usuário está nulo.");
		Specification<Usuario> spec = Usuario.filterByFields(nome);
		return userRepository.findAll(spec, pageable);
	}

	public List<Usuario> findAllEntity() throws EntityNotFoundException {
		return userRepository.findAll();
	}

	public Usuario findByIdEntity(Long cod) throws EntityNotFoundException {
		return userRepository.findById(cod).orElseThrow(() -> new EntityNotFoundException("Usuário não encontrado com o código: " + cod));
	}

	public UsuarioDTO findByUsuario(Long codigo) throws EntityNotFoundException {
		Usuario user = findByIdEntity(codigo);
		return new UsuarioDTO(user);
	}

	public Usuario saveUpdateEntity(@NonNull Long codigo, @NonNull UsuarioModel userModel) throws EntityNotFoundException, IOException {
		Usuario existingUser = userRepository.findById(codigo).orElseThrow(() -> new EntityNotFoundException("Usuário não encontrado com o código: " + codigo));

		if ("admin".equalsIgnoreCase(existingUser.getUsulog())) {
			log.info("Usuário admin não pode ser alterado: " + existingUser);
			throw new IOException("Usuário admin não pode ser alterado.");
		}

		existingUser = updateUserInfo(existingUser, userModel);
		return userRepository.save(existingUser);
	}

	public Usuario saveUpdateEntity(@NonNull UsuarioModel userModel) throws EntityNotFoundException, IOException {
		Optional<Usuario> existingUserOpt = userRepository.findByUsulog(userModel.getLogin());
		if (existingUserOpt.isPresent() && "admin".equalsIgnoreCase(existingUserOpt.get().getUsulog())) {
			Usuario existingUser = existingUserOpt.get();
			log.info("Usuário com o login admin já existe: " + existingUser);
			throw new IOException("Usuário admin já existe!");
		}

		Usuario newUser = new Usuario();
		newUser = updateUserInfo(newUser, userModel);
		return userRepository.save(newUser);
	}

	@Override
	public ResponseEntity<Page<UsuarioDTO>> findAll(String nome, @NonNull Pageable pageable) throws EntityNotFoundException, IOException {
		Page<Usuario> users = findAllEntity(nome, pageable);
		return MessageResponse.success(users.map(UsuarioDTO::new));
	}

	@Override
	public ResponseEntity<List<UsuarioDTO>> findAll() throws EntityNotFoundException, IOException {
		List<Usuario> users = findAllEntity();
		List<UsuarioDTO> userDTOs = users.stream().map(UsuarioDTO::new).toList();
		return MessageResponse.success(userDTOs);
	}

	@Override
	public ResponseEntity<UsuarioDTO> findById(@NonNull Long codigo) throws EntityNotFoundException, IOException {
		return MessageResponse.success(findByUsuario(codigo));
	}

	@Override
	public ResponseEntity<UsuarioPermissaoDTO> findByIdPermission(@NonNull Long codigo) throws EntityNotFoundException, IOException {
		Usuario user = findByIdEntity(codigo);
		return MessageResponse.success(new UsuarioPermissaoDTO(user, permissaoService.findByIdPerfil(user.getPerfil().getPercod())));
	}

	@Override
	public ResponseEntity<UsuarioDTO> saveUpdateEncodePassword(@NonNull UsuarioModel userModel) throws EntityNotFoundException, IOException {
		Usuario user = saveUpdateEntity(userModel);
		return MessageResponse.create(new UsuarioDTO(user));
	}

	@Override
	public ResponseEntity<UsuarioDTO> saveUpdateEncodePassword(@NonNull Long codigo, @NonNull UsuarioModel userModel) throws EntityNotFoundException, IOException {
		Usuario user = saveUpdateEntity(codigo, userModel);
		return MessageResponse.success(new UsuarioDTO(user));
	}

	@Override
	public ResponseEntity<Void> delete(@NonNull Long codigo) throws IOException {
		try {
			userRepository.deleteById(codigo);
			return MessageResponse.noContent();
		} catch (Exception e) {
			log.error("Erro ao apagar o item. Mensagem: " + e.getMessage(), e);
			throw new IOException("Erro ao apagar o item. Mensagem: " + e.getMessage(), e);
		}
	}

	private Usuario updateUserInfo(Usuario user, UsuarioModel userModel) throws EntityNotFoundException {
		Perfil perfil = permissaoService.findByIdPerfilEntity(userModel.getPerfil());
		Empresa empresa = empresaService.findByIdEntity(userModel.getEmpresa());
		Abrangencia abrangencia = abrangenciaService.findByIdEntity(userModel.getAbrangencia());

		user.setUsunom(userModel.getNome());
		user.setUsucpf(userModel.getCpf());
		user.setUsulog(userModel.getLogin());
		// Só criptografa a senha se ela não estiver vazia
		if (userModel.getSenha() != null && !userModel.getSenha().isEmpty()) {
			user.setUsusen(passwordEncoder.encode(userModel.getSenha()));
		}
		user.setUsuema(Optional.ofNullable(userModel.getEmail()).orElse(""));
		user.setPerfil(perfil);
		user.setAbrangencia(abrangencia);
		user.setEmpresa(empresa);
		return user;
	}

}
