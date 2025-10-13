package com.ocoelhogabriel.manager_user_security.services;

import java.io.IOException;
import java.time.Instant;

import org.springframework.http.ResponseEntity;
import org.springframework.lang.NonNull;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetailsService;

import com.ocoelhogabriel.manager_user_security.model.AuthModel;
import com.ocoelhogabriel.manager_user_security.model.dto.ResponseAuthDTO;
import com.ocoelhogabriel.manager_user_security.model.dto.TokenValidationResponseDTO;
import com.ocoelhogabriel.manager_user_security.records.GenerateTokenRecords;

public interface AuthServInterface extends UserDetailsService {
	public GenerateTokenRecords getToken(AuthModel authToken) throws IOException;

	public String validToken(String token);

	public Instant validateTimeToken(String token);

	ResponseEntity<ResponseAuthDTO> refreshToken(String token);

	ResponseEntity<TokenValidationResponseDTO> validateAndParseToken(@NonNull String token);

	ResponseEntity<ResponseAuthDTO> authLogin(@NonNull AuthModel authReq) throws AuthenticationException, IOException;
}
