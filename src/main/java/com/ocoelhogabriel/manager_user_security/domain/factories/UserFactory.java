package com.ocoelhogabriel.manager_user_security.domain.factories;

import java.time.LocalDateTime;

import com.ocoelhogabriel.manager_user_security.domain.entities.User;
import com.ocoelhogabriel.manager_user_security.domain.value_objects.Email;
import com.ocoelhogabriel.manager_user_security.domain.value_objects.HashedPassword;
import com.ocoelhogabriel.manager_user_security.domain.value_objects.UserId;
import com.ocoelhogabriel.manager_user_security.domain.value_objects.Username;

/**
 * Abstract Factory para criação de usuários
 * Aplica Abstract Factory Pattern e Single Responsibility Principle
 * Centraliza a lógica de criação de diferentes tipos de usuários
 */
public abstract class UserFactory {
    
    /**
     * Cria um novo usuário regular
     */
    public static User createRegularUser(final Username username, 
                                       final Email email, 
                                       final HashedPassword password) {
        return User.create(username, email, password);
    }
    
    /**
     * Cria um usuário administrador
     */
    public static User createAdminUser(final Username username, 
                                     final Email email, 
                                     final HashedPassword password) {
        // Lógica específica para usuários admin pode ser adicionada aqui
        return User.create(username, email, password);
    }
    
    /**
     * Restaura um usuário existente a partir do banco de dados
     */
    public static User restoreUser(final UserId id,
                                 final Username username,
                                 final Email email,
                                 final HashedPassword password,
                                 final boolean active,
                                 final LocalDateTime createdAt,
                                 final LocalDateTime updatedAt) {
        return User.restore(id, username, email, password, active, createdAt, updatedAt);
    }
    
    /**
     * Cria um usuário de sistema/serviço
     */
    public static User createSystemUser(final Username username, 
                                      final Email email, 
                                      final HashedPassword password) {
        // Lógica específica para usuários de sistema
        return User.create(username, email, password);
    }
    
    /**
     * Factory method baseado em tipo de usuário
     */
    public static User createUserByType(final UserType userType,
                                      final Username username,
                                      final Email email,
                                      final HashedPassword password) {
        return switch (userType) {
            case REGULAR -> createRegularUser(username, email, password);
            case ADMIN -> createAdminUser(username, email, password);
            case SYSTEM -> createSystemUser(username, email, password);
        };
    }
    
    public enum UserType {
        REGULAR, ADMIN, SYSTEM
    }
}
