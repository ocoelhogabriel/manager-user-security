package com.ocoelhogabriel.manager_user_security.infrastructure.adapters.security;

/**
 * Interface para serviço de hash de senhas
 * Aplica Clean Architecture - porta para infraestrutura
 * Permite diferentes implementações de hash
 */
public interface PasswordHashingService {
    
    /**
     * Gera hash de uma senha em texto plano
     * @param plainPassword senha em texto plano
     * @return hash da senha
     */
    String hash(String plainPassword);
    
    /**
     * Verifica se uma senha em texto plano corresponde ao hash
     * @param plainPassword senha em texto plano
     * @param hashedPassword hash da senha
     * @return true se a senha corresponde ao hash
     */
    boolean verify(String plainPassword, String hashedPassword);
}
