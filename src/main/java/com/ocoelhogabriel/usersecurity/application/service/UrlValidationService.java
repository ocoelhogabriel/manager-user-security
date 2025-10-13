package com.ocoelhogabriel.usersecurity.application.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;

/**
 * Service for validating URLs and extracting resource information.
 */
@Service
public class UrlValidationService {

    @Value("${api.security.public-urls}")
    private String[] publicUrlPatterns;

    private static final List<String> DEFAULT_PUBLIC_URLS = Arrays.asList(
        "/api/auth/v1/.*",
        "/api/health/.*",
        "/api/device/v1/keep-alive/.*",
        "/api/device/v1/auth-validate",
        "/api/device/v1/auth",
        "/v2/api-docs.*",
        "/v3/api-docs.*",
        "/swagger-resources.*",
        "/configuration/.*",
        "/swagger-ui.*",
        "/webjars/.*"
    );

    /**
     * Checks if a URL is a public URL that doesn't require authentication.
     *
     * @param url the URL to check
     * @return true if the URL is public, false otherwise
     */
    public boolean isPublicUrl(String url) {
        // Check configured public URLs
        if (publicUrlPatterns != null) {
            for (String pattern : publicUrlPatterns) {
                if (Pattern.matches(pattern, url)) {
                    return true;
                }
            }
        }

        // Check default public URLs
        for (String pattern : DEFAULT_PUBLIC_URLS) {
            if (Pattern.matches(pattern, url)) {
                return true;
            }
        }

        return false;
    }

    /**
     * Validates if a URL has a valid format.
     *
     * @param url the URL to validate
     * @param method the HTTP method
     * @return true if the URL is valid, false otherwise
     */
    public boolean isValidUrl(String url, String method) {
        try {
            // URL format should be: /api/{resource}/v{version}/{action}
            String[] parts = url.split("/");
            
            if (parts.length < 5) {
                return false;
            }
            
            // Check for /api/ prefix
            if (!parts[1].equals("api")) {
                return false;
            }
            
            // Check for version format
            if (!parts[4].matches("v\\d+")) {
                return false;
            }
            
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Extracts the resource name from a URL.
     *
     * @param url the URL to extract from
     * @return the extracted resource name
     */
    public String extractResourceNameFromUrl(String url) {
        String[] parts = url.split("/");
        if (parts.length >= 4) {
            return parts[3];
        }
        return "";
    }

    /**
     * Maps an HTTP method to an action name.
     *
     * @param method the HTTP method to map
     * @return the mapped action name
     */
    public String mapHttpMethodToAction(String method) {
        return switch (method.toUpperCase()) {
            case "GET" -> "READ";
            case "POST" -> "CREATE";
            case "PUT", "PATCH" -> "UPDATE";
            case "DELETE" -> "DELETE";
            default -> "UNKNOWN";
        };
    }

    /**
     * Extracts the version from a URL.
     *
     * @param url the URL to extract from
     * @return the extracted version
     */
    public String extractVersionFromUrl(String url) {
        String[] parts = url.split("/");
        if (parts.length >= 5) {
            return parts[4];
        }
        return "";
    }

    /**
     * Extracts the action from a URL.
     *
     * @param url the URL to extract from
     * @return the extracted action
     */
    public String extractActionFromUrl(String url) {
        String[] parts = url.split("/");
        if (parts.length >= 6) {
            return parts[5];
        }
        return "";
    }

    /**
     * Checks if a URL contains an ID parameter.
     *
     * @param url the URL to check
     * @return true if the URL contains an ID parameter, false otherwise
     */
    public boolean urlContainsId(String url) {
        String[] parts = url.split("/");
        return parts.length >= 6 && parts[5].matches("\\d+");
    }
}