package com.ocoelhogabriel.manager_user_security.application.validator;

import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * Class responsible for validating URLs and extracting resource information.
 */
@Component
public class UrlValidator {

    private static final Logger logger = LoggerFactory.getLogger(UrlValidator.class);
    
    @Value("${security.public-urls:}")
    private List<String> publicUrls;
    
    @Value("${security.api-base-pattern:/api}")
    private String apiBasePattern;
    
    private static final List<String> VALID_METHODS = Arrays.asList("GET", "POST", "PUT", "DELETE", "PATCH");
    
    /**
     * Checks if a URL is public (doesn't require authentication).
     *
     * @param url the URL to check
     * @return true if the URL is public, false otherwise
     */
    public boolean isPublicUrl(String url) {
        if (url == null) {
            return false;
        }
        
        // Check if URL is in the public URLs list
        for (String pattern : publicUrls) {
            if (url.matches(pattern) || url.startsWith(pattern)) {
                return true;
            }
        }
        
        return false;
    }
    
    /**
     * Checks if a URL and HTTP method combination is valid.
     *
     * @param url the URL to check
     * @param method the HTTP method
     * @return true if valid, false otherwise
     */
    public boolean isValidUrl(String url, String method) {
        if (url == null || method == null) {
            return false;
        }
        
        // Check if method is valid
        if (!VALID_METHODS.contains(method.toUpperCase())) {
            logger.debug("Invalid HTTP method: {}", method);
            return false;
        }
        
        // Check if URL matches the API base pattern
        if (!url.startsWith(apiBasePattern)) {
            logger.debug("URL does not start with API base pattern: {}", url);
            return false;
        }
        
        // Additional validation logic could be added here
        return true;
    }
    
    /**
     * Extracts the resource name from a URL.
     *
     * @param url the URL to extract from
     * @return the resource name
     */
    public String extractResourceNameFromUrl(String url) {
        if (url == null) {
            return null;
        }
        
        // Remove API base pattern
        String path = url.startsWith(apiBasePattern) ? 
                url.substring(apiBasePattern.length()) : url;
        
        // Split path into segments
        String[] segments = path.split("/");
        
        // Find the resource segment (usually the second one after the version)
        if (segments.length > 2) {
            // Check if second segment is a version number (v1, v2, etc.)
            if (Pattern.matches("v\\d+", segments[1])) {
                return segments.length > 2 ? segments[2] : null;
            } else {
                return segments[1];
            }
        }
        
        return segments.length > 1 ? segments[1] : null;
    }
    
    /**
     * Maps HTTP method to a permission action.
     *
     * @param method the HTTP method
     * @return the corresponding action
     */
    public String mapHttpMethodToAction(String method) {
        if (method == null) {
            return null;
        }
        
        switch (method.toUpperCase()) {
            case "GET":
                return "READ";
            case "POST":
                return "CREATE";
            case "PUT":
                return "UPDATE";
            case "PATCH":
                return "UPDATE";
            case "DELETE":
                return "DELETE";
            default:
                return method.toUpperCase();
        }
    }
}