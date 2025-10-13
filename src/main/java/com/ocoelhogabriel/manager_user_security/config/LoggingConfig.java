package com.ocoelhogabriel.manager_user_security.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.filter.CommonsRequestLoggingFilter;

/**
 * Configuração do sistema de logging da aplicação.
 * Registra filtros e outros componentes necessários para o funcionamento do sistema de logs.
 */
@Configuration
public class LoggingConfig {
    
    /**
     * Configura o filtro CommonsRequestLoggingFilter do Spring para registrar detalhes das requisições HTTP.
     * Este é um filtro complementar ao nosso filtro personalizado.
     * 
     * @return o filtro configurado
     */
    @Bean
    public CommonsRequestLoggingFilter requestLoggingFilter() {
        CommonsRequestLoggingFilter filter = new CommonsRequestLoggingFilter();
        filter.setIncludeQueryString(true);
        filter.setIncludePayload(true);
        filter.setMaxPayloadLength(10000);
        filter.setIncludeHeaders(true);
        filter.setAfterMessagePrefix("REQUEST DATA: ");
        return filter;
    }
    
    // Removido o método loggingFilterRegistration para evitar o problema de dependência circular
}