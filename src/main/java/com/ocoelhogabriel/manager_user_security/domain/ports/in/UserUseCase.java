package com.ocoelhogabriel.manager_user_security.domain.ports.in;

import com.ocoelhogabriel.manager_user_security.domain.model.User;
import com.ocoelhogabriel.manager_user_security.domain.model.value_objects.Email;
import com.ocoelhogabriel.manager_user_security.domain.model.value_objects.UserId;
import com.ocoelhogabriel.manager_user_security.domain.ports.out.UserRepository.PageQuery;
import com.ocoelhogabriel.manager_user_security.domain.ports.out.UserRepository.PagedResult;

/**
 * Input Port defining the use cases for user management.
 * This interface is the entry point to the domain's core logic,
 * completely decoupled from web or persistence layers.
 */
public interface UserUseCase {

    // --- Commands for mutations ---

    /**
     * Command to create a new user.
     * @param name The user's full name.
     * @param cpf The user's CPF (Brazilian individual taxpayer registry).
     * @param username The desired username.
     * @param email The user's email.
     * @param rawPassword The user's raw, unhashed password.
     * @param empresaId The ID of the company the user belongs to.
     * @param perfilId The ID of the profile assigned to the user.
     * @param abrangenciaId The ID of the scope assigned to the user.
     */
    record CreateUserCommand(String name, String cpf, String username, String email, String rawPassword, Long empresaId, Long perfilId, Long abrangenciaId) {}

    /**
     * Command to update a user's email.
     * @param id The ID of the user to update.
     * @param newEmail The new email address.
     */
    record UpdateEmailCommand(UserId id, Email newEmail) {}

    // --- Use Case Methods ---

    /**
     * Creates a new user.
     * @param command The command containing user data.
     * @return The newly created User entity.
     * @throws UserAlreadyExistsException if a user with the same username, email, or CPF already exists.
     */
    User createUser(CreateUserCommand command);

    /**
     * Finds a user by their ID.
     * @param id The user's ID.
     * @return The User entity.
     * @throws UserNotFoundException if no user is found with the given ID.
     */
    User findById(UserId id);

    /**
     * Finds all active users with pagination.
     * @param query The pagination query.
     * @return A paginated result of active users.
     */
    PagedResult<User> findAllActive(PageQuery query);

    /**
     * Updates a user's email address.
     * @param command The command containing the user ID and new email.
     * @return The updated User entity.
     * @throws UserNotFoundException if the user to update is not found.
     */
    User updateEmail(UpdateEmailCommand command);

    /**
     * Deactivates a user (soft delete).
     * @param id The ID of the user to deactivate.
     * @return The deactivated User entity.
     * @throws UserNotFoundException if the user to deactivate is not found.
     */
    User deactivateUser(UserId id);

    // --- Custom Domain Exceptions ---

    class UserNotFoundException extends RuntimeException {
        public UserNotFoundException(String message) {
            super(message);
        }
    }

    class UserAlreadyExistsException extends RuntimeException {
        public UserAlreadyExistsException(String message) {
            super(message);
        }
    }
}
