package com.ocoelhogabriel.manager_user_security.domain.services;

import com.ocoelhogabriel.manager_user_security.domain.entities.AuthModel;
import com.ocoelhogabriel.manager_user_security.interfaces.web.dtos.ResponseAuthDTO;
import com.ocoelhogabriel.manager_user_security.interfaces.web.dtos.TokenValidationResponseDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.AuthenticationException;

import java.io.IOException;

public interface AuthServInterface {
    
    ResponseEntity<ResponseAuthDTO> authLogin(AuthModel auth) throws AuthenticationException, IOException;
    
    ResponseEntity<TokenValidationResponseDTO> validateAndParseToken(String token);
    
    ResponseEntity<ResponseAuthDTO> refreshToken(String token);
}