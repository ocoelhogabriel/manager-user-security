package com.ocoelhogabriel.usersecurity.infrastructure.security.config;

import org.aopalliance.intercept.MethodInvocation;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.access.expression.method.DefaultMethodSecurityExpressionHandler;
import org.springframework.security.access.expression.method.MethodSecurityExpressionHandler;
import org.springframework.security.access.expression.method.MethodSecurityExpressionOperations;
import org.springframework.security.authentication.AuthenticationTrustResolver;
import org.springframework.security.authentication.AuthenticationTrustResolverImpl;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.core.Authentication;

import com.ocoelhogabriel.usersecurity.infrastructure.security.permission.CustomPermissionEvaluator;
import com.ocoelhogabriel.usersecurity.infrastructure.security.permission.CustomSecurityExpression;

/**
 * Configuration for method security.
 * This class configures the method security expression handler to use our custom permission evaluator.
 */
@Configuration
@EnableMethodSecurity
public class MethodSecurityConfig {

    private final CustomPermissionEvaluator permissionEvaluator;

    /**
     * Constructor for MethodSecurityConfig.
     * 
     * @param permissionEvaluator the permission evaluator
     */
    public MethodSecurityConfig(CustomPermissionEvaluator permissionEvaluator) {
        this.permissionEvaluator = permissionEvaluator;
    }

    /**
     * Creates a method security expression handler.
     * 
     * @return the method security expression handler
     */
    @Bean
    public MethodSecurityExpressionHandler methodSecurityExpressionHandler() {
        DefaultMethodSecurityExpressionHandler expressionHandler = new CustomMethodSecurityExpressionHandler();
        expressionHandler.setPermissionEvaluator(permissionEvaluator);
        expressionHandler.setTrustResolver(trustResolver());
        return expressionHandler;
    }
    
    /**
     * Creates an authentication trust resolver.
     * 
     * @return the authentication trust resolver
     */
    @Bean
    public AuthenticationTrustResolver trustResolver() {
        return new AuthenticationTrustResolverImpl();
    }
    
    /**
     * Custom method security expression handler that uses our CustomSecurityExpression.
     */
    private static class CustomMethodSecurityExpressionHandler extends DefaultMethodSecurityExpressionHandler {
        @Override
        protected MethodSecurityExpressionOperations createSecurityExpressionRoot(Authentication authentication, MethodInvocation invocation) {
            CustomSecurityExpression root = new CustomSecurityExpression(authentication);
            root.setPermissionEvaluator(getPermissionEvaluator());
            root.setTrustResolver(getTrustResolver());
            root.setRoleHierarchy(getRoleHierarchy());
            root.setThis(invocation.getThis());
            return root;
        }
    }
}