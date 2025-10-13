package com.ocoelhogabriel.manager_user_security.application.dtos;

/**
 * DTO para requisição de criação de usuário
 * Aplica Object Calisthenics - Records são imutáveis por natureza
 */
public record CreateUserRequest(
        String username,
        String email,
        String password
) {
    public CreateUserRequest {
        if (username == null || username.trim().isEmpty()) {
            throw new IllegalArgumentException("Username cannot be null or empty");
        }
        if (email == null || email.trim().isEmpty()) {
            throw new IllegalArgumentException("Email cannot be null or empty");
        }
        if (password == null || password.trim().isEmpty()) {
            throw new IllegalArgumentException("Password cannot be null or empty");
        }
    }
}
