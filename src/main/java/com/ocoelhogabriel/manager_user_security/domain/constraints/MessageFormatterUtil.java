package com.ocoelhogabriel.manager_user_security.domain.constraints;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Classe utilitária para formatação e centralização de mensagens.
 * Utiliza as constantes definidas em MessageConstraints e MessageTemplateKeys.
 * Esta classe substitui a implementação anterior do MessageUtil.
 */
public final class MessageFormatterUtil {

    private static final Logger LOGGER = LoggerFactory.getLogger(MessageFormatterUtil.class);

    private MessageFormatterUtil() {
        // Construtor privado para classe utilitária
    }

    /**
     * Formata uma mensagem com parâmetros
     * @param message A mensagem ou chave de mensagem
     * @param params Os parâmetros para substituição
     * @return A mensagem formatada
     */
    public static String format(String message, Object... params) {
        if (message == null) {
            if (LOGGER.isWarnEnabled()) {
                LOGGER.warn("Tentativa de formatar mensagem nula");
            }
            return MessageConstraints.ERROR_GENERIC;
        }
        
        return MessageConstraints.MessageFormatter.format(message, params);
    }
    
    /**
     * Mapeia uma chave de template para a mensagem correspondente e formata com parâmetros
     * @param templateKey A chave do template
     * @param params Os parâmetros para substituição
     * @return A mensagem formatada ou mensagem de erro genérica se o template não for encontrado
     */
    public static String formatTemplate(String templateKey, Object... params) {
        try {
            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("Processando template de mensagem: {}", templateKey);
            }
            
            // Mapear a chave do template para a mensagem correspondente
            String template = mapTemplateKeyToMessage(templateKey);
            
            if (template == null) {
                if (LOGGER.isWarnEnabled()) {
                    LOGGER.warn("Template de mensagem não encontrado: {}", templateKey);
                }
                return MessageConstraints.ERROR_GENERIC;
            }
            
            return format(template, params);
        } catch (Exception e) {
            if (LOGGER.isErrorEnabled()) {
                LOGGER.error("Erro ao processar template de mensagem: {}", e.getMessage(), e);
            }
            return MessageConstraints.ERROR_GENERIC;
        }
    }
    
    /**
     * Mapeia uma chave de template para a mensagem correspondente em MessageConstraints
     * @param templateKey A chave do template
     * @return A mensagem correspondente ou null se não encontrada
     */
    private static String mapTemplateKeyToMessage(String templateKey) {
        // Entidades não encontradas por ID
        if (MessageTemplateKeys.PLANT_NOT_FOUND_ID.equals(templateKey)) {
            return "Planta com ID {0} não encontrada.";
        }
        if (MessageTemplateKeys.USER_NOT_FOUND_ID.equals(templateKey)) {
            return "Usuário com ID {0} não encontrado.";
        }
        if (MessageTemplateKeys.ENTERPRISE_NOT_FOUND_ID.equals(templateKey)) {
            return MessageConstraints.ENTERPRISE_NOT_FOUND_ID;
        }
        if (MessageTemplateKeys.RESOURCE_NOT_FOUND_ID.equals(templateKey)) {
            return MessageConstraints.RESOURCE_NOT_FOUND_ID;
        }
        if (MessageTemplateKeys.PROFILE_NOT_FOUND_ID.equals(templateKey)) {
            return MessageConstraints.PROFILE_NOT_FOUND_ID;
        }
        if (MessageTemplateKeys.PROFILE_NOT_FOUND_NAME.equals(templateKey)) {
            return MessageConstraints.PROFILE_NOT_FOUND_NAME;
        }
        if (MessageTemplateKeys.ENTITY_GENERIC.equals(templateKey)) {
            return "Entidade com ID {0} não encontrada.";
        }
        
        // Templates de autenticação
        if (MessageTemplateKeys.AUTH_USER_NOT_FOUND.equals(templateKey)) {
            return MessageConstraints.AUTH_USER_NOT_FOUND;
        }
        if (MessageTemplateKeys.AUTH_TOKEN_VALIDATION_ERROR.equals(templateKey)) {
            return MessageConstraints.ERROR_JWT_TOKEN_VALIDATION;
        }
        if (MessageTemplateKeys.AUTH_TOKEN_REFRESH_ERROR.equals(templateKey)) {
            return MessageConstraints.ERROR_JWT_TOKEN_REFRESH;
        }
        if (MessageTemplateKeys.AUTH_LOGIN_ERROR.equals(templateKey)) {
            return MessageConstraints.AUTH_FAILED;
        }
        
        // Templates de logs
        if (MessageTemplateKeys.LOG_OPERATION_START.equals(templateKey)) {
            return MessageConstraints.LOG_OPERATION_START;
        }
        if (MessageTemplateKeys.LOG_OPERATION_END.equals(templateKey)) {
            return MessageConstraints.LOG_OPERATION_END;
        }
        if (MessageTemplateKeys.LOG_OPERATION_ERROR.equals(templateKey)) {
            return MessageConstraints.LOG_OPERATION_ERROR;
        }
        
        // Template não encontrado
        return null;
    }
}