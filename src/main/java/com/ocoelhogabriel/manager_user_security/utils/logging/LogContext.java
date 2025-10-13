package com.ocoelhogabriel.manager_user_security.utils.logging;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Classe para armazenar contexto de rastreamento para logs.
 * Mantém informações como IDs de transação e de requisição.
 */
public class LogContext {
    private static final ThreadLocal<Map<String, String>> CONTEXT = new ThreadLocal<>() {
        @Override
        protected Map<String, String> initialValue() {
            return new ConcurrentHashMap<>();
        }
        
        @Override
        public void remove() {
            get().clear();
            super.remove();
        }
    };

    public static final String TRACE_ID = "traceId";
    public static final String REQUEST_ID = "requestId";
    public static final String USER_ID = "userId";
    public static final String SESSION_ID = "sessionId";
    public static final String TENANT_ID = "tenantId";
    
    private LogContext() {
        // Construtor privado para impedir instanciação
    }

    /**
     * Define o valor de um atributo no contexto de log atual.
     * 
     * @param key nome do atributo
     * @param value valor do atributo
     */
    public static void put(String key, String value) {
        CONTEXT.get().put(key, value);
    }

    /**
     * Recupera um valor do contexto de log atual.
     * 
     * @param key nome do atributo
     * @return valor do atributo ou null se não existir
     */
    public static String get(String key) {
        return CONTEXT.get().get(key);
    }

    /**
     * Remove um atributo do contexto de log atual.
     * 
     * @param key nome do atributo a ser removido
     */
    public static void remove(String key) {
        CONTEXT.get().remove(key);
    }

    /**
     * Limpa todos os atributos do contexto de log atual.
     */
    public static void clear() {
        CONTEXT.get().clear();
    }

    /**
     * Obtém uma cópia imutável do mapa de contexto atual.
     * 
     * @return mapa do contexto de log
     */
    public static Map<String, String> getContext() {
        return Map.copyOf(CONTEXT.get());
    }

    /**
     * Inicia um novo contexto de rastreamento com IDs gerados automaticamente.
     * 
     * @return ID de rastreamento gerado
     */
    public static String initTraceContext() {
        String traceId = UUID.randomUUID().toString();
        String requestId = UUID.randomUUID().toString();
        
        put(TRACE_ID, traceId);
        put(REQUEST_ID, requestId);
        
        return traceId;
    }
    
    /**
     * Define o ID do usuário no contexto atual.
     * 
     * @param userId ID do usuário
     */
    public static void setUserId(String userId) {
        put(USER_ID, userId);
    }
    
    /**
     * Define o ID da sessão no contexto atual.
     * 
     * @param sessionId ID da sessão
     */
    public static void setSessionId(String sessionId) {
        put(SESSION_ID, sessionId);
    }
    
    /**
     * Define o ID do tenant no contexto atual.
     * 
     * @param tenantId ID do tenant
     */
    public static void setTenantId(String tenantId) {
        put(TENANT_ID, tenantId);
    }
}