package com.ocoelhogabriel.manager_user_security.infrastructure.adapters.services;

import java.io.IOException;
import java.time.Instant;
import java.util.Objects;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.ocoelhogabriel.manager_user_security.domain.constraints.MessageConstraints;
import com.ocoelhogabriel.manager_user_security.domain.constraints.MessageFormatterUtil;
import com.ocoelhogabriel.manager_user_security.domain.entities.AuthModel;
import com.ocoelhogabriel.manager_user_security.domain.services.AuthService;
import com.ocoelhogabriel.manager_user_security.domain.services.AuthenticationStrategy;
import com.ocoelhogabriel.manager_user_security.interfaces.web.dtos.GenerateTokenRecords;
import com.ocoelhogabriel.manager_user_security.interfaces.web.dtos.ResponseAuthDTO;
import com.ocoelhogabriel.manager_user_security.interfaces.web.dtos.TokenValidationResponseDTO;

/**
 * Implementação do serviço de autenticação
 * Aplica Strategy Pattern para diferentes tipos de autenticação
 * Segue SOLID principles
 */
@Service
public class AuthServiceImpl
        implements AuthService, com.ocoelhogabriel.manager_user_security.domain.services.AuthServInterface {

    private static final Logger LOGGER = LoggerFactory.getLogger(AuthServiceImpl.class);

    private final AuthenticationStrategy authenticationStrategy;

    public AuthServiceImpl(final AuthenticationStrategy authenticationStrategy) {
        this.authenticationStrategy = Objects.requireNonNull(authenticationStrategy, "AuthenticationStrategy cannot be null");
    }

    @Override
    public GenerateTokenRecords getToken(final AuthModel authToken) throws IOException {
        this.validateAuthModel(authToken);

        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug(MessageFormatterUtil.format(MessageConstraints.LOG_OPERATION_START, "token generation"));
        }

        final String token = this.authenticationStrategy.authenticate(authToken.getLogin());
        final Instant expirationTime = this.validateTimeToken(token);

        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug(MessageFormatterUtil.format(MessageConstraints.LOG_OPERATION_END, "token generation"));
        }

        return new GenerateTokenRecords(token, expirationTime);
    }

    @Override
    public String validToken(final String token) {
        if (Objects.isNull(token) || token.trim().isEmpty()) {
            if (LOGGER.isErrorEnabled()) {
                LOGGER.error(MessageConstraints.AUTH_TOKEN_NULL);
            }
            throw new IllegalArgumentException(MessageConstraints.AUTH_TOKEN_NULL);
        }

        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug(MessageConstraints.LOG_AUTH_TOKEN_VALIDATE);
        }

        return this.authenticationStrategy.extractUserInfo(token);
    }

    @Override
    public Instant validateTimeToken(final String token) {
        // Implementation would depend on JWT library to extract expiration time
        // For now, returning current time plus 24 hours as default
        return Instant.now().plusSeconds(24L * 60L * 60L);
    }

    @Override
    public ResponseEntity<ResponseAuthDTO> refreshToken(final String token) {
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug(MessageConstraints.LOG_AUTH_TOKEN_REFRESH);
        }

        try {
            final String username = this.validToken(token);
            final String newToken = this.authenticationStrategy.authenticate(username);

            final ResponseAuthDTO response = new ResponseAuthDTO(newToken, MessageConstraints.AUTH_REFRESH_SUCCESS);
            return ResponseEntity.ok(response);
        } catch (final Exception e) {
            if (LOGGER.isErrorEnabled()) {
                LOGGER.error(MessageFormatterUtil.format(MessageConstraints.AUTH_REFRESH_FAILED, e.getMessage()), e);
            }
            final ResponseAuthDTO response = new ResponseAuthDTO(null,
                    MessageFormatterUtil.format(MessageConstraints.AUTH_REFRESH_FAILED, e.getMessage()));
            return ResponseEntity.badRequest().body(response);
        }
    }

    @Override
    public ResponseEntity<TokenValidationResponseDTO> validateAndParseToken(final String token) {
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug(MessageConstraints.LOG_AUTH_TOKEN_VALIDATE);
        }

        try {
            final boolean isValid = this.authenticationStrategy.validateToken(token);
            if (isValid) {
                final TokenValidationResponseDTO response = new TokenValidationResponseDTO(
                        true,
                        System.currentTimeMillis(),
                        MessageConstraints.AUTH_VALIDATION_SUCCESS);
                return ResponseEntity.ok(response);
            } else {
                if (LOGGER.isWarnEnabled()) {
                    LOGGER.warn(MessageConstraints.AUTH_INVALID_TOKEN);
                }
                final TokenValidationResponseDTO response = new TokenValidationResponseDTO(
                        false,
                        0L,
                        MessageConstraints.AUTH_VALIDATION_FAILED);
                return ResponseEntity.badRequest().body(response);
            }
        } catch (final Exception e) {
            if (LOGGER.isErrorEnabled()) {
                LOGGER.error(MessageFormatterUtil.format(MessageConstraints.AUTH_VALIDATION_ERROR, e.getMessage()), e);
            }
            final TokenValidationResponseDTO response = new TokenValidationResponseDTO(
                    false,
                    0L,
                    MessageFormatterUtil.format(MessageConstraints.AUTH_VALIDATION_ERROR, e.getMessage()));
            return ResponseEntity.badRequest().body(response);
        }
    }

    @Override
    public ResponseEntity<ResponseAuthDTO> authLogin(final AuthModel authReq)
            throws AuthenticationException, IOException {
        try {
            this.validateAuthModel(authReq);

            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug(MessageFormatterUtil.format(MessageConstraints.LOG_AUTH_LOGIN, authReq.getLogin()));
            }

            // Usar AuthenticationStrategy para validar as credenciais
            if (this.authenticationStrategy.validateCredentials(authReq.getLogin(), authReq.getSenha())) {
                final GenerateTokenRecords tokenRecord = this.getToken(authReq);
                final ResponseAuthDTO response = new ResponseAuthDTO(tokenRecord.token(),
                        MessageConstraints.AUTH_SUCCESS);
                return ResponseEntity.ok(response);
            } else {
                if (LOGGER.isWarnEnabled()) {
                    LOGGER.warn(MessageConstraints.AUTH_FAILED);
                }
                final ResponseAuthDTO response = new ResponseAuthDTO(null, MessageConstraints.AUTH_FAILED);
                return ResponseEntity.badRequest().body(response);
            }
        } catch (final AuthenticationException e) {
            if (LOGGER.isErrorEnabled()) {
                LOGGER.error(MessageFormatterUtil.format(MessageConstraints.LOG_AUTH_ERROR, e.getMessage()), e);
            }
            final ResponseAuthDTO response = new ResponseAuthDTO(
                    null,
                    MessageFormatterUtil.format(MessageConstraints.AUTH_FAILED + " " + e.getMessage()));
            return ResponseEntity.badRequest().body(response);
        }
    }

    @Override
    public UserDetails loadUserByUsername(final String username) throws UsernameNotFoundException {
        // This would typically load user from database
        // For now, throwing an exception with a standard message
        if (LOGGER.isErrorEnabled()) {
            LOGGER.error(MessageFormatterUtil.format(MessageConstraints.AUTH_USER_NOT_FOUND, username));
        }
        throw new UsernameNotFoundException(
                MessageFormatterUtil.format(MessageConstraints.AUTH_USER_NOT_FOUND, username));
    }

    private void validateAuthModel(final AuthModel authModel) {
        if (Objects.isNull(authModel)) {
            if (LOGGER.isErrorEnabled()) {
                LOGGER.error(MessageConstraints.AUTH_LOGIN_NULL);
            }
            throw new IllegalArgumentException(MessageConstraints.AUTH_LOGIN_NULL);
        }
        if (Objects.isNull(authModel.getLogin()) || authModel.getLogin().trim().isEmpty()) {
            if (LOGGER.isErrorEnabled()) {
                LOGGER.error(MessageConstraints.AUTH_USERNAME_NULL);
            }
            throw new IllegalArgumentException(MessageConstraints.AUTH_USERNAME_NULL);
        }
        if (Objects.isNull(authModel.getSenha()) || authModel.getSenha().trim().isEmpty()) {
            if (LOGGER.isErrorEnabled()) {
                LOGGER.error(MessageConstraints.AUTH_PASSWORD_NULL);
            }
            throw new IllegalArgumentException(MessageConstraints.AUTH_PASSWORD_NULL);
        }
    }
}
