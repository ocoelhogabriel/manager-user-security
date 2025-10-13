package com.ocoelhogabriel.manager_user_security.unit;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

/**
 * Test configuration for unit tests.
 * Provides common beans needed for testing.
 */
@TestConfiguration
public class TestConfig {

    /**
     * Provides a password encoder for testing.
     * 
     * @return a BCryptPasswordEncoder
     */
    @Bean
    @Primary
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
    
    /**
     * Provides a configured ObjectMapper for testing.
     * 
     * @return an ObjectMapper with JavaTimeModule registered
     */
    @Bean
    @Primary
    public ObjectMapper objectMapper() {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        return objectMapper;
    }
    
    /**
     * Provides a mock UserDetailsService for testing.
     * 
     * @return a mock UserDetailsService
     */
    @Bean
    @Primary
    public UserDetailsService testUserDetailsService() {
        return new TestUserDetailsService();
    }
    
    /**
     * Simple UserDetailsService implementation for testing.
     */
    public static class TestUserDetailsService implements UserDetailsService {
        
        private final Map<String, UserDetails> users = new HashMap<>();
        
        public TestUserDetailsService() {
            // Add test users
            addUser("admin", "password", "ROLE_ADMIN");
            addUser("user", "password", "ROLE_USER");
        }
        
        private void addUser(String username, String password, String role) {
            PasswordEncoder encoder = new BCryptPasswordEncoder();
            Collection<GrantedAuthority> authorities = Collections.singleton(new SimpleGrantedAuthority(role));
            
            User user = new User(username, encoder.encode(password), authorities);
            users.put(username, user);
        }
        
        @Override
        public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
            UserDetails user = users.get(username);
            if (user == null) {
                throw new UsernameNotFoundException("User not found: " + username);
            }
            return user;
        }
    }
}