package com.ocoelhogabriel.manager_user_security.domain.services;

import com.ocoelhogabriel.manager_user_security.domain.entities.AuthModel;
import com.ocoelhogabriel.manager_user_security.interfaces.web.dtos.ResponseAuthDTO;
import com.ocoelhogabriel.manager_user_security.interfaces.web.dtos.TokenValidationResponseDTO;
import com.ocoelhogabriel.manager_user_security.interfaces.web.dtos.GenerateTokenRecords;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.io.IOException;
import java.time.Instant;

public interface AuthService extends UserDetailsService {
    
    GenerateTokenRecords getToken(AuthModel authToken) throws IOException;
    
    String validToken(String token);
    
    Instant validateTimeToken(String token);
    
    ResponseEntity<ResponseAuthDTO> refreshToken(String token);
    
    ResponseEntity<TokenValidationResponseDTO> validateAndParseToken(String token);
    
    ResponseEntity<ResponseAuthDTO> authLogin(AuthModel authRequest) throws AuthenticationException, IOException;
}