package com.ocoelhogabriel.manager_user_security.domain.service;

import com.ocoelhogabriel.manager_user_security.interfaces.dto.AuthenticationResponse;
import com.ocoelhogabriel.manager_user_security.interfaces.dto.TokenValidationResponse;

public interface AuthenticationService {

    AuthenticationResponse authenticate(String username, String password);

    TokenValidationResponse validateToken(String token);

    AuthenticationResponse refreshToken(String token);

    Object getCurrentUser();
    
    void invalidateToken(String token);
}
