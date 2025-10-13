package com.ocoelhogabriel.manager_user_security.infrastructure.security.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.access.expression.method.DefaultMethodSecurityExpressionHandler;
import org.springframework.security.access.expression.method.MethodSecurityExpressionHandler;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;

import com.ocoelhogabriel.manager_user_security.infrastructure.security.authorization.CustomPermissionEvaluator;

/**
 * Configuration class for method-level security
 */
@Configuration
@EnableMethodSecurity
public class MethodSecurityConfig {

    @Autowired
    private CustomPermissionEvaluator permissionEvaluator;
    
    /**
     * Configure method security expression handler
     * 
     * @return The configured method security expression handler
     */
    protected MethodSecurityExpressionHandler createExpressionHandler() {
        DefaultMethodSecurityExpressionHandler expressionHandler = 
                new DefaultMethodSecurityExpressionHandler();
        expressionHandler.setPermissionEvaluator(permissionEvaluator);
        return expressionHandler;
    }
}