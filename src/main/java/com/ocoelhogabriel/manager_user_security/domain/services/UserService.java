package com.ocoelhogabriel.manager_user_security.domain.services;

import com.ocoelhogabriel.manager_user_security.domain.entities.User;
import com.ocoelhogabriel.manager_user_security.domain.repositories.UserRepository;
import com.ocoelhogabriel.manager_user_security.domain.repositories.UserRepository.PageQuery;
import com.ocoelhogabriel.manager_user_security.domain.repositories.UserRepository.PagedResult;
import com.ocoelhogabriel.manager_user_security.domain.value_objects.*;
import java.time.LocalDateTime;
import java.util.Objects;

/**
 * Domain Service implementing the UserUseCase port.
 * This class orchestrates the business logic for user management,
 * acting as the core of the hexagonal architecture.
 */
public class UserService implements UserUseCase {

    private final UserRepository userRepository;
    private final PasswordHashingService passwordHashingService;

    public UserService(final UserRepository userRepository, final PasswordHashingService passwordHashingService) {
        this.userRepository = Objects.requireNonNull(userRepository);
        this.passwordHashingService = Objects.requireNonNull(passwordHashingService);
    }

    @Override
    public User createUser(final CreateUserCommand command) {
        final Name name = new Name(command.name());
        final CPF cpf = new CPF(command.cpf());
        final Username username = new Username(command.username());
        final Email email = new Email(command.email());

        // This check will require a new method in the UserRepository interface
        // if (userRepository.existsByUsername(username) || userRepository.existsByEmail(email) || userRepository.existsByCpf(cpf)) {
        //     throw new UserAlreadyExistsException("A user with the same username, email, or CPF already exists.");
        // }

        final HashedPassword hashedPassword = new HashedPassword(passwordHashingService.hash(command.rawPassword()));
        final LocalDateTime now = LocalDateTime.now();

        final User newUser = User.builder()
            .name(name)
            .cpf(cpf)
            .username(username)
            .email(email)
            .password(hashedPassword)
            .active(true)
            .createdAt(now)
            .updatedAt(now)
            .build();

        return userRepository.save(newUser);
    }

    @Override
    public User findById(final UserId id) {
        return userRepository.findById(id)
            .orElseThrow(() -> new UserNotFoundException("User not found with ID: " + id.value()));
    }

    @Override
    public PagedResult<User> findAllActive(final PageQuery query) {
        return userRepository.findAllActive(query);
    }

    @Override
    public User updateEmail(final UpdateEmailCommand command) {
        final User existingUser = findById(command.id());
        final User updatedUser = existingUser.updateEmail(command.newEmail());
        return userRepository.save(updatedUser);
    }

    @Override
    public User deactivateUser(final UserId id) {
        final User existingUser = findById(id);
        final User deactivatedUser = existingUser.deactivate();
        return userRepository.save(deactivatedUser);
    }
}
