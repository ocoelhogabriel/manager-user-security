package com.ocoelhogabriel.manager_user_security.infrastructure.configuration;

import com.ocoelhogabriel.manager_user_security.application.service.AuthService;
import com.ocoelhogabriel.manager_user_security.application.service.EmpresaService;
import com.ocoelhogabriel.manager_user_security.application.service.PerfilService;
import com.ocoelhogabriel.manager_user_security.application.service.UserService;
import com.ocoelhogabriel.manager_user_security.domain.ports.in.AuthUseCase;
import com.ocoelhogabriel.manager_user_security.domain.ports.in.EmpresaUseCase;
import com.ocoelhogabriel.manager_user_security.domain.ports.in.PerfilUseCase;
import com.ocoelhogabriel.manager_user_security.domain.ports.in.UserUseCase;
import com.ocoelhogabriel.manager_user_security.domain.ports.out.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * Spring configuration class for defining application beans.
 * This class is responsible for wiring the application's components together,
 * such as mapping interfaces (ports) to their concrete implementations (adapters).
 */
@Configuration
public class BeanConfiguration {

    @Bean
    public UserUseCase userUseCase(
        final UserRepository userRepository,
        final PasswordHashingService passwordHashingService
    ) {
        return new UserService(userRepository, passwordHashingService);
    }

    @Bean
    public PerfilUseCase perfilUseCase(final PerfilRepository perfilRepository) {
        return new PerfilService(perfilRepository);
    }

    @Bean
    public EmpresaUseCase empresaUseCase(final EmpresaRepository empresaRepository) {
        return new EmpresaService(empresaRepository);
    }

    @Bean
    public AuthUseCase authUseCase(
        final AuthenticationManager authenticationManager,
        final UserRepository userRepository,
        final JwtService jwtService
    ) {
        return new AuthService(authenticationManager, userRepository, jwtService);
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
