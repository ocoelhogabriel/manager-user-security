package com.ocoelhogabriel.manager_user_security.utils.logging;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

/**
 * Gerenciador de logs padronizado para a aplicação.
 * Implementa logging estruturado compatível com ferramentas como DataDog e ELK.
 */
public final class LogManager {

    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();
    
    private LogManager() {
        // Construtor privado para impedir instanciação
    }
    
    /**
     * Obtém o logger para a classe especificada.
     * 
     * @param clazz classe para a qual obter o logger
     * @return o logger SLF4J para a classe
     */
    public static Logger getLogger(Class<?> clazz) {
        return LoggerFactory.getLogger(clazz);
    }
    
    /**
     * Gera um log de debug estruturado com contexto padronizado.
     * 
     * @param logger o logger SLF4J a ser utilizado
     * @param category categoria do log
     * @param message mensagem do log
     * @param args pares de chave-valor para adicionar ao contexto do log
     */
    public static void debug(Logger logger, LogCategory category, String message, Object... args) {
        if (logger.isDebugEnabled()) {
            log(logger, LogSeverity.DEBUG, category, message, null, args);
        }
    }
    
    /**
     * Gera um log de info estruturado com contexto padronizado.
     * 
     * @param logger o logger SLF4J a ser utilizado
     * @param category categoria do log
     * @param message mensagem do log
     * @param args pares de chave-valor para adicionar ao contexto do log
     */
    public static void info(Logger logger, LogCategory category, String message, Object... args) {
        log(logger, LogSeverity.INFO, category, message, null, args);
    }
    
    /**
     * Gera um log de warning estruturado com contexto padronizado.
     * 
     * @param logger o logger SLF4J a ser utilizado
     * @param category categoria do log
     * @param message mensagem do log
     * @param args pares de chave-valor para adicionar ao contexto do log
     */
    public static void warn(Logger logger, LogCategory category, String message, Object... args) {
        log(logger, LogSeverity.WARN, category, message, null, args);
    }
    
    /**
     * Gera um log de warning estruturado com exceção e contexto padronizado.
     * 
     * @param logger o logger SLF4J a ser utilizado
     * @param category categoria do log
     * @param message mensagem do log
     * @param throwable exceção a ser logada
     * @param args pares de chave-valor para adicionar ao contexto do log
     */
    public static void warn(Logger logger, LogCategory category, String message, Throwable throwable, Object... args) {
        log(logger, LogSeverity.WARN, category, message, throwable, args);
    }
    
    /**
     * Gera um log de erro estruturado com contexto padronizado.
     * 
     * @param logger o logger SLF4J a ser utilizado
     * @param category categoria do log
     * @param message mensagem do log
     * @param args pares de chave-valor para adicionar ao contexto do log
     */
    public static void error(Logger logger, LogCategory category, String message, Object... args) {
        log(logger, LogSeverity.ERROR, category, message, null, args);
    }
    
    /**
     * Gera um log de erro estruturado com exceção e contexto padronizado.
     * 
     * @param logger o logger SLF4J a ser utilizado
     * @param category categoria do log
     * @param message mensagem do log
     * @param throwable exceção a ser logada
     * @param args pares de chave-valor para adicionar ao contexto do log
     */
    public static void error(Logger logger, LogCategory category, String message, Throwable throwable, Object... args) {
        log(logger, LogSeverity.ERROR, category, message, throwable, args);
    }
    
    /**
     * Gera um log de erro fatal estruturado com contexto padronizado.
     * 
     * @param logger o logger SLF4J a ser utilizado
     * @param category categoria do log
     * @param message mensagem do log
     * @param args pares de chave-valor para adicionar ao contexto do log
     */
    public static void fatal(Logger logger, LogCategory category, String message, Object... args) {
        log(logger, LogSeverity.FATAL, category, message, null, args);
    }
    
    /**
     * Gera um log de erro fatal estruturado com exceção e contexto padronizado.
     * 
     * @param logger o logger SLF4J a ser utilizado
     * @param category categoria do log
     * @param message mensagem do log
     * @param throwable exceção a ser logada
     * @param args pares de chave-valor para adicionar ao contexto do log
     */
    public static void fatal(Logger logger, LogCategory category, String message, Throwable throwable, Object... args) {
        log(logger, LogSeverity.FATAL, category, message, throwable, args);
    }
    
    /**
     * Gera um log de auditoria estruturado com informações específicas para auditoria.
     * 
     * @param logger o logger SLF4J a ser utilizado
     * @param action ação realizada
     * @param resourceType tipo de recurso afetado
     * @param resourceId ID do recurso afetado
     * @param status status da ação (success/failure)
     * @param details detalhes adicionais da ação
     */
    public static void audit(Logger logger, String action, String resourceType, String resourceId, String status, Map<String, Object> details) {
        Map<String, Object> auditData = new HashMap<>();
        auditData.put("action", action);
        auditData.put("resourceType", resourceType);
        auditData.put("resourceId", resourceId);
        auditData.put("status", status);
        
        if (details != null) {
            auditData.put("details", details);
        }
        
        Object[] args = {"auditData", auditData};
        log(logger, LogSeverity.INFO, LogCategory.AUDIT, action, null, args);
    }
    
    /**
     * Método central de logging que formata e envia logs estruturados.
     */
    private static void log(Logger logger, LogSeverity severity, LogCategory category, 
                           String message, Throwable throwable, Object... args) {
        try {
            // Adicionar contexto MDC para appenders
            MDC.put("category", category.getValue());
            MDC.put("severity", severity.getValue());
            
            // Obter qualquer contexto de rastreamento existente
            Map<String, String> traceContext = LogContext.getContext();
            for (Map.Entry<String, String> entry : traceContext.entrySet()) {
                MDC.put(entry.getKey(), entry.getValue());
            }
            
            // Criar objeto JSON estruturado para o log
            Map<String, Object> logData = new HashMap<>();
            logData.put("timestamp", Instant.now().toString());
            logData.put("severity", severity.getValue());
            logData.put("category", category.getValue());
            logData.put("message", message);
            
            // Adicionar contexto de rastreamento
            if (!traceContext.isEmpty()) {
                logData.put("context", traceContext);
            }
            
            // Adicionar metadados extras (pares chave-valor)
            if (args != null && args.length > 0) {
                if (args.length % 2 != 0) {
                    logData.put("warning", "Odd number of context arguments provided to logger");
                }
                
                for (int i = 0; i < args.length - 1; i += 2) {
                    if (args[i] != null) {
                        logData.put(args[i].toString(), args[i + 1]);
                    }
                }
            }
            
            // Serializar para JSON se o appender não fizer isso automaticamente
            String logMessage = message;
            if (logger.isDebugEnabled()) {
                try {
                    logMessage = OBJECT_MAPPER.writeValueAsString(logData);
                } catch (JsonProcessingException e) {
                    logMessage = message + " [Erro ao serializar contexto do log]";
                }
            }
            
            // Registrar o log com a severidade apropriada
            switch (severity) {
                case DEBUG:
                    if (throwable != null) {
                        logger.debug(logMessage, throwable);
                    } else {
                        logger.debug(logMessage);
                    }
                    break;
                case INFO:
                    if (throwable != null) {
                        logger.info(logMessage, throwable);
                    } else {
                        logger.info(logMessage);
                    }
                    break;
                case WARN:
                    if (throwable != null) {
                        logger.warn(logMessage, throwable);
                    } else {
                        logger.warn(logMessage);
                    }
                    break;
                case ERROR:
                case FATAL:
                    if (throwable != null) {
                        logger.error(logMessage, throwable);
                    } else {
                        logger.error(logMessage);
                    }
                    break;
                default:
                    logger.info(logMessage);
            }
        } finally {
            // Limpar o MDC após o log
            MDC.clear();
        }
    }
}