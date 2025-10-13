package com.ocoelhogabriel.usersecurity.application.service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ocoelhogabriel.usersecurity.domain.entity.Role;
import com.ocoelhogabriel.usersecurity.domain.entity.User;
import com.ocoelhogabriel.usersecurity.domain.exception.DomainException;
import com.ocoelhogabriel.usersecurity.domain.repository.UserRepository;
import com.ocoelhogabriel.usersecurity.domain.service.UserService;

/**
 * Implementation of the UserService.
 */
@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private PasswordEncoder passwordEncoder;
    
    @Override
    @Transactional
    public User createUser(String username, String email, String password) {
        // Check if username already exists
        if (userRepository.existsByUsername(username)) {
            throw new DomainException("Username already exists");
        }
        
        // Check if email already exists
        if (userRepository.existsByEmail(email)) {
            throw new DomainException("Email already exists");
        }
        
        // Hash the password
        String hashedPassword = passwordEncoder.encode(password);
        
        // Create user
        User user = new User(username, email, hashedPassword);
        
        // Save user
        return userRepository.save(user);
    }

    @Override
    public Optional<User> findById(UUID id) {
        return userRepository.findById(id);
    }

    @Override
    public Optional<User> findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    @Override
    public List<User> findAll() {
        return userRepository.findAll();
    }

    @Override
    @Transactional
    public User updateUser(User user) {
        // Check if user exists
        if (!userRepository.existsById(user.getId())) {
            throw new DomainException("User not found");
        }
        
        return userRepository.save(user);
    }

    @Override
    @Transactional
    public User updatePassword(User user, String newPassword) {
        // Check if user exists
        if (!userRepository.existsById(user.getId())) {
            throw new DomainException("User not found");
        }
        
        // Hash the new password
        String hashedPassword = passwordEncoder.encode(newPassword);
        user.setPasswordHash(hashedPassword);
        
        return userRepository.save(user);
    }

    @Override
    @Transactional
    public User assignRole(User user, Role role) {
        // Check if user exists
        if (!userRepository.existsById(user.getId())) {
            throw new DomainException("User not found");
        }
        
        user.addRole(role);
        
        return userRepository.save(user);
    }

    @Override
    @Transactional
    public User removeRole(User user, Role role) {
        // Check if user exists
        if (!userRepository.existsById(user.getId())) {
            throw new DomainException("User not found");
        }
        
        user.removeRole(role);
        
        return userRepository.save(user);
    }

    @Override
    @Transactional
    public User deactivateUser(User user) {
        // Check if user exists
        if (!userRepository.existsById(user.getId())) {
            throw new DomainException("User not found");
        }
        
        user.setActive(false);
        
        return userRepository.save(user);
    }

    @Override
    @Transactional
    public User activateUser(User user) {
        // Check if user exists
        if (!userRepository.existsById(user.getId())) {
            throw new DomainException("User not found");
        }
        
        user.setActive(true);
        
        return userRepository.save(user);
    }

    @Override
    @Transactional
    public void deleteUser(UUID id) {
        // Check if user exists
        if (!userRepository.existsById(id)) {
            throw new DomainException("User not found");
        }
        
        userRepository.deleteById(id);
    }
}