package com.ocoelhogabriel.manager_user_security.config;

import org.springframework.boot.actuate.autoconfigure.security.servlet.EndpointRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

/**
 * Configuração específica para endpoints de monitoramento (Actuator)
 * Esta configuração é separada da principal para não interferir nas regras de segurança da API
 */
@Configuration
public class ActuatorSecurityConfig {

    @Bean
    @Order(1) // Ordem maior tem precedência sobre a configuração principal de segurança
    public SecurityFilterChain actuatorSecurityFilterChain(HttpSecurity http) throws Exception {
        http.securityMatcher(EndpointRequest.toAnyEndpoint())
            .authorizeHttpRequests(requests ->
                requests
                    // Endpoint de health pode ser acessado sem autenticação (para Kubernetes/Docker)
                    .requestMatchers(AntPathRequestMatcher.antMatcher("/actuator/health/**")).permitAll()
                    .requestMatchers(AntPathRequestMatcher.antMatcher("/actuator/info")).permitAll()
                    // Outros endpoints do actuator requerem autenticação
                    .anyRequest().hasRole("ADMIN"))
            .csrf(csrf -> csrf.disable()); // CSRF desativado para simplificar

        return http.build();
    }
}
