package com.ocoelhogabriel.manager_user_security.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.userdetails.UserDetailsService;

import com.ocoelhogabriel.manager_user_security.application.use_cases.PermissaoHandler;
import com.ocoelhogabriel.manager_user_security.domain.services.AuthService;
import com.ocoelhogabriel.manager_user_security.exception.CustomAccessDeniedHandler;

/**
 * Configuração específica para o filtro JWT para evitar referências circulares
 * Esta classe isola a criação do JWTAuthFilter para resolver problemas de injeção cíclica
 */
@Configuration
public class JWTFilterConfig {

    @Bean
    public JWTAuthFilter jwtAuthFilter(
            AuthService authService,
            UserDetailsService userDetailsService,
            PermissaoHandler permissaoHandler,
            CustomAccessDeniedHandler accessDeniedHandler) {

        return new JWTAuthFilter(
            authService,
            userDetailsService,
            permissaoHandler,
            accessDeniedHandler
        );
    }
}
