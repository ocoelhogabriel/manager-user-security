package com.ocoelhogabriel.manager_user_security.application.dtos;

import java.time.LocalDateTime;

/**
 * DTO para resposta de usuário
 * Aplica Object Calisthenics - Records são imutáveis por natureza
 */
public record UserResponse(
        Long id,
        String username,
        String email,
        Boolean active,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {
    public UserResponse {
        if (username == null || username.trim().isEmpty()) {
            throw new IllegalArgumentException("Username cannot be null or empty");
        }
        if (email == null || email.trim().isEmpty()) {
            throw new IllegalArgumentException("Email cannot be null or empty");
        }
    }
}
