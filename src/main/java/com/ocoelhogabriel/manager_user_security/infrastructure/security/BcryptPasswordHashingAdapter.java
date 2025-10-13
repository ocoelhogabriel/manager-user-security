package com.ocoelhogabriel.manager_user_security.infrastructure.security;

import com.ocoelhogabriel.manager_user_security.domain.ports.out.PasswordHashingService;
import java.util.Objects;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

/**
 * Driven Adapter that implements the PasswordHashingService port.
 * This class adapts the domain's hashing contract to the Spring Security implementation.
 */
@Service
public class BcryptPasswordHashingAdapter implements PasswordHashingService {

    private final PasswordEncoder passwordEncoder;

    public BcryptPasswordHashingAdapter(final PasswordEncoder passwordEncoder) {
        this.passwordEncoder = Objects.requireNonNull(passwordEncoder);
    }

    @Override
    public String hash(final String rawPassword) {
        return passwordEncoder.encode(rawPassword);
    }

    @Override
    public boolean matches(final String rawPassword, final String hashedPassword) {
        return passwordEncoder.matches(rawPassword, hashedPassword);
    }
}
