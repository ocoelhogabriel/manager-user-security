package com.ocoelhogabriel.manager_user_security.infrastructure.security.authorization;

import java.util.Objects;

import com.ocoelhogabriel.manager_user_security.infrastructure.security.authorization.Resource;
import com.ocoelhogabriel.manager_user_security.domain.valueobject.HttpMethod;

/**
 * Utility class for validating and classifying URLs
 */
public class UrlPathMatcher {

    private final Resource resource;
    private final String message;

    public UrlPathMatcher(Resource resource, String message) {
        this.resource = resource;
        this.message = message;
    }

    /**
     * Validates and classifies a URL based on its path and HTTP method
     *
     * @param url    The URL to validate
     * @param method The HTTP method
     * @return UrlPathMatcher containing the result of validation
     */
    public static UrlPathMatcher validateUrl(String url, String method) {
        try {
            String[] parts = url.split("/");

            // Check if URL has enough parts
            if (parts.length < 5) {
                return new UrlPathMatcher(null, "Invalid URL format");
            }

            // Parse URL parts
            String serverPart = "/" + parts[1];
            String resourcePart = "/api/" + parts[3];
            String versionPart = "/" + parts[4];
            String actionPart = parts.length > 5 ? "/" + parts[5] : null;

            // Map URL parts to enums
            String server = mapServerPart(serverPart.toUpperCase());
            String resourceName = mapResourcePart(resourcePart.toUpperCase());
            Resource resource = findResourceByUrl(resourcePart.toUpperCase());
            String version = mapVersionPart(versionPart.toUpperCase());
            String action = actionPart != null ? mapActionPart(actionPart.toUpperCase()) : "";

            // Validate mapped values
            if (server == null || resourceName == null || version == null) {
                return new UrlPathMatcher(null, "Error in URL mapping");
            }

            // Special case for GET requests with ID parameter
            if (method.equalsIgnoreCase("GET") && actionPart != null && action == null) {
                if (!actionPart.matches("/\\d+")) {
                    return new UrlPathMatcher(resource, "Invalid URL for SEARCH action: missing or invalid ID");
                }
                return new UrlPathMatcher(resource, "SEARCH");
            }

            return new UrlPathMatcher(
                    resource, 
                    "Valid URL! Method: " + method + 
                    ", Server: " + server + 
                    ", Resource: " + resourceName + 
                    ", Version: " + version + 
                    ", Action: " + action);
        } catch (Exception e) {
            return new UrlPathMatcher(null, "Error processing URL");
        }
    }
    
    /**
     * Maps server part to server name
     * 
     * @param serverPart The server part of the URL
     * @return The mapped server name
     */
    private static String mapServerPart(String serverPart) {
        // TODO: Implement server mapping based on the application requirements
        return serverPart;
    }
    
    /**
     * Maps resource part to resource name
     * 
     * @param resourcePart The resource part of the URL
     * @return The mapped resource name
     */
    private static String mapResourcePart(String resourcePart) {
        // TODO: Implement resource mapping based on the application requirements
        return resourcePart;
    }
    
    /**
     * Finds a resource by its URL
     * 
     * @param resourceUrl The resource URL
     * @return The found resource
     */
    private static Resource findResourceByUrl(String resourceUrl) {
        // TODO: Implement resource finding based on the application requirements
        return new Resource(resourceUrl, resourceUrl);
    }
    
    /**
     * Maps version part to version name
     * 
     * @param versionPart The version part of the URL
     * @return The mapped version name
     */
    private static String mapVersionPart(String versionPart) {
        // TODO: Implement version mapping based on the application requirements
        return versionPart;
    }
    
    /**
     * Maps action part to action name
     * 
     * @param actionPart The action part of the URL
     * @return The mapped action name
     */
    private static String mapActionPart(String actionPart) {
        // TODO: Implement action mapping based on the application requirements
        return actionPart;
    }

    public Resource getResource() {
        return resource;
    }

    public String getMessage() {
        return message;
    }
}