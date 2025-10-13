package com.ocoelhogabriel.manager_user_security.domain.ports.in;

/**
 * Input Port for authentication use cases.
 */
public interface AuthUseCase {

    /**
     * Command to perform a login.
     * @param username The user's username.
     * @param password The user's raw password.
     */
    record LoginCommand(String username, String password) {}

    /**
     * Result of a successful login.
     * @param token The generated JWT.
     */
    record LoginResult(String token) {}

    /**
     * Authenticates a user and returns a JWT.
     *
     * @param command The login command containing credentials.
     * @return The result of a successful login.
     * @throws InvalidCredentialsException if the authentication fails.
     */
    LoginResult login(LoginCommand command);

    /**
     * Custom exception for authentication failures.
     */
    class InvalidCredentialsException extends RuntimeException {
        public InvalidCredentialsException(String message) {
            super(message);
        }
    }
}
