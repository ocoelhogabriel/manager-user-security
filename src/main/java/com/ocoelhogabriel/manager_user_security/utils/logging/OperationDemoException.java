package com.ocoelhogabriel.manager_user_security.utils.logging;

/**
 * Exceção personalizada para o serviço de exemplo de logging.
 */
public class OperationDemoException extends RuntimeException {
    
    private static final long serialVersionUID = 1L;
    
    /**
     * Constrói uma nova exceção com a mensagem especificada.
     * 
     * @param message a mensagem de detalhe
     */
    public OperationDemoException(String message) {
        super(message);
    }
    
    /**
     * Constrói uma nova exceção com a mensagem e causa especificadas.
     * 
     * @param message a mensagem de detalhe
     * @param cause a causa da exceção
     */
    public OperationDemoException(String message, Throwable cause) {
        super(message, cause);
    }
}