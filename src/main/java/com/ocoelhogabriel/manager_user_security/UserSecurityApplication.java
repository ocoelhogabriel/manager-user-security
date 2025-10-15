package com.ocoelhogabriel.manager_user_security;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

/**
 * Main application class for the User Security system.
 * This application provides authentication, authorization and user management functionality.
 */
@SpringBootApplication
@EnableJpaRepositories(basePackages = {"com.ocoelhogabriel.manager_user_security.infrastructure.persistence.repository"})
public class UserSecurityApplication extends SpringBootServletInitializer {

    /**
     * Main method to start the application.
     *
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        SpringApplication.run(UserSecurityApplication.class, args);
    }
    
    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
        return application.sources(UserSecurityApplication.class);
    }
}


