package com.ocoelhogabriel.manager_user_security.handler;

import com.ocoelhogabriel.manager_user_security.domain.services.UserUseCase.UserAlreadyExistsException;
import com.ocoelhogabriel.manager_user_security.domain.services.UserUseCase.UserNotFoundException;
import java.time.Instant;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

/**
 * Global exception handler for the REST API.
 * This class intercepts exceptions thrown by controllers and translates them into
 * standardized HTTP error responses.
 */
@ControllerAdvice
public class RestExceptionHandler extends ResponseEntityExceptionHandler {

    /**
     * DTO for a standardized error response (following RFC 7807 Problem Details structure).
     */
    private record ErrorResponse(int status, String error, String message, Instant timestamp) {}

    /**
     * Handles UserNotFoundException, returning a 404 Not Found response.
     *
     * @param ex The caught UserNotFoundException.
     * @return A ResponseEntity containing the standardized error details.
     */
    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleUserNotFound(final UserNotFoundException ex) {
        final ErrorResponse errorResponse = new ErrorResponse(
            HttpStatus.NOT_FOUND.value(),
            HttpStatus.NOT_FOUND.getReasonPhrase(),
            ex.getMessage(),
            Instant.now()
        );
        return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
    }

    /**
     * Handles UserAlreadyExistsException, returning a 409 Conflict response.
     *
     * @param ex The caught UserAlreadyExistsException.
     * @return A ResponseEntity containing the standardized error details.
     */
    @ExceptionHandler(UserAlreadyExistsException.class)
    public ResponseEntity<ErrorResponse> handleUserAlreadyExists(final UserAlreadyExistsException ex) {
        final ErrorResponse errorResponse = new ErrorResponse(
            HttpStatus.CONFLICT.value(),
            HttpStatus.CONFLICT.getReasonPhrase(),
            ex.getMessage(),
            Instant.now()
        );
        return new ResponseEntity<>(errorResponse, HttpStatus.CONFLICT);
    }

    /**
     * Handles generic IllegalArgumentException, returning a 400 Bad Request response.
     * This is useful for validation errors from Value Objects.
     *
     * @param ex The caught IllegalArgumentException.
     * @return A ResponseEntity containing the standardized error details.
     */
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ErrorResponse> handleIllegalArgument(final IllegalArgumentException ex) {
        final ErrorResponse errorResponse = new ErrorResponse(
            HttpStatus.BAD_REQUEST.value(),
            HttpStatus.BAD_REQUEST.getReasonPhrase(),
            ex.getMessage(),
            Instant.now()
        );
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }
}
