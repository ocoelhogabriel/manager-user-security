package com.ocoelhogabriel.usersecurity.application.service;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

/**
 * Service for validating and processing URLs in the application.
 */
@Service
public class UrlValidationService {
    
    private static final Logger logger = LoggerFactory.getLogger(UrlValidationService.class);
    
    private final List<String> PUBLIC_URLS = Arrays.asList(
        "/api/auth/v1/.*",
        "/api/health/.*",
        "/api/device/v1/keep-alive/.*",
        "/api/device/v1/auth-validate",
        "/api/device/v1/auth",
        "/v2/api-docs.*",
        "/v3/api-docs.*",
        "/swagger-resources.*",
        "/configuration/ui",
        "/configuration/security",
        "/swagger-ui.*",
        "/webjars/.*"
    );
    
    private final Map<String, String> HTTP_METHOD_TO_ACTION = new HashMap<>();
    
    public UrlValidationService() {
        HTTP_METHOD_TO_ACTION.put("GET", "READ");
        HTTP_METHOD_TO_ACTION.put("POST", "CREATE");
        HTTP_METHOD_TO_ACTION.put("PUT", "UPDATE");
        HTTP_METHOD_TO_ACTION.put("DELETE", "DELETE");
        HTTP_METHOD_TO_ACTION.put("PATCH", "UPDATE");
    }
    
    /**
     * Checks if a URL is in the public URL list and doesn't require authentication.
     *
     * @param url the URL to check
     * @return true if the URL is public, false otherwise
     */
    public boolean isPublicUrl(String url) {
        for (String publicUrlPattern : PUBLIC_URLS) {
            if (Pattern.matches(publicUrlPattern, url)) {
                return true;
            }
        }
        return false;
    }
    
    /**
     * Validates if a URL is valid according to the application's URL structure.
     *
     * @param url the URL to validate
     * @param method the HTTP method
     * @return true if the URL is valid, false otherwise
     */
    public boolean isValidUrl(String url, String method) {
        try {
            String[] parts = url.split("/");
            
            if (parts.length < 5) {
                logger.debug("URL {} is invalid - insufficient parts", url);
                return false;
            }
            
            // URL structure should be /api/{resource}/v{version}/{action}
            if (!parts[1].equalsIgnoreCase("api")) {
                logger.debug("URL {} is invalid - doesn't start with /api", url);
                return false;
            }
            
            // Check if the version part starts with v and is followed by a number
            String versionPart = parts[4];
            if (!versionPart.toLowerCase().startsWith("v") || 
                    !Pattern.matches("v[0-9]+", versionPart.toLowerCase())) {
                logger.debug("URL {} is invalid - version part {} is not valid", url, versionPart);
                return false;
            }
            
            // Additional validation can be added here
            
            return true;
        } catch (Exception e) {
            logger.error("Error validating URL: {}", url, e);
            return false;
        }
    }
    
    /**
     * Extracts the resource name from a URL.
     *
     * @param url the URL to extract from
     * @return the resource name
     */
    public String extractResourceNameFromUrl(String url) {
        try {
            String[] parts = url.split("/");
            if (parts.length >= 3) {
                return parts[3].toUpperCase();
            }
        } catch (Exception e) {
            logger.error("Error extracting resource name from URL: {}", url, e);
        }
        return null;
    }
    
    /**
     * Maps an HTTP method to an action name.
     *
     * @param method the HTTP method
     * @return the action name
     */
    public String mapHttpMethodToAction(String method) {
        return HTTP_METHOD_TO_ACTION.getOrDefault(method.toUpperCase(), "READ");
    }
    
    /**
     * Extracts the version number from a URL.
     *
     * @param url the URL to extract from
     * @return the version number as a string
     */
    public String extractVersionFromUrl(String url) {
        try {
            String[] parts = url.split("/");
            if (parts.length >= 5 && parts[4].toLowerCase().startsWith("v")) {
                return parts[4].substring(1);
            }
        } catch (Exception e) {
            logger.error("Error extracting version from URL: {}", url, e);
        }
        return "1"; // Default to version 1
    }
    
    /**
     * Extracts the action from a URL.
     *
     * @param url the URL to extract from
     * @param method the HTTP method
     * @return the action name
     */
    public String extractActionFromUrl(String url, String method) {
        try {
            String[] parts = url.split("/");
            if (parts.length >= 6) {
                return parts[5].toUpperCase();
            }
            return mapHttpMethodToAction(method);
        } catch (Exception e) {
            logger.error("Error extracting action from URL: {}", url, e);
            return mapHttpMethodToAction(method);
        }
    }
}