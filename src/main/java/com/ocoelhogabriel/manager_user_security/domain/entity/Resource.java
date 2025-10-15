package com.ocoelhogabriel.manager_user_security.domain.entity;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.Collections;

/**
 * Resource domain entity representing a protected resource in the system. Resources are elements that can be accessed via the API and may require
 * authorization.
 */
public class Resource {
    // Changed from final to allow modification through setter
    private Long id;
    private String name;
    private String description;
    private String urlPattern;
    private String version;
    private final Set<String> allowedMethods;

    /**
     * Creates a new Resource with the specified name and URL pattern.
     *
     * @param name        the name of the resource
     * @param description the description of the resource
     * @param urlPattern  the URL pattern of the resource
     * @param version     the API version of the resource
     */
    public Resource(Long id, String name, String description, String urlPattern, String version) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.urlPattern = urlPattern;
        this.version = version;
        this.allowedMethods = new HashSet<>();
    }

    /**
     * Creates a Resource with an existing ID.
     *
     * @param id             the ID of the resource
     * @param name           the name of the resource
     * @param description    the description of the resource
     * @param urlPattern     the URL pattern of the resource
     * @param version        the API version of the resource
     * @param allowedMethods the HTTP methods allowed for this resource
     */
    public Resource(Long id, String name, String description, String urlPattern, String version, Set<String> allowedMethods) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.urlPattern = urlPattern;
        this.version = version;
        this.allowedMethods = allowedMethods != null ? new HashSet<>(allowedMethods) : new HashSet<>();
    }

    /**
     * Default constructor for frameworks.
     */
    public Resource() {
        this.allowedMethods = new HashSet<>();
    }

    public Long getId() {
        return id;
    }

    // Modified setter to ensure it works properly
    public void setId(Long newId) {
        this.id = newId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getUrlPattern() {
        return urlPattern;
    }

    public void setUrlPattern(String urlPattern) {
        this.urlPattern = urlPattern;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public Set<String> getAllowedMethods() {
        return Collections.unmodifiableSet(allowedMethods);
    }

    /**
     * Adds an allowed HTTP method to the resource.
     *
     * @param method the HTTP method to add
     * @return true if the method was added, false if it was already present
     */
    public boolean addAllowedMethod(String method) {
        return allowedMethods.add(method.toUpperCase());
    }

    /**
     * Removes an allowed HTTP method from the resource.
     *
     * @param method the HTTP method to remove
     * @return true if the method was removed, false if it was not present
     */
    public boolean removeAllowedMethod(String method) {
        return allowedMethods.remove(method.toUpperCase());
    }

    /**
     * Checks if the resource allows a specific HTTP method.
     *
     * @param method the HTTP method to check
     * @return true if the resource allows the method, false otherwise
     */
    public boolean isMethodAllowed(String method) {
        return allowedMethods.contains(method.toUpperCase());
    }

    /**
     * Permite modificar os métodos permitidos para o recurso.
     */
    public void setAllowedMethods(Set<String> allowedMethods) {
        this.allowedMethods.clear();
        if (allowedMethods != null) {
            this.allowedMethods.addAll(allowedMethods);
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        Resource resource = (Resource) o;
        return Objects.equals(id, resource.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

}
