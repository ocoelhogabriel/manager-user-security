package com.ocoelhogabriel.manager_user_security.infrastructure.adapters.services;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.ocoelhogabriel.manager_user_security.domain.constraints.MessageConstraints;
import com.ocoelhogabriel.manager_user_security.domain.constraints.MessageFormatterUtil;
import com.ocoelhogabriel.manager_user_security.domain.entities.UsuarioModel;
import com.ocoelhogabriel.manager_user_security.interfaces.web.dtos.UsuarioDTO;
import com.ocoelhogabriel.manager_user_security.interfaces.web.dtos.UsuarioPermissaoDTO;

import jakarta.persistence.EntityNotFoundException;

/**
 * Implementação do serviço de Usuario
 * Aplica SOLID principles - Single Responsibility Principle
 * Segue Clean Architecture - Infrastructure Layer
 */
@Service
public final class UsuarioServiceImpl
        implements com.ocoelhogabriel.manager_user_security.domain.services.UsuarioServInterface {

    private static final Logger LOGGER = LoggerFactory.getLogger(UsuarioServiceImpl.class);
    private final Map<String, UsuarioModel> usuarioStorage = new ConcurrentHashMap<>();

    private UsuarioServiceImpl() {
    }

    @Override
    public ResponseEntity<UsuarioDTO> saveUpdateEncodePassword(UsuarioModel usuario)
            throws EntityNotFoundException, IOException {
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug(MessageConstraints.USER_SAVING);
        }

        if (usuario.getLogin() == null) {
            if (LOGGER.isWarnEnabled()) {
                LOGGER.warn(MessageConstraints.AUTH_USERNAME_NULL);
            }
            throw new IllegalArgumentException(MessageConstraints.AUTH_USERNAME_NULL);
        }

        usuarioStorage.put(usuario.getLogin(), usuario);

        if (LOGGER.isInfoEnabled()) {
            LOGGER.info(MessageConstraints.USER_CREATED);
        }

        return ResponseEntity.ok(new UsuarioDTO());
    }

    @Override
    public ResponseEntity<UsuarioDTO> saveUpdateEncodePassword(Long codigo, UsuarioModel usuario)
            throws EntityNotFoundException, IOException {
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug(MessageFormatterUtil.format(MessageConstraints.USER_UPDATING, codigo));
        }

        if (usuario.getLogin() == null) {
            if (LOGGER.isWarnEnabled()) {
                LOGGER.warn(MessageFormatterUtil.format(MessageConstraints.AUTH_USERNAME_NULL));
            }
            throw new IllegalArgumentException(MessageConstraints.AUTH_USERNAME_NULL);
        }

        usuarioStorage.put(usuario.getLogin(), usuario);

        if (LOGGER.isInfoEnabled()) {
            LOGGER.info(MessageConstraints.USER_UPDATED);
        }

        return ResponseEntity.ok(new UsuarioDTO());
    }

    @Override
    public ResponseEntity<UsuarioDTO> findById(Long codigo) throws EntityNotFoundException, IOException {
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug(MessageFormatterUtil.format(MessageConstraints.USER_FINDING_BY_ID, codigo));
        }

        // Aqui iria a lógica para buscar o usuário

        return ResponseEntity.ok(new UsuarioDTO());
    }

    @Override
    public ResponseEntity<UsuarioPermissaoDTO> findByIdPermission(Long codigo)
            throws EntityNotFoundException, IOException {
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug(MessageFormatterUtil.format(MessageConstraints.USER_FINDING_BY_ID, codigo));
        }

        // Aqui iria a lógica para buscar as permissões do usuário

        return ResponseEntity.ok(new UsuarioPermissaoDTO());
    }

    @Override
    public ResponseEntity<List<UsuarioDTO>> findAll() throws EntityNotFoundException, IOException {
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug(MessageConstraints.USER_FINDING_ALL);
        }

        // Aqui iria a lógica para listar todos os usuários

        return ResponseEntity.ok(List.of());
    }

    @Override
    public ResponseEntity<Page<UsuarioDTO>> findAll(String filtro, Pageable pageable)
            throws EntityNotFoundException, IOException {
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug(MessageFormatterUtil.format(MessageConstraints.USER_FINDING_WITH_FILTER, filtro));
        }

        // Aqui iria a lógica para buscar usuários paginados com filtro

        return ResponseEntity.ok(Page.empty());
    }

    @Override
    public ResponseEntity<Void> delete(Long codigo) throws IOException {
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug(MessageFormatterUtil.format(MessageConstraints.USER_DELETING, codigo));
        }

        // Aqui iria a lógica para excluir o usuário

        if (LOGGER.isInfoEnabled()) {
            LOGGER.info(MessageConstraints.USER_DELETED);
        }

        return ResponseEntity.ok().build();
    }

    // Métodos temporários para compatibilidade com código existente
    public com.ocoelhogabriel.manager_user_security.infrastructure.adapters.persistence.entities.Usuario findLoginEntity(
            final String login) {
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug(MessageFormatterUtil.format(MessageConstraints.USER_FINDING_BY_LOGIN, login));
        }

        // Retorna null pois não temos acesso à camada de entidade
        return null;
    }

    public UsuarioModel findLoginEntityNull(final String login) {
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug(MessageFormatterUtil.format(MessageConstraints.USER_FINDING_BY_LOGIN, login));
        }

        return usuarioStorage.get(login);
    }

    public UsuarioModel saveUpdateEntity(final UsuarioModel usuario) {
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug(MessageConstraints.USER_SAVING);
        }

        if (usuario.getLogin() != null) {
            usuarioStorage.put(usuario.getLogin(), usuario);

            if (LOGGER.isInfoEnabled()) {
                LOGGER.info(MessageConstraints.USER_UPDATED);
            }
        } else {
            if (LOGGER.isWarnEnabled()) {
                LOGGER.warn(MessageConstraints.AUTH_USERNAME_NULL);
            }
        }

        return usuario;
    }
}