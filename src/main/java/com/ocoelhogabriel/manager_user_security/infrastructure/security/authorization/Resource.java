package com.ocoelhogabriel.manager_user_security.infrastructure.security.authorization;

/**
 * Simple implementation of Resource for the UrlPathMatcher
 */
public record Resource(
        String name,
        String description) {
    /**
     * Creates a new Resource with the specified name and description.
     *
     * @param name        the name of the resource
     * @param description the description of the resource
     */
    public Resource {
    }

    /**
     * Gets the name of the resource.
     *
     * @return the name of the resource
     */
    @Override
    public String name() {
        return name;
    }

    /**
     * Gets the description of the resource.
     *
     * @return the description of the resource
     */
    @Override
    public String description() {
        return description;
    }
}