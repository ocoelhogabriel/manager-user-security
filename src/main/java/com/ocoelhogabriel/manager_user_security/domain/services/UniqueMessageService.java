package com.ocoelhogabriel.manager_user_security.domain.services;

import java.time.Duration;

/**
 * Interface para o serviço de mensagens que previne mensagens duplicadas.
 * Define operações para processamento de mensagens com detecção de duplicidade.
 */
public interface UniqueMessageService {

    /**
     * Processa uma mensagem apenas se não for duplicada.
     *
     * @param message a mensagem a ser processada
     * @param context o contexto da mensagem (ex: usuário, categoria, etc.)
     * @return true se a mensagem foi processada, false se foi rejeitada por ser duplicada
     */
    boolean processIfUnique(String message, String context);

    /**
     * Verifica se uma mensagem é considerada única no contexto especificado.
     *
     * @param message a mensagem a ser verificada
     * @param context o contexto da mensagem
     * @return true se a mensagem é única, false caso contrário
     */
    boolean isUnique(String message, String context);
    
    /**
     * Define o tempo de expiração para mensagens no cache.
     *
     * @param duration o tempo de expiração
     */
    void setExpirationTime(Duration duration);
    
    /**
     * Limpa o cache de mensagens.
     */
    void clearMessageCache();
    
    /**
     * Obtém o tamanho atual do cache de mensagens.
     * 
     * @return o número de mensagens armazenadas no cache
     */
    int getMessageCacheSize();
}