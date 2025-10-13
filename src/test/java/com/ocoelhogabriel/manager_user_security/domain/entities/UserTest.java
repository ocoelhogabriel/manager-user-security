package com.ocoelhogabriel.manager_user_security.domain.entities;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import com.ocoelhogabriel.manager_user_security.domain.entities.User;
import com.ocoelhogabriel.manager_user_security.domain.value_objects.Email;
import com.ocoelhogabriel.manager_user_security.domain.value_objects.HashedPassword;
import com.ocoelhogabriel.manager_user_security.domain.value_objects.UserId;
import com.ocoelhogabriel.manager_user_security.domain.value_objects.Username;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDateTime;

/**
 * Testes unitários para User Entity
 * Aplica Clean Architecture - Domain Layer Testing
 * Testa comportamentos e invariantes da entidade User
 */
@DisplayName("User Entity Tests")
class UserTest {

    private static final String VALID_BCRYPT_HASH = "$2a$10$CdWCgQQ6uYL0s5G3Av1k/.kRl5c6aUm8FoU6.kDbRMVk8VNxe9Gke";
    private static final String DIFFERENT_BCRYPT_HASH = "$2a$10$N.XqKhLZFEt2l5.GeFSX1e0Rn8Z8J8Zi8P4Uy3VZQeR6JQJQ2JQJQ";

    @Test
    @DisplayName("Should create new user successfully")
    void shouldCreateNewUserSuccessfully() {
        // Given
        final Username username = Username.of("john_doe");
        final Email email = Email.of("john@example.com");
        final HashedPassword password = HashedPassword.of(VALID_BCRYPT_HASH);
        
        // When
        final User user = User.create(username, email, password);
        
        // Then
        assertNotNull(user);
        assertNull(user.id());
        assertEquals(username, user.username());
        assertEquals(email, user.email());
        assertEquals(password, user.password());
        assertTrue(user.isActive());
        assertNotNull(user.createdAt());
        assertNotNull(user.updatedAt());
        assertEquals(user.createdAt(), user.updatedAt());
    }

    @Test
    @DisplayName("Should restore user from persistence successfully")
    void shouldRestoreUserFromPersistenceSuccessfully() {
        // Given
        final UserId userId = UserId.of(1L);
        final Username username = Username.of("restored_user");
        final Email email = Email.of("restored@example.com");
        final HashedPassword password = HashedPassword.of(VALID_BCRYPT_HASH);
        final LocalDateTime now = LocalDateTime.now();
        
        // When
        final User user = User.restore(userId, username, email, password, true, now, now);
        
        // Then
        assertNotNull(user);
        assertEquals(userId, user.id());
        assertEquals(username, user.username());
        assertEquals(email, user.email());
        assertEquals(password, user.password());
        assertTrue(user.isActive());
        assertEquals(now, user.createdAt());
        assertEquals(now, user.updatedAt());
    }

    @Test
    @DisplayName("Should throw exception when creating user with null username")
    void shouldThrowExceptionWhenCreatingUserWithNullUsername() {
        // Given
        final Email email = Email.of("test@example.com");
        final HashedPassword password = HashedPassword.of(VALID_BCRYPT_HASH);
        
        // When & Then
        assertThrows(IllegalArgumentException.class, 
                    () -> User.create(null, email, password));
    }

    @Test
    @DisplayName("Should throw exception when creating user with null email")
    void shouldThrowExceptionWhenCreatingUserWithNullEmail() {
        // Given
        final Username username = Username.of("test_user");
        final HashedPassword password = HashedPassword.of(VALID_BCRYPT_HASH);
        
        // When & Then
        assertThrows(IllegalArgumentException.class, 
                    () -> User.create(username, null, password));
    }

    @Test
    @DisplayName("Should throw exception when creating user with null password")
    void shouldThrowExceptionWhenCreatingUserWithNullPassword() {
        // Given
        final Username username = Username.of("test_user");
        final Email email = Email.of("test@example.com");
        
        // When & Then
        assertThrows(IllegalArgumentException.class, 
                    () -> User.create(username, email, null));
    }

    @Test
    @DisplayName("Should deactivate user successfully")
    void shouldDeactivateUserSuccessfully() {
        // Given
        final User user = User.create(
            Username.of("test_user"),
            Email.of("test@example.com"),
            HashedPassword.of(VALID_BCRYPT_HASH)
        );
        
        // When
        final User deactivatedUser = user.deactivate();
        
        // Then
        assertFalse(deactivatedUser.isActive());
        assertEquals(user.username(), deactivatedUser.username());
        assertEquals(user.email(), deactivatedUser.email());
        assertEquals(user.password(), deactivatedUser.password());
        // Then
        assertFalse(deactivatedUser.isActive());
    }

    @Test
    @DisplayName("Should activate user successfully")
    void shouldActivateUserSuccessfully() {
        // Given
        final User user = User.create(
            Username.of("test_user"),
            Email.of("test@example.com"),
            HashedPassword.of(VALID_BCRYPT_HASH)
        ).deactivate();
        
        // When
        final User activatedUser = user.activate();
        
        // Then
        assertTrue(activatedUser.isActive());
        assertEquals(user.username(), activatedUser.username());
        assertEquals(user.email(), activatedUser.email());
        assertEquals(user.password(), activatedUser.password());
    }

    @Test
    @DisplayName("Should change password successfully")
    void shouldChangePasswordSuccessfully() {
        // Given
        final User user = User.create(
            Username.of("test_user"),
            Email.of("test@example.com"),
            HashedPassword.of(VALID_BCRYPT_HASH)
        );
        final HashedPassword newPassword = HashedPassword.of(DIFFERENT_BCRYPT_HASH);
        
        // When
        final User userWithNewPassword = user.changePassword(newPassword);
        
        // Then
        assertEquals(newPassword, userWithNewPassword.password());
        assertEquals(user.username(), userWithNewPassword.username());
        assertEquals(user.email(), userWithNewPassword.email());
        assertEquals(user.isActive(), userWithNewPassword.isActive());
    }

    @Test
    @DisplayName("Should implement equals correctly")
    void shouldImplementEqualsCorrectly() {
        // Given
        final UserId userId = UserId.of(1L);
        final Username username = Username.of("test_user");
        final Email email = Email.of("test@example.com");
        final HashedPassword password = HashedPassword.of(VALID_BCRYPT_HASH);
        final LocalDateTime now = LocalDateTime.now();
        
        final User user1 = User.restore(userId, username, email, password, true, now, now);
        final User user2 = User.restore(userId, username, email, password, true, now, now);
        final User user3 = User.restore(UserId.of(2L), username, email, password, true, now, now);
        
        // Then
        assertEquals(user1, user2);
        assertNotEquals(user1, user3);
        assertNotEquals(user1, null);
        assertNotEquals(user1, "not a user");
    }

    @Test
    @DisplayName("Should implement hashCode correctly")
    void shouldImplementHashCodeCorrectly() {
        // Given
        final UserId userId = UserId.of(1L);
        final Username username = Username.of("test_user");
        final Email email = Email.of("test@example.com");
        final HashedPassword password = HashedPassword.of(VALID_BCRYPT_HASH);
        final LocalDateTime now = LocalDateTime.now();
        
        final User user1 = User.restore(userId, username, email, password, true, now, now);
        final User user2 = User.restore(userId, username, email, password, true, now, now);
        
        // Then
        assertEquals(user1.hashCode(), user2.hashCode());
    }

    @Test
    @DisplayName("Should be immutable")
    void shouldBeImmutable() {
        // Given
        final User originalUser = User.create(
            Username.of("test_user"),
            Email.of("test@example.com"),
            HashedPassword.of(VALID_BCRYPT_HASH)
        );
        
        // When
        final User passwordChangedUser = originalUser.changePassword(HashedPassword.of(DIFFERENT_BCRYPT_HASH));
        final User deactivatedUser = originalUser.deactivate();
        final User activatedUser = originalUser.activate();
        
        // Then - Original user should remain unchanged
        assertTrue(originalUser.isActive());
        assertEquals(HashedPassword.of(VALID_BCRYPT_HASH), originalUser.password());
        
        // New instances should have expected changes
        assertEquals(HashedPassword.of(DIFFERENT_BCRYPT_HASH), passwordChangedUser.password());
        assertFalse(deactivatedUser.isActive());
        assertTrue(activatedUser.isActive());
    }

    @Test
    @DisplayName("Should handle user without id correctly")
    void shouldHandleUserWithoutIdCorrectly() {
        // Given
        final User user = User.create(
            Username.of("new_user"),
            Email.of("new@example.com"),
            HashedPassword.of(VALID_BCRYPT_HASH)
        );
        
        // Then
        assertNull(user.id());
        assertNotNull(user.username());
        assertNotNull(user.email());
        assertNotNull(user.password());
        assertTrue(user.isActive());
        assertNotNull(user.createdAt());
        assertNotNull(user.updatedAt());
    }

    @ParameterizedTest
    @ValueSource(strings = {"user1", "test_user", "valid_username", "another_user"})
    @DisplayName("Should create users with different valid usernames")
    void shouldCreateUsersWithDifferentValidUsernames(String usernameValue) {
        // Given
        final Username username = Username.of(usernameValue);
        final Email email = Email.of("user@example.com");
        final HashedPassword password = HashedPassword.of(VALID_BCRYPT_HASH);
        
        // When
        final User user = User.create(username, email, password);
        
        // Then
        assertNotNull(user);
        assertEquals(username, user.username());
        assertTrue(user.isActive());
    }
}