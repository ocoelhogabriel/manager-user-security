package com.ocoelhogabriel.manager_user_security.infrastructure.persistence.entity;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

/**
 * JPA entity representing a resource in the system.
 */
@Entity
@Table(name = "resources")
@EntityListeners(AuditingEntityListener.class)
public class ResourceEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Size(max = 100)
    @Column(unique = true)
    private String name;

    @Size(max = 255)
    private String description;

    @NotBlank
    @Size(max = 255)
    private String path;

    @NotBlank
    @Size(max = 10)
    private String method;

    @CreatedDate
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @LastModifiedDate
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @OneToMany(mappedBy = "resource")
    private Set<PermissionEntity> permissions = new HashSet<>();

    /**
     * Default constructor.
     */
    public ResourceEntity() {
    }

    /**
     * Constructor with basic fields.
     *
     * @param name the resource name
     * @param path the resource path pattern
     * @param method the HTTP method
     */
    public ResourceEntity(String name, String path, String method) {
        this.name = name;
        this.path = path;
        this.method = method;
    }

    /**
     * Constructor with all fields.
     *
     * @param name the resource name
     * @param description the resource description
     * @param path the resource path pattern
     * @param method the HTTP method
     */
    public ResourceEntity(String name, String description, String path, String method) {
        this.name = name;
        this.description = description;
        this.path = path;
        this.method = method;
    }

    // Getters and Setters

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    public Set<PermissionEntity> getPermissions() {
        return permissions;
    }

    public void setPermissions(Set<PermissionEntity> permissions) {
        this.permissions = permissions;
    }

    @Override
    public String toString() {
        return "ResourceEntity{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", path='" + path + '\'' +
                ", method='" + method + '\'' +
                '}';
    }
}
