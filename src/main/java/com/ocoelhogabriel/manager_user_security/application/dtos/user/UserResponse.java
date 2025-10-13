package com.ocoelhogabriel.manager_user_security.application.dtos.user;

import java.time.LocalDateTime;

/**
 * Response DTO para usuário
 * Aplica Clean Architecture - Application Layer DTO
 * Expõe dados seguros do usuário (sem senha)
 */
public record UserResponse(
    Long id,
    String username,
    String email,
    boolean active,
    LocalDateTime createdAt,
    LocalDateTime updatedAt
) {}
