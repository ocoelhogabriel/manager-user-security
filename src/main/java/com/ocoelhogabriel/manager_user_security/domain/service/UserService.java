package com.ocoelhogabriel.manager_user_security.domain.service;

import java.util.List;
import java.util.Optional;

import com.ocoelhogabriel.manager_user_security.domain.entity.User;
import com.ocoelhogabriel.manager_user_security.domain.entity.Role;

/**
 * Service interface for user management operations.
 * This interface defines the contract for managing users.
 */
public interface UserService {
    
    /**
     * Creates a new user.
     *
     * @param username the username of the user
     * @param email the email of the user
     * @param password the password of the user
     * @return the created user
     */
    User createUser(String username, String email, String password);
    
    /**
     * Finds a user by ID.
     *
     * @param id the ID of the user to find
     * @return an Optional containing the found user, or empty if not found
     */
    Optional<User> findById(Long id);
    
    /**
     * Gets a user by ID or throws an exception if not found.
     *
     * @param id the ID of the user to find
     * @return the user with the specified ID
     * @throws com.ocoelhogabriel.manager_user_security.domain.exception.ResourceNotFoundException if user not found
     */
    User getUserById(Long id);

    /**
     * Finds a user by username.
     *
     * @param username the username of the user to find
     * @return an Optional containing the found user, or empty if not found
     */
    Optional<User> findByUsername(String username);
    
    /**
     * Finds all users.
     *
     * @return a list of all users
     */
    List<User> findAll();
    
    /**
     * Updates a user's information.
     *
     * @param user the user to update
     * @return the updated user
     */
    User updateUser(User user);
    
    /**
     * Updates a user's password.
     *
     * @param user the user to update
     * @param newPassword the new password
     * @return the updated user
     */
    User updatePassword(User user, String newPassword);
    
    /**
     * Assigns a role to a user.
     *
     * @param user the user to assign the role to
     * @param role the role to assign
     * @return the updated user
     */
    User assignRole(User user, Role role);
    
    /**
     * Removes a role from a user.
     *
     * @param user the user to remove the role from
     * @param role the role to remove
     * @return the updated user
     */
    User removeRole(User user, Role role);
    
    /**
     * Deactivates a user.
     *
     * @param user the user to deactivate
     * @return the updated user
     */
    User deactivateUser(User user);
    
    /**
     * Activates a user.
     *
     * @param user the user to activate
     * @return the updated user
     */
    User activateUser(User user);
    
    /**
     * Deletes a user.
     *
     * @param id the ID of the user to delete
     */
    void deleteUser(Long id);

    Optional<User> findByIdWithRoles(Long userId);
}
