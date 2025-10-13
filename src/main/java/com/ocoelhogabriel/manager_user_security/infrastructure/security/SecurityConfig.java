package com.ocoelhogabriel.manager_user_security.infrastructure.security;

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
 * Security configuration class for the application.
 * Configures Spring Security with JWT authentication.
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
public class SecurityConfig {

    /**
     * List of URL patterns that don't require authentication.
     */
    private static final String[] PUBLIC_URLS = {
        "/api/auth/v1/**",
        "/api/health/**",
        "/api/device/v1/keep-alive/**",
        "/api/device/v1/auth-validate",
        "/api/device/v1/auth",
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
     * Configures the security filter chain.
     *
     * @param jwtAuthFilter the JWT authentication filter
     * @param http the HttpSecurity to configure
     * @return the configured SecurityFilterChain
     * @throws Exception if an error occurs during configuration
     */
    @Bean
    SecurityFilterChain filterChain(JwtAuthenticationFilter jwtAuthFilter, HttpSecurity http) throws Exception {
        http.csrf(csrf -> csrf.disable())
            // Request filter
            .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class)
            .authorizeHttpRequests(requests -> requests
                // Public URLs
                .requestMatchers(PUBLIC_URLS).permitAll()
                // All other requests require authentication
                .anyRequest().authenticated()
            )
            // Session management
            .sessionManagement(session -> session.sessionCreationPolicy(STATELESS))
            // Exception handling
            .exceptionHandling(ex -> ex
                // Access denied handler
                .accessDeniedHandler(new CustomAccessDeniedHandler())
                // Authentication entry point
                .authenticationEntryPoint(new CustomAuthenticationEntryPoint())
            );
        
        return http.build();
    }

    /**
     * Provides a password encoder bean.
     *
     * @return the password encoder
     */
    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /**
     * Provides an authentication manager bean.
     *
     * @param authenticationConfiguration the authentication configuration
     * @return the authentication manager
     * @throws Exception if an error occurs during creation
     */
    @Bean
    AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration)
            throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }
}
