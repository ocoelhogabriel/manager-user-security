package com.ocoelhogabriel.manager_user_security.interfaces.dto.resource;

/**
 * Data Transfer Object for resource responses.
 */
public class ResourceResponse {

    private Long id;
    private String name;
    private String path;
    private String description;
    private String method;

    /**
     * Default constructor.
     */
    public ResourceResponse() {
    }

    /**
     * Constructor with all fields.
     *
     * @param id the resource ID
     * @param name the resource name
     * @param path the resource path
     * @param description the resource description
     * @param method the HTTP method
     */
    public ResourceResponse(Long id, String name, String path, String description, String method) {
        this.id = id;
        this.name = name;
        this.path = path;
        this.description = description;
        this.method = method;
    }

    /**
     * Gets the ID.
     *
     * @return the id
     */
    public Long getId() {
        return id;
    }

    /**
     * Sets the ID.
     *
     * @param id the id to set
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * Gets the name.
     *
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the name.
     *
     * @param name the name to set
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Gets the path.
     *
     * @return the path
     */
    public String getPath() {
        return path;
    }

    /**
     * Sets the path.
     *
     * @param path the path to set
     */
    public void setPath(String path) {
        this.path = path;
    }

    /**
     * Gets the description.
     *
     * @return the description
     */
    public String getDescription() {
        return description;
    }

    /**
     * Sets the description.
     *
     * @param description the description to set
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * Gets the HTTP method.
     *
     * @return the method
     */
    public String getMethod() {
        return method;
    }

    /**
     * Sets the HTTP method.
     *
     * @param method the method to set
     */
    public void setMethod(String method) {
        this.method = method;
    }
}
