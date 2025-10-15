package com.ocoelhogabriel.manager_user_security.infrastructure.persistence.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.ocoelhogabriel.manager_user_security.infrastructure.persistence.entity.ResourceEntity;

/**
 * Repository for managing ResourceEntity entities.
 */
@Repository
public interface ResourceJpaRepository extends JpaRepository<ResourceEntity, Long> {

    /**
     * Find a resource by its URL pattern and HTTP method.
     *
     * @param urlPattern the resource URL pattern
     * @param method the HTTP method
     * @return the resource, if found
     */
    Optional<ResourceEntity> findByUrlPatternAndMethod(String urlPattern, String method);

    /**
     * Find a resource by name.
     *
     * @param name the name of the resource
     * @return the resource, if found
     */
    Optional<ResourceEntity> findByName(String name);
    
    /**
     * Find resources by version.
     *
     * @param version the version
     * @return the list of resources with the specified version
     */
    List<ResourceEntity> findByVersion(String version);

    /**
     * Check if a resource with the given URL pattern exists.
     *
     * @param urlPattern the URL pattern to check
     * @return true if a resource with the URL pattern exists, false otherwise
     */
    boolean existsByUrlPattern(String urlPattern);

    /**
     * Finds resources that match a given URL and HTTP method.
     * This method should implement URL pattern matching logic to find all resources
     * whose URL patterns match the given URL.
     *
     * @param url the URL to match against resource patterns
     * @param method the HTTP method to match
     * @return a list of matching resources
     */
    @Query("SELECT r FROM ResourceEntity r WHERE :url LIKE r.urlPattern AND r.allowedMethods LIKE CONCAT('%', :method, '%')")
    List<ResourceEntity> findMatchingResources(@Param("url") String url, @Param("method") String method);

    /**
     * Finds a resource by URL pattern and checks if the method is contained in its allowed methods.
     *
     * @param urlPattern the URL pattern of the resource
     * @param method the HTTP method
     * @return a list of resources matching the criteria
     */
    @Query("SELECT r FROM ResourceEntity r WHERE r.urlPattern = :urlPattern AND r.allowedMethods LIKE CONCAT('%', :method, '%')")
    List<ResourceEntity> findByUrlPatternAndAllowedMethodsContains(@Param("urlPattern") String urlPattern, @Param("method") String method);
}
