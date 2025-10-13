package com.ocoelhogabriel.manager_user_security.domain.ports.out;

import com.ocoelhogabriel.manager_user_security.domain.model.User;
import com.ocoelhogabriel.manager_user_security.domain.model.value_objects.CPF;
import com.ocoelhogabriel.manager_user_security.domain.model.value_objects.Email;
import com.ocoelhogabriel.manager_user_security.domain.model.value_objects.UserId;
import com.ocoelhogabriel.manager_user_security.domain.model.value_objects.Username;
import java.util.List;
import java.util.Optional;

/**
 * Repository interface (Driven Port) for the User entity.
 * Follows the Dependency Inversion Principle (DIP), defining contracts that the infrastructure layer must implement.
 */
public interface UserRepository {

    /**
     * Represents a pagination query.
     * @param page The page number (0-indexed).
     * @param size The number of items per page.
     */
    record PageQuery(int page, int size) {}

    /**
     * Represents a paginated result set.
     * @param items The list of items on the current page.
     * @param total The total number of items across all pages.
     * @param <T> The type of the items.
     */
    record PagedResult<T>(List<T> items, long total) {}

    /**
     * Saves (creates or updates) a user.
     * @param user The user entity to save.
     * @return The saved user entity, potentially with updated state (e.g., generated ID).
     */
    User save(User user);

    /**
     * Finds a user by their unique ID.
     * @param id The user ID.
     * @return An Optional containing the user if found, otherwise empty.
     */
    Optional<User> findById(UserId id);

    /**
     * Finds a user by their username.
     * @param username The username.
     * @return An Optional containing the user if found, otherwise empty.
     */
    Optional<User> findByUsername(Username username);

    /**
     * Finds a user by their email address.
     * @param email The email address.
     * @return An Optional containing the user if found, otherwise empty.
     */
    Optional<User> findByEmail(Email email);

    /**
     * Finds all active users, with pagination.
     * @param query The pagination query.
     * @return A paginated result of active users.
     */
    PagedResult<User> findAllActive(PageQuery query);

    /**
     * Checks if a user exists with the given username.
     * @param username The username to check.
     * @return true if a user with the username exists, false otherwise.
     */
    boolean existsByUsername(Username username);

    /**
     * Checks if a user exists with the given email.
     * @param email The email to check.
     * @return true if a user with the email exists, false otherwise.
     */
    boolean existsByEmail(Email email);

    /**
     * Checks if a user exists with the given CPF.
     * @param cpf The CPF to check.
     * @return true if a user with the CPF exists, false otherwise.
     */
    boolean existsByCpf(CPF cpf);

    /**
     * Counts the total number of active users.
     * @return The count of active users.
     */
    long countActive();
}
