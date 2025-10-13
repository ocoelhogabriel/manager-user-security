package com.ocoelhogabriel.manager_user_security.utils.message;

import org.springframework.stereotype.Component;

import com.ocoelhogabriel.manager_user_security.domain.services.UniqueMessageService;

/**
 * Utilitário para processar mensagens, garantindo que não sejam repetidas.
 * Esta classe é um componente Spring que encapsula o UniqueMessageService,
 * facilitando seu uso em outras partes do sistema.
 */
@Component
public class UniqueMessageUtil {

    private final UniqueMessageService uniqueMessageService;
    private static UniqueMessageUtil instance;
    
    public UniqueMessageUtil(UniqueMessageService uniqueMessageService) {
        this.uniqueMessageService = uniqueMessageService;
    }
    
    /**
     * Verifica se uma mensagem pode ser processada (não é duplicada)
     * e a registra como processada se for única.
     *
     * @param message a mensagem a verificar
     * @return true se a mensagem pode ser processada, false se for duplicada
     */
    public boolean canProcess(String message) {
        return uniqueMessageService.processIfUnique(message, null);
    }
    
    /**
     * Verifica se uma mensagem pode ser processada (não é duplicada) em um contexto específico
     * e a registra como processada se for única.
     *
     * @param message a mensagem a verificar
     * @param context o contexto da mensagem (ex: usuário, categoria, etc)
     * @return true se a mensagem pode ser processada, false se for duplicada
     */
    public boolean canProcess(String message, String context) {
        return uniqueMessageService.processIfUnique(message, context);
    }
    
    /**
     * Verifica se uma mensagem é considerada única no sistema,
     * sem registrá-la como processada.
     * 
     * @param message a mensagem a verificar
     * @return true se a mensagem é única, false se for duplicada
     */
    public boolean isUnique(String message) {
        return uniqueMessageService.isUnique(message, null);
    }
    
    /**
     * Verifica se uma mensagem é considerada única em um contexto específico,
     * sem registrá-la como processada.
     * 
     * @param message a mensagem a verificar
     * @param context o contexto da mensagem
     * @return true se a mensagem é única, false se for duplicada
     */
    public boolean isUnique(String message, String context) {
        return uniqueMessageService.isUnique(message, context);
    }
    
    /**
     * Limpa o cache de mensagens.
     */
    public void clearCache() {
        uniqueMessageService.clearMessageCache();
    }
    
    /**
     * Obtém a instância estática para uso em classes utilitárias.
     * @return a instância do UniqueMessageUtil
     */
    public static UniqueMessageUtil getInstance() {
        if (instance == null) {
            throw new IllegalStateException("UniqueMessageUtil não foi inicializado pelo Spring");
        }
        return instance;
    }
    
    /**
     * Método de conveniência para verificar mensagens em contexto estático.
     * @param message a mensagem a verificar
     * @param context o contexto da mensagem
     * @return true se a mensagem pode ser processada, false se for duplicada
     */
    public static boolean isUniqueMessage(String message, String context) {
        if (instance == null) {
            // Se não tivermos instância, permitimos a mensagem para evitar perda de informações
            return true;
        }
        return instance.canProcess(message, context);
    }
}