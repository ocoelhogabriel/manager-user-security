package com.ocoelhogabriel.manager_user_security.application.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ocoelhogabriel.manager_user_security.domain.entity.User;
import com.ocoelhogabriel.manager_user_security.domain.repository.UserRepository;
import com.ocoelhogabriel.manager_user_security.domain.service.AuthorizationService;
import com.ocoelhogabriel.manager_user_security.application.validator.UrlValidator;

/**
 * Implementation of the authorization service.
 */
@Service
public class AuthorizationServiceImpl implements AuthorizationService {

    private static final Logger logger = LoggerFactory.getLogger(AuthorizationServiceImpl.class);
    
    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private UrlValidator urlValidator;
    
    @Override
    public boolean hasPermission(User user, String resource, String action) {
        if (user == null || resource == null || action == null) {
            return false;
        }
        
        // Check if the user has the required permission through their roles
        return user.getRoles().stream()
                .anyMatch(role -> role.hasPermission(resource, action));
    }
    
    /**
     * Checks if a user has permission to perform an action.
     *
     * @param username the username of the user
     * @param resource the resource name
     * @param action the action to check
     * @return true if the user has permission, false otherwise
     */
    public boolean hasPermission(String username, String resource, String action) {
        // Find the user
        return userRepository.findByUsername(username)
                .map(user -> hasPermission(user, resource, action))
                .orElse(false);
    }

    @Override
    public boolean canAccessUrl(User user, String url, String method) {
        if (user == null || url == null || method == null) {
            return false;
        }
        
        // Check if the URL is public
        if (urlValidator.isPublicUrl(url)) {
            return true;
        }
        
        // Check if the URL is valid
        if (!urlValidator.isValidUrl(url, method)) {
            logger.debug("URL {} with method {} is not valid", url, method);
            return false;
        }
        
        // Extract resource and action from URL
        String resource = urlValidator.extractResourceNameFromUrl(url);
        String action = urlValidator.mapHttpMethodToAction(method);
        
        // Check if the user has permission
        return hasPermission(user, resource, action);
    }

    @Override
    public boolean hasRole(User user, String roleName) {
        if (user == null || roleName == null) {
            return false;
        }
        
        return user.hasRole(roleName);
    }
}
