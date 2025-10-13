package com.ocoelhogabriel.manager_user_security.domain.services;

/**
 * Interface para serviço de hash de senhas
 * Aplica Interface Segregation Principle (ISP)
 * Aplica Dependency Inversion Principle (DIP)
 */
public interface PasswordHashingService {
    
    /**
     * Gera hash da senha
     * 
     * @param rawPassword senha em texto plano
     * @return senha hash
     */
    String hash(String rawPassword);
    
    /**
     * Verifica se a senha corresponde ao hash
     * 
     * @param rawPassword senha em texto plano
     * @param hashedPassword senha hash
     * @return true se corresponder
     */
    boolean matches(String rawPassword, String hashedPassword);
}
