package com.ocoelhogabriel.manager_user_security.infrastructure.web;

import com.ocoelhogabriel.manager_user_security.domain.model.User;
import com.ocoelhogabriel.manager_user_security.domain.model.value_objects.Email;
import com.ocoelhogabriel.manager_user_security.domain.model.value_objects.UserId;
import com.ocoelhogabriel.manager_user_security.domain.ports.in.UserUseCase;
import com.ocoelhogabriel.manager_user_security.domain.ports.out.UserRepository.PageQuery;
import com.ocoelhogabriel.manager_user_security.domain.ports.out.UserRepository.PagedResult;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Driving Adapter (Controller) for user management.
 * This class adapts incoming HTTP requests to calls on the UserUseCase input port.
 */
@RestController
@RequestMapping("/api/v1/users")
@Tag(name = "Users", description = "API for User Management")
public class UsuarioController {

    private final UserUseCase userUseCase;

    public UsuarioController(final UserUseCase userUseCase) {
        this.userUseCase = Objects.requireNonNull(userUseCase);
    }

    // --- DTOs for the API Layer ---
    public record CreateUserRequest(String name, String cpf, String username, String email, String password, Long empresaId, Long perfilId, Long abrangenciaId) {}
    public record UpdateEmailRequest(String newEmail) {}
    public record UserResponse(Long id, String name, String cpf, String username, String email, Long empresaId, Long perfilId, Long abrangenciaId, boolean active, LocalDateTime createdAt) {}
    public record ApiPagedResult<T>(List<T> items, long total) {}

    @PostMapping
    @Operation(summary = "Create a new user")
    public ResponseEntity<UserResponse> createUser(@RequestBody final CreateUserRequest request) {
        final var command = UserApiMapper.toCreateCommand(request);
        final User createdUser = userUseCase.createUser(command);
        return ResponseEntity.status(HttpStatus.CREATED).body(UserApiMapper.toResponse(createdUser));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Find a user by their ID")
    public ResponseEntity<UserResponse> findUserById(@PathVariable final Long id) {
        final User user = userUseCase.findById(new UserId(id));
        return ResponseEntity.ok(UserApiMapper.toResponse(user));
    }

    @GetMapping
    @Operation(summary = "Find all active users with pagination")
    public ResponseEntity<ApiPagedResult<UserResponse>> findUsers(
        @RequestParam(defaultValue = "0") final int page,
        @RequestParam(defaultValue = "10") final int size) {
        final PagedResult<User> userPagedResult = userUseCase.findAllActive(new PageQuery(page, size));
        return ResponseEntity.ok(UserApiMapper.toPagedResponse(userPagedResult));
    }

    @PatchMapping("/{id}/email")
    @Operation(summary = "Update a user's email")
    public ResponseEntity<UserResponse> updateUserEmail(@PathVariable final Long id, @RequestBody final UpdateEmailRequest request) {
        final var command = new UserUseCase.UpdateEmailCommand(new UserId(id), new Email(request.newEmail()));
        final User updatedUser = userUseCase.updateEmail(command);
        return ResponseEntity.ok(UserApiMapper.toResponse(updatedUser));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Deactivate a user")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deactivateUser(@PathVariable final Long id) {
        userUseCase.deactivateUser(new UserId(id));
    }

    /**
     * Private mapper to keep API DTOs decoupled from the domain.
     */
    private static class UserApiMapper {
        static UserUseCase.CreateUserCommand toCreateCommand(final CreateUserRequest request) {
            return new UserUseCase.CreateUserCommand(
                request.name(),
                request.cpf(),
                request.username(),
                request.email(),
                request.password(),
                request.empresaId(),
                request.perfilId(),
                request.abrangenciaId()
            );
        }

        static UserResponse toResponse(final User user) {
            return new UserResponse(
                user.id().value(),
                user.name().value(),
                user.cpf().value(),
                user.username().value(),
                user.email().value(),
                user.empresaId() != null ? user.empresaId().value() : null,
                user.perfilId() != null ? user.perfilId().value() : null,
                user.abrangenciaId() != null ? user.abrangenciaId().value() : null,
                user.isActive(),
                user.createdAt()
            );
        }

        static ApiPagedResult<UserResponse> toPagedResponse(final PagedResult<User> pagedResult) {
            final List<UserResponse> userResponses = pagedResult.items().stream()
                .map(UserApiMapper::toResponse)
                .collect(Collectors.toList());
            return new ApiPagedResult<>(userResponses, pagedResult.total());
        }
    }
}
