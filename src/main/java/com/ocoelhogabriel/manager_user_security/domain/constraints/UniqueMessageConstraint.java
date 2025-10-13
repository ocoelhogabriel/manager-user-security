package com.ocoelhogabriel.manager_user_security.domain.constraints;

import java.time.Clock;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Constraint para garantir que mensagens não sejam repetidas em um período específico de tempo.
 * Utiliza um cache baseado em memória para rastrear mensagens recentes.
 * 
 * Esta classe é thread-safe e pode ser usada em ambientes concorrentes.
 */
public final class UniqueMessageConstraint {

    private final Map<MessageKey, LocalDateTime> messageCache;
    private final Duration expirationTime;
    private static UniqueMessageConstraint instance;
    
    // Clock para facilitar testes sem sleep
    private Clock clock;

    /**
     * Construtor privado para implementar o padrão Singleton.
     * @param expirationTime tempo de expiração para mensagens no cache
     */
    private UniqueMessageConstraint(Duration expirationTime) {
        this.messageCache = new ConcurrentHashMap<>();
        this.expirationTime = expirationTime;
        this.clock = Clock.systemDefaultZone();
    }

    /**
     * Obtém a instância única da classe, criando-a se necessário.
     * @param expirationTime tempo de expiração para mensagens no cache
     * @return a instância da constraint
     */
    public static synchronized UniqueMessageConstraint getInstance(Duration expirationTime) {
        if (instance == null) {
            instance = new UniqueMessageConstraint(expirationTime);
        }
        return instance;
    }

    /**
     * Obtém a instância padrão com tempo de expiração de 30 minutos.
     * @return a instância da constraint
     */
    public static UniqueMessageConstraint getDefaultInstance() {
        return getInstance(Duration.ofMinutes(30));
    }
    
    /**
     * Configura um clock personalizado para testes.
     * Permite testes determinísticos sem necessidade de Thread.sleep().
     * 
     * @param clock o clock a ser usado
     */
    public void setClock(Clock clock) {
        this.clock = clock;
    }

    /**
     * Verifica se uma mensagem pode ser processada (não é repetida ou já expirou).
     * Se a mensagem for única ou tiver expirado, ela será registrada no cache.
     * 
     * @param message a mensagem a ser verificada
     * @param context o contexto da mensagem (ex: usuário, categoria, etc)
     * @return true se a mensagem pode ser processada, false se for repetida
     */
    public boolean canProcessMessage(String message, String context) {
        if (message == null || message.trim().isEmpty()) {
            throw new IllegalArgumentException("Message cannot be null or empty");
        }
        
        cleanExpiredMessages();
        
        MessageKey key = new MessageKey(message, context);
        LocalDateTime now = LocalDateTime.now(clock);
        
        return messageCache.compute(key, (k, storedTime) -> {
            if (storedTime == null || now.isAfter(storedTime.plus(expirationTime))) {
                return now;
            }
            return storedTime;
        }) == now;
    }

    /**
     * Remove mensagens expiradas do cache para evitar vazamento de memória.
     * Esta operação é realizada periodicamente quando novos registros são adicionados.
     */
    private void cleanExpiredMessages() {
        LocalDateTime now = LocalDateTime.now(clock);
        messageCache.entrySet().removeIf(entry -> 
            now.isAfter(entry.getValue().plus(expirationTime)));
    }

    /**
     * Limpa manualmente todas as mensagens do cache.
     */
    public void clearCache() {
        messageCache.clear();
    }

    /**
     * Obtém o número atual de mensagens no cache.
     * @return a contagem de mensagens
     */
    public int getCacheSize() {
        cleanExpiredMessages();
        return messageCache.size();
    }

    /**
     * Chave composta para o cache de mensagens, contendo a mensagem e seu contexto.
     */
    private static class MessageKey {
        private final String message;
        private final String context;

        public MessageKey(String message, String context) {
            this.message = message;
            this.context = context != null ? context : "default";
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            MessageKey that = (MessageKey) o;
            return Objects.equals(message, that.message) && 
                   Objects.equals(context, that.context);
        }

        @Override
        public int hashCode() {
            return Objects.hash(message, context);
        }
    }
}