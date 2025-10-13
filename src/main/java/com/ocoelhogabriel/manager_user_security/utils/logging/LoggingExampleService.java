package com.ocoelhogabriel.manager_user_security.utils.logging;

import org.slf4j.Logger;
import java.util.HashMap;
import java.util.Map;

/**
 * Classe de exemplo que demonstra como utilizar o sistema de logs padronizado.
 * Esta classe oferece exemplos práticos de uso do LogManager para diferentes cenários.
 */
public class LoggingExampleService {

    private static final Logger logger = LogManager.getLogger(LoggingExampleService.class);
    
    /**
     * Demonstra diferentes níveis e categorias de log em ações comuns.
     */
    public void demonstrateLogging() {
        // Log informativo simples
        LogManager.info(logger, LogCategory.SYSTEM, "Sistema inicializado com sucesso");
        
        // Log com metadados adicionais
        LogManager.info(logger, LogCategory.BUSINESS, "Processando solicitação de cliente", 
                "clientId", "12345",
                "requestType", "CONSULTA_SALDO");
        
        try {
            // Simular operação que pode falhar
            simulateOperation(true);
        } catch (Exception e) {
            // Log de erro com exceção
            LogManager.error(logger, LogCategory.SYSTEM, 
                    "Erro ao processar solicitação", e,
                    "operationId", "OP-789",
                    "severity", "HIGH");
        }
        
        // Log de performance
        LogManager.debug(logger, LogCategory.PERFORMANCE, 
                "Operação concluída", 
                "durationMs", 157,
                "resourceType", "CPU",
                "threadName", Thread.currentThread().getName());
        
        // Log de auditoria
        Map<String, Object> auditDetails = new HashMap<>();
        auditDetails.put("ipAddress", "192.168.1.100");
        auditDetails.put("deviceId", "MOBILE-123");
        auditDetails.put("loginType", "2FA");
        
        LogManager.audit(logger, 
                "USER_LOGIN", 
                "USER", 
                "john.doe@example.com", 
                "SUCCESS",
                auditDetails);
    }
    
    /**
     * Método que simula uma operação que pode falhar.
     * 
     * @param shouldFail determina se a operação deve falhar
     * @throws RuntimeException se shouldFail for true
     */
    private void simulateOperation(boolean shouldFail) {
        LogManager.debug(logger, LogCategory.BUSINESS, 
                "Iniciando operação de processamento", 
                "shouldFail", shouldFail);
                
        if (shouldFail) {
            LogManager.warn(logger, LogCategory.SYSTEM, 
                    "Condição de falha detectada", 
                    "errorCode", "INVALID_STATE");
                    
            throw new OperationDemoException("Falha simulada para demonstração de log");
        }
        
        LogManager.info(logger, LogCategory.BUSINESS, 
                "Operação concluída com sucesso");
    }
}