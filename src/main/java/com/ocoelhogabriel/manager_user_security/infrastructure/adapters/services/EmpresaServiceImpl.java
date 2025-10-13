package com.ocoelhogabriel.manager_user_security.infrastructure.adapters.services;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.ocoelhogabriel.manager_user_security.application.services.EmpresaService;
import com.ocoelhogabriel.manager_user_security.domain.constraints.MessageConstraints;
import com.ocoelhogabriel.manager_user_security.domain.constraints.MessageFormatterUtil;
import com.ocoelhogabriel.manager_user_security.domain.entities.EmpresaModel;

/**
 * Implementação do serviço de Empresa
 * Aplica SOLID principles - Single Responsibility Principle
 * Segue Clean Architecture - Infrastructure Layer
 */
@Service
public final class EmpresaServiceImpl implements EmpresaService {
    
    private static final Logger LOGGER = LoggerFactory.getLogger(EmpresaServiceImpl.class);
    private final Map<Long, EmpresaModel> empresaStorage = new ConcurrentHashMap<>();
    
    public EmpresaServiceImpl() {
        // Construtor padrão
    }

    
    @Override
    public List<EmpresaModel> findAll() {
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug(MessageConstraints.ENTERPRISE_FINDING_ALL);
        }
        return new ArrayList<>(empresaStorage.values());
    }
    
    @Override
    public EmpresaModel findById(final Long id) {
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug(MessageFormatterUtil.format(MessageConstraints.ENTERPRISE_FINDING_BY_ID, id));
        }
        
        EmpresaModel empresa = empresaStorage.get(id);
        
        if (empresa == null && LOGGER.isWarnEnabled()) {
            LOGGER.warn(MessageFormatterUtil.format(MessageConstraints.ENTERPRISE_NOT_FOUND_ID, id));
        }
        
        return empresa;
    }
    
    @Override
    public EmpresaModel save(final EmpresaModel empresa) {
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug(MessageFormatterUtil.format(MessageConstraints.ENTERPRISE_SAVING, 
                empresa != null ? empresa.getNome() : "null"));
        }
        
        if (empresa == null) {
            if (LOGGER.isWarnEnabled()) {
                LOGGER.warn("Tentativa de salvar empresa nula");
            }
            return null;
        }
        
        if (empresa.getCnpj() != null) {
            empresaStorage.put(empresa.getCnpj(), empresa);
            
            if (LOGGER.isInfoEnabled()) {
                if (empresaStorage.containsKey(empresa.getCnpj())) {
                    LOGGER.info(MessageConstraints.ENTERPRISE_UPDATED);
                } else {
                    LOGGER.info(MessageConstraints.ENTERPRISE_CREATED);
                }
            }
        } else {
            if (LOGGER.isWarnEnabled()) {
                LOGGER.warn("Empresa sem CNPJ não pode ser salva");
            }
        }
        
        return empresa;
    }
    
    // Métodos temporários para compatibilidade
    public com.ocoelhogabriel.manager_user_security.infrastructure.adapters.persistence.entities.Empresa empresaFindByCnpjEntity(final Long cnpj) {
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug(MessageFormatterUtil.format(MessageConstraints.ENTERPRISE_FINDING_BY_CNPJ, cnpj));
        }
        // Retorna null pois não temos acesso à camada de entidade
        return null;
    }
    
    public EmpresaModel empresaSave(final EmpresaModel empresa) {
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug(MessageFormatterUtil.format(MessageConstraints.ENTERPRISE_SAVING, 
                empresa != null ? empresa.getNome() : "null"));
        }
        return save(empresa);
    }
    
    @Override
    public void deleteById(final Long id) {
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug(MessageFormatterUtil.format(MessageConstraints.ENTERPRISE_DELETING, id));
        }
        
        if (empresaStorage.containsKey(id)) {
            empresaStorage.remove(id);
            if (LOGGER.isInfoEnabled()) {
                LOGGER.info(MessageConstraints.ENTERPRISE_DELETED);
            }
        } else if (LOGGER.isWarnEnabled()) {
            LOGGER.warn(MessageFormatterUtil.format(MessageConstraints.ENTERPRISE_NOT_FOUND_ID, id));
        }
    }
    
    @Override
    public EmpresaModel findByCnpj(final String cnpj) {
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug(MessageFormatterUtil.format(MessageConstraints.ENTERPRISE_FINDING_BY_CNPJ, cnpj));
        }
        
        try {
            long cnpjValue = Long.parseLong(cnpj);
            return empresaStorage.values().stream()
                    .filter(empresa -> empresa.getCnpj() != null && empresa.getCnpj() == cnpjValue)
                    .findFirst()
                    .orElse(null);
        } catch (NumberFormatException e) {
            if (LOGGER.isWarnEnabled()) {
                LOGGER.warn(MessageFormatterUtil.format(MessageConstraints.ENTERPRISE_INVALID_CNPJ, cnpj), e);
            }
            return null;
        }
    }
    
    @Override
    public List<EmpresaModel> findByNome(final String nome) {
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug(MessageFormatterUtil.format(MessageConstraints.ENTERPRISE_FINDING_BY_NAME, nome));
        }
        
        return empresaStorage.values().stream()
                .filter(empresa -> empresa.getNome() != null && 
                        empresa.getNome().toLowerCase().contains(nome.toLowerCase()))
                .toList();
    }
    
    @Override
    public boolean existsByCnpj(final String cnpj) {
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug(MessageFormatterUtil.format(MessageConstraints.ENTERPRISE_CHECK_EXISTS_BY_CNPJ, cnpj));
        }
        
        try {
            long cnpjValue = Long.parseLong(cnpj);
            return empresaStorage.values().stream()
                    .anyMatch(empresa -> empresa.getCnpj() != null && empresa.getCnpj() == cnpjValue);
        } catch (NumberFormatException e) {
            if (LOGGER.isWarnEnabled()) {
                LOGGER.warn(MessageFormatterUtil.format(MessageConstraints.ENTERPRISE_INVALID_CNPJ, cnpj), e);
            }
            return false;
        }
    }
}
