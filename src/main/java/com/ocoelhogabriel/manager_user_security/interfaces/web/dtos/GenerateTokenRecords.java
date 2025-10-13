package com.ocoelhogabriel.manager_user_security.interfaces.web.dtos;

import java.time.Instant;

/**
 * Record para representar dados de geração de token
 * Aplica Object Calisthenics - Records são imutáveis por natureza
 */
public record GenerateTokenRecords(String username, String token, String date, String expiryIn) {

    // Construtor adicional para compatibilidade com Instant
    public GenerateTokenRecords(String token, Instant expirationTime) {
        this(null, token, Instant.now().toString(), expirationTime.toString());
    }
    
    // Construtor completo
    public GenerateTokenRecords(String username, String token, Instant createdAt, Instant expiryIn) {
        this(username, token, createdAt.toString(), expiryIn.toString());
    }
}
