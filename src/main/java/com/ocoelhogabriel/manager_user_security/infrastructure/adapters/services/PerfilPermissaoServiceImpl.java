package com.ocoelhogabriel.manager_user_security.infrastructure.adapters.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.ocoelhogabriel.manager_user_security.application.services.PerfilPermissaoService;
import com.ocoelhogabriel.manager_user_security.domain.constraints.MessageConstraints;
import com.ocoelhogabriel.manager_user_security.domain.constraints.MessageFormatterUtil;
import com.ocoelhogabriel.manager_user_security.domain.constraints.MessageTemplateKeys;
import com.ocoelhogabriel.manager_user_security.domain.entities.PerfilModel;
import com.ocoelhogabriel.manager_user_security.domain.entities.PermissaoModel;
import com.ocoelhogabriel.manager_user_security.domain.services.PerfilService;
import com.ocoelhogabriel.manager_user_security.infrastructure.adapters.repositories.PerfilRepository;
import com.ocoelhogabriel.manager_user_security.infrastructure.adapters.persistence.entities.Perfil;


/**
 * Implementação do serviço de Perfil Permissão
 * Aplica SOLID principles - Single Responsibility Principle
 * Segue Clean Architecture - Infrastructure Layer
 */
@Service
public final class PerfilPermissaoServiceImpl implements PerfilPermissaoService, PerfilService {
    
    private static final Logger LOGGER = LoggerFactory.getLogger(PerfilPermissaoServiceImpl.class);
    private final PerfilRepository perfilRepository;
    
    public PerfilPermissaoServiceImpl(PerfilRepository perfilRepository) {
        this.perfilRepository = perfilRepository;
    }

    
    @Override
    public boolean hasPermission(final Long perfilId, final Long recursoId) {
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug(MessageFormatterUtil.format(MessageConstraints.PERMISSION_CHECKING, perfilId, recursoId));
        }
        
        try {
            PermissaoModel permissao = findByPerfilAndRecurso(perfilId, recursoId);
            return permissao != null;
        } catch (Exception e) {
            LOGGER.error(MessageFormatterUtil.format(MessageConstraints.PERMISSION_ERROR_CHECK, e.getMessage()), e);
            return false;
        }
    }
    
    @Override
    public void grantPermission(final Long perfilId, final Long recursoId) {
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug(MessageFormatterUtil.format(MessageConstraints.PERMISSION_GRANTING, perfilId, recursoId));
        }
        
        try {
            PermissaoModel existingPermissao = findByPerfilAndRecurso(perfilId, recursoId);
            if (existingPermissao == null) {
                // Criar nova permissão com valores padrão
                PermissaoModel novaPermissao = new PermissaoModel();
                novaPermissao.setListar(1);
                novaPermissao.setBuscar(1);
                novaPermissao.setCriar(1);
                novaPermissao.setEditar(1);
                novaPermissao.setDeletar(1);
                savePermissao(perfilId, novaPermissao);
            }
        } catch (Exception e) {
            LOGGER.error(MessageFormatterUtil.format(MessageConstraints.PERMISSION_ERROR_GRANT, e.getMessage()), e);
        }
    }
    
    @Override
    public void revokePermission(final Long perfilId, final Long recursoId) {
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug(MessageFormatterUtil.format(MessageConstraints.PERMISSION_REVOKING, perfilId, recursoId));
        }
        // Implementação simplificada
    }
    
    @Override
    public void revokeAllPermissions(final Long perfilId) {
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug(MessageFormatterUtil.format(MessageConstraints.PERMISSION_REVOKING_ALL, perfilId));
        }
        // Implementação simplificada
    }
    
    @Override
    public PerfilModel findByNome(final String nome) {
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug(MessageFormatterUtil.format(MessageConstraints.PROFILE_FIND_BY_NAME, nome));
        }
        
        var perfilEntity = perfilRepository.findByPernom(nome);
        if (perfilEntity.isPresent()) {
            var perfil = perfilEntity.get();
            return new PerfilModel(perfil.getNome(), perfil.getDescricao(), null);
        }
        
        if (LOGGER.isInfoEnabled()) {
            LOGGER.info(MessageFormatterUtil.format(MessageConstraints.PROFILE_NOT_FOUND_NAME, nome));
        }
        return null;
    }
    
    @Override
    public PerfilModel save(final PerfilModel perfil) {
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug(MessageFormatterUtil.format(MessageConstraints.PROFILE_SAVING, perfil));
        }
        
        // Converter PerfilModel para Perfil entity
        var perfilEntity = new com.ocoelhogabriel.manager_user_security.infrastructure.adapters.persistence.entities.Perfil();
        perfilEntity.atualizarNome(perfil.getNome());
        perfilEntity.setDescricao(perfil.getDescricao());
        var saved = perfilRepository.save(perfilEntity);
        return new PerfilModel(saved.getNome(), saved.getDescricao(), null);
    }
    
    @Override
    public PerfilModel findByIdPerfilEntity(final String perfil) {
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug(MessageFormatterUtil.format(MessageConstraints.PROFILE_FIND_BY_ID, perfil));
        }
        
        try {
            Long id = Long.valueOf(perfil);
            var perfilEntity = perfilRepository.findById(id);
            if (perfilEntity.isPresent()) {
                var p = perfilEntity.get();
                return new PerfilModel(p.getNome(), p.getDescricao(), null);
            }
            
            if (LOGGER.isInfoEnabled()) {
                LOGGER.info(MessageFormatterUtil.format(MessageTemplateKeys.PROFILE_NOT_FOUND_ID, id));
            }
        } catch (NumberFormatException e) {
            // Se não é um número, busca por nome
            return findByNome(perfil);
        }
        return null;
    }
    
    @Override
    public PerfilModel createOrUpdate(final PerfilModel perfil) {
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug(MessageFormatterUtil.format(MessageConstraints.PROFILE_CREATE_UPDATE, perfil));
        }
        return save(perfil);
    }
    
    @Override
    public PermissaoModel findByPerfilAndRecurso(final Long perfilId, final Long recursoId) {
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug(MessageFormatterUtil.format(MessageConstraints.PERMISSION_FIND_BY_PROFILE_RESOURCE, perfilId, recursoId));
        }
        return null; // Implementação simplificada
    }
    
    @Override
    public void savePermissao(final Long perfilId, final PermissaoModel permissao) {
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug(MessageFormatterUtil.format(MessageConstraints.PERMISSION_SAVING, perfilId));
        }
        // Implementação simplificada
    }

    
    public Perfil createUpdatePerfil(
            final Perfil perfil) {
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug(MessageFormatterUtil.format(MessageConstraints.PROFILE_CREATE_UPDATE, perfil.getNome()));
        }
        
        PerfilModel perfilModel = new PerfilModel();
        perfilModel.setNome(perfil.getNome());
        PerfilModel savedModel = createOrUpdate(perfilModel);
        
        Perfil result = 
            new Perfil();
        result.atualizarNome(savedModel.getNome());
        return result;
    }
}
