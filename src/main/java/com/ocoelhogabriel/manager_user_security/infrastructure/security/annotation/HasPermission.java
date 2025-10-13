package com.ocoelhogabriel.manager_user_security.infrastructure.security.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.springframework.security.access.prepost.PreAuthorize;

/**
 * Custom annotation for permission-based authorization
 */
@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@PreAuthorize("hasPermission(#resource, #permission)")
public @interface HasPermission {
    
    /**
     * The resource to check permission for
     * @return The resource name
     */
    String resource();
    
    /**
     * The permission to check
     * @return The permission name
     */
    String permission();
}