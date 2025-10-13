package com.ocoelhogabriel.manager_user_security.infrastructure.adapters.services;

import java.time.Duration;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.ocoelhogabriel.manager_user_security.domain.constraints.MessageConstraints;
import com.ocoelhogabriel.manager_user_security.domain.constraints.UniqueMessageConstraint;
import com.ocoelhogabriel.manager_user_security.domain.services.UniqueMessageService;

/**
 * Implementação do serviço de mensagens únicas.
 * Utiliza a constraint de mensagens únicas para evitar duplicação.
 */
@Service
public class UniqueMessageServiceImpl implements UniqueMessageService {

    private static final Logger LOGGER = LoggerFactory.getLogger(UniqueMessageServiceImpl.class);
    private UniqueMessageConstraint constraint;
    
    public UniqueMessageServiceImpl() {
        this.constraint = UniqueMessageConstraint.getDefaultInstance();
    }
    
    @Override
    public boolean processIfUnique(String message, String context) {
        try {
            if (constraint.canProcessMessage(message, context)) {
                LOGGER.debug("Processando mensagem única: [{}] para contexto: [{}]", message, context);
                return true;
            } else {
                LOGGER.debug("Mensagem duplicada ignorada: [{}] para contexto: [{}]", message, context);
                return false;
            }
        } catch (Exception e) {
            LOGGER.error("Erro ao processar mensagem: {}", e.getMessage(), e);
            // Em caso de erro, permitimos o processamento para evitar perda de mensagens
            return true;
        }
    }

    @Override
    public boolean isUnique(String message, String context) {
        try {
            return constraint.canProcessMessage(message, context);
        } catch (Exception e) {
            LOGGER.error("Erro ao verificar unicidade da mensagem: {}", e.getMessage(), e);
            // Em caso de erro, consideramos a mensagem como única para evitar bloqueios
            return true;
        }
    }

    @Override
    public void setExpirationTime(Duration duration) {
        if (duration == null || duration.isNegative() || duration.isZero()) {
            if (LOGGER.isWarnEnabled()) {
                LOGGER.warn(MessageConstraints.VALIDATION_INVALID_FORMAT, "expirationTime");
            }
            throw new IllegalArgumentException(MessageConstraints.MessageFormatter.format(
                MessageConstraints.VALIDATION_INVALID_FORMAT, "expirationTime"));
        }
        
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("Definindo tempo de expiração para: {}", duration);
        }
        
        // Recria a constraint com o novo tempo de expiração
        this.constraint = UniqueMessageConstraint.getInstance(duration);
    }

    @Override
    public void clearMessageCache() {
        constraint.clearCache();
        LOGGER.debug("Cache de mensagens foi limpo");
    }

    @Override
    public int getMessageCacheSize() {
        return constraint.getCacheSize();
    }
}