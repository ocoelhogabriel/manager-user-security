package com.ocoelhogabriel.manager_user_security.infrastructure.persistence.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * Configuration class for the persistence layer.
 * Enables JPA repositories, auditing, and transaction management.
 */
@Configuration
@EnableJpaRepositories(basePackages = "com.ocoelhogabriel.usersecurity.infrastructure.persistence.repository")
@EnableJpaAuditing(auditorAwareRef = "auditorAware")
@EnableTransactionManagement
public class PersistenceConfig {
    
    /**
     * Creates an AuditorAware bean for JPA auditing.
     * 
     * @return the AuditorAware implementation
     */
    @Bean
    public AuditorAware<String> auditorAware() {
        return new AuditorAwareImpl();
    }
}
