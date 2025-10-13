package com.ocoelhogabriel.manager_user_security.infrastructure.security.config;

import static org.springframework.security.config.http.SessionCreationPolicy.STATELESS;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.ocoelhogabriel.manager_user_security.infrastructure.security.filter.JwtAuthenticationFilter;
import com.ocoelhogabriel.manager_user_security.infrastructure.security.handler.CustomAccessDeniedHandler;
import com.ocoelhogabriel.manager_user_security.infrastructure.security.handler.CustomAuthenticationEntryPoint;

import io.swagger.v3.oas.annotations.enums.SecuritySchemeIn;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.security.SecurityScheme;

/**
 * Configuration class for Spring Security
 */
@Configuration
@EnableWebSecurity
@SecurityScheme(
    name = "bearerAuth",
    description = "JWT Authentication",
    type = SecuritySchemeType.HTTP,
    scheme = "bearer",
    bearerFormat = "JWT",
    in = SecuritySchemeIn.HEADER
)
public class WebSecurityConfig {

    /**
     * List of URLs that don't require authentication
     */
    private static final String[] WHITE_LIST_URL = {
        "/api/medicao/v1/criarMedicao/**",
        "/api/medicao/v2",
        "/api/modulo-device/v1/keepAlive/**",
        "/api/modulo-device/v1/auth-validate",
        "/api/modulo-device/v1/auth",
        "/api/autenticacao/v1/**",
        "/v2/api-docs",
        "/v3/api-docs",
        "/v3/api-docs/**",
        "/swagger-resources",
        "/swagger-resources/**",
        "/configuration/ui",
        "/configuration/security",
        "/swagger-ui/**",
        "/webjars/**",
        "/swagger-ui.html",
        "/swagger-ui/index.html"
    };

    /**
     * Configure the security filter chain
     * 
     * @param jwtAuthFilter JWT authentication filter
     * @param http HTTP security configuration
     * @return The configured security filter chain
     * @throws Exception If an error occurs during configuration
     */
    @Bean
    SecurityFilterChain filterChain(JwtAuthenticationFilter jwtAuthFilter, HttpSecurity http) throws Exception {
        return http
            // Disable CSRF
            .csrf(csrf -> csrf.disable())
            // Add JWT authentication filter
            .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class)
            // Configure authorization
            .authorizeHttpRequests(requests -> requests
                // Allow access to whitelisted URLs
                .requestMatchers(WHITE_LIST_URL).permitAll()
                // Require authentication for all other requests
                .anyRequest().authenticated())
            // Configure session management
            .sessionManagement(session -> session.sessionCreationPolicy(STATELESS))
            // Configure exception handling
            .exceptionHandling(exHandling -> exHandling
                // Access denied handler
                .accessDeniedHandler(new CustomAccessDeniedHandler())
                // Authentication entry point
                .authenticationEntryPoint(new CustomAuthenticationEntryPoint()))
            .build();
    }

    /**
     * Configure password encoder
     * 
     * @return BCryptPasswordEncoder
     */
    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /**
     * Configure authentication manager
     * 
     * @param authConfig Authentication configuration
     * @return The configured authentication manager
     * @throws Exception If an error occurs during configuration
     */
    @Bean
    AuthenticationManager authenticationManager(AuthenticationConfiguration authConfig) throws Exception {
        return authConfig.getAuthenticationManager();
    }
}