package com.ocoelhogabriel.manager_user_security.infrastructure.adapters.security;

import java.util.Objects;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.ocoelhogabriel.manager_user_security.domain.services.PasswordHashingService;

/**
 * Implementação do serviço de hash de senhas usando BCrypt
 * Aplica Single Responsibility Principle (SRP)
 * Aplica Dependency Inversion Principle (DIP)
 */
@Service
public final class BCryptPasswordHashingService implements PasswordHashingService {
    
    private final PasswordEncoder passwordEncoder;
    
    public BCryptPasswordHashingService() {
        this.passwordEncoder = new BCryptPasswordEncoder();
    }
    
    @Override
    public String hash(final String rawPassword) {
        this.validatePassword(rawPassword);
        return this.passwordEncoder.encode(rawPassword);
    }
    
    @Override
    public boolean matches(final String rawPassword, final String hashedPassword) {
        this.validatePassword(rawPassword);
        this.validateHashedPassword(hashedPassword);
        return this.passwordEncoder.matches(rawPassword, hashedPassword);
    }
    
    private void validatePassword(final String password) {
        if (Objects.isNull(password) || password.trim().isEmpty()) {
            throw new IllegalArgumentException("Password cannot be null or empty");
        }
    }
    
    private void validateHashedPassword(final String hashedPassword) {
        if (Objects.isNull(hashedPassword) || hashedPassword.trim().isEmpty()) {
            throw new IllegalArgumentException("Hashed password cannot be null or empty");
        }
    }
}
