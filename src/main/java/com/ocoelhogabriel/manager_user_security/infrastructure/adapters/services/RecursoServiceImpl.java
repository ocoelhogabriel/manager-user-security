package com.ocoelhogabriel.manager_user_security.infrastructure.adapters.services;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.ocoelhogabriel.manager_user_security.application.services.RecursoService;
import com.ocoelhogabriel.manager_user_security.domain.constraints.MessageConstraints;
import com.ocoelhogabriel.manager_user_security.domain.constraints.MessageFormatterUtil;
import com.ocoelhogabriel.manager_user_security.domain.entities.RecursoModel;


/**
 * Implementação do serviço de Recurso
 * Aplica SOLID principles - Single Responsibility Principle
 * Segue Clean Architecture - Infrastructure Layer
 */
@Service
public final class RecursoServiceImpl implements RecursoService {
    
    private static final Logger LOGGER = LoggerFactory.getLogger(RecursoServiceImpl.class);
    private final Map<Long, RecursoModel> recursoStorage = new ConcurrentHashMap<>();
    private long nextId = 1L;
    
    private RecursoServiceImpl() {
    }
    
    @Override
    public List<RecursoModel> findAll() {
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug(MessageFormatterUtil.format(MessageConstraints.RESOURCE_RETRIEVING_ALL));
        }
        return new ArrayList<>(recursoStorage.values());
    }
    
    @Override
    public RecursoModel findById(final Long id) {
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug(MessageFormatterUtil.format(MessageConstraints.RESOURCE_FINDING_BY_ID, id));
        }
        return recursoStorage.get(id);
    }
    
    @Override
    public RecursoModel save(final RecursoModel recurso) {
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug(MessageFormatterUtil.format(MessageConstraints.RESOURCE_SAVING_NEW));
        }
        recursoStorage.put(nextId++, recurso);
        return recurso;
    }
    
    @Override
    public Object findByIdEntity(final String nome) {
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug(MessageFormatterUtil.format(MessageConstraints.RESOURCE_FINDING_ENTITY_BY_NAME, nome));
        }
        // Método para compatibilidade com código legado
        // Retorna null pois não temos acesso direto à camada de entidade
        return null;
    }
    
    public RecursoModel saveEntity(final RecursoModel recurso) {
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug(MessageFormatterUtil.format(MessageConstraints.RESOURCE_SAVING_ENTITY));
        }
        return save(recurso);
    }
    
    @Override
    public void deleteById(final Long id) {
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug(MessageFormatterUtil.format(MessageConstraints.RESOURCE_DELETING, id));
        }
        recursoStorage.remove(id);
    }
    
    @Override
    public List<RecursoModel> findByTipo(final String tipo) {
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug(MessageFormatterUtil.format(MessageConstraints.RESOURCE_FIND_BY_TYPE, tipo));
        }
        // RecursoModel não tem campo tipo, retorna lista vazia
        return new ArrayList<>();
    }
    
    
    @Override
    public boolean existsByNome(final String nome) {
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug(MessageFormatterUtil.format(MessageConstraints.RESOURCE_CHECKING_EXISTS_BY_NAME, nome));
        }
        return recursoStorage.values().stream()
                .anyMatch(recurso -> nome.equals(recurso.getNome().name()));
    }
}
