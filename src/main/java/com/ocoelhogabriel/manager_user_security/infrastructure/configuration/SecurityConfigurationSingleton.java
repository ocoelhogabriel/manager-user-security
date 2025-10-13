package com.ocoelhogabriel.manager_user_security.infrastructure.configuration;

import java.util.List;
import java.util.Objects;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * Singleton para configurações de segurança
 * Aplica Singleton Pattern e Object Calisthenics
 * Garante que existe apenas uma instância das configurações de segurança
 */
@Component
public final class SecurityConfigurationSingleton {
    
    private static SecurityConfigurationSingleton instance;
    private static final Object LOCK = new Object();
    
    private final String jwtSecret;
    private final long jwtExpirationMinutes;
    private final List<String> whiteListUrls;
    
    public SecurityConfigurationSingleton(
            @Value("${api.security.token.secret}") final String jwtSecret,
            @Value("${api.security.expiration.time.minutes:1440}") final long jwtExpirationMinutes) {
        this.jwtSecret = jwtSecret;
        this.jwtExpirationMinutes = jwtExpirationMinutes;
        this.whiteListUrls = this.initializeWhiteListUrls();
        
        synchronized (LOCK) {
            if (Objects.isNull(instance)) {
                instance = this;
            }
        }
    }
    
    public static SecurityConfigurationSingleton getInstance() {
        if (Objects.isNull(instance)) {
            synchronized (LOCK) {
                if (Objects.isNull(instance)) {
                    throw new IllegalStateException("SecurityConfigurationSingleton not initialized");
                }
            }
        }
        return instance;
    }
    
    public String getJwtSecret() {
        return this.jwtSecret;
    }
    
    public long getJwtExpirationMinutes() {
        return this.jwtExpirationMinutes;
    }
    
    public List<String> getWhiteListUrls() {
        return List.copyOf(this.whiteListUrls);
    }
    
    public boolean isUrlWhitelisted(final String url) {
        return this.whiteListUrls.stream()
                .anyMatch(whitelistedUrl -> this.matchesPattern(url, whitelistedUrl));
    }
    
    private List<String> initializeWhiteListUrls() {
        return List.of(
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
        );
    }
    
    private boolean matchesPattern(final String url, final String pattern) {
        if (pattern.endsWith("/**")) {
            final String basePattern = pattern.substring(0, pattern.length() - 3);
            return url.startsWith(basePattern);
        }
        return url.equals(pattern);
    }
}
