package com.ocoelhogabriel.manager_user_security.domain.entity;

import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

/**
 * User domain entity representing a user in the system.
 * This is the core domain entity and is independent of any persistence mechanisms.
 */
public class User {
    private Long id;
    private String username;
    private String email;
    private String passwordHash;
    private String fullName;
    private boolean active;
    private String image;
    private String registrationNumber;
    private String phone;
    private String cpf;
    private Long companyId;
    private String companyName;
    private Set<Role> roles;

    /**
     * Default constructor for User.
     */
    public User() {
        this.active = true;
        this.roles = new HashSet<>();
    }
    
    /**
     * Creates a new User with the specified username and email.
     *
     * @param username the username of the user
     * @param email the email of the user
     * @param passwordHash the hashed password of the user
     */
    public User(String username, String email, String passwordHash) {
        this.username = username;
        this.email = email;
        this.passwordHash = passwordHash;
        this.active = true;
        this.roles = new HashSet<>();
    }
    
    /**
     * Creates a new User with the specified username, email, and full name.
     *
     * @param username the username of the user
     * @param email the email of the user
     * @param passwordHash the hashed password of the user
     * @param fullName the full name of the user
     */
    public User(String username, String email, String passwordHash, String fullName) {
        this.username = username;
        this.email = email;
        this.passwordHash = passwordHash;
        this.fullName = fullName;
        this.active = true;
        this.roles = new HashSet<>();
    }
    
    /**
     * Creates a new User with the specified username, email, full name, and other user details.
     *
     * @param username the username of the user
     * @param email the email of the user
     * @param passwordHash the hashed password of the user
     * @param fullName the full name of the user
     * @param image the image URL of the user
     * @param registrationNumber the registration number of the user
     * @param phone the phone number of the user
     * @param cpf the CPF (tax ID) of the user
     * @param companyId the ID of the company the user belongs to
     * @param companyName the name of the company the user belongs to
     */
    public User(String username, String email, String passwordHash, String fullName, 
                String image, String registrationNumber, String phone, String cpf, 
                Long companyId, String companyName) {
        this.username = username;
        this.email = email;
        this.passwordHash = passwordHash;
        this.fullName = fullName;
        this.image = image;
        this.registrationNumber = registrationNumber;
        this.phone = phone;
        this.cpf = cpf;
        this.companyId = companyId;
        this.companyName = companyName;
        this.active = true;
        this.roles = new HashSet<>();
    }

    /**
     * Creates a User with an existing ID.
     *
     * @param id the ID of the user
     * @param username the username of the user
     * @param email the email of the user
     * @param passwordHash the hashed password of the user
     * @param active whether the user is active
     */
    public User(Long id, String username, String email, String passwordHash, boolean active) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.passwordHash = passwordHash;
        this.active = active;
        this.roles = new HashSet<>();
    }
    
    /**
     * Creates a User with an existing ID and full name.
     *
     * @param id the ID of the user
     * @param username the username of the user
     * @param email the email of the user
     * @param passwordHash the hashed password of the user
     * @param fullName the full name of the user
     * @param active whether the user is active
     */
    public User(Long id, String username, String email, String passwordHash, String fullName, boolean active) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.passwordHash = passwordHash;
        this.fullName = fullName;
        this.active = active;
        this.roles = new HashSet<>();
    }
    
    /**
     * Creates a User with core fields and roles.
     *
     * @param id the ID of the user
     * @param username the username of the user
     * @param email the email of the user
     * @param passwordHash the hashed password of the user
     * @param fullName the full name of the user
     * @param active whether the user is active
     * @param roles the roles of the user
     */
    public User(Long id, String username, String email, String passwordHash, 
            String fullName, boolean active, Set<Role> roles) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.passwordHash = passwordHash;
        this.fullName = fullName;
        this.active = active;
        this.roles = roles != null ? new HashSet<>(roles) : new HashSet<>();
    }
    
    /**
     * Creates a User with all fields.
     *
     * @param id the ID of the user
     * @param username the username of the user
     * @param email the email of the user
     * @param passwordHash the hashed password of the user
     * @param fullName the full name of the user
     * @param active whether the user is active
     * @param image the image URL of the user
     * @param registrationNumber the registration number of the user
     * @param phone the phone number of the user
     * @param cpf the CPF (tax ID) of the user
     * @param companyId the ID of the company the user belongs to
     * @param companyName the name of the company the user belongs to
     * @param roles the roles of the user
     */
    public User(Long id, String username, String email, String passwordHash, 
            String fullName, boolean active, String image, String registrationNumber, 
            String phone, String cpf, Long companyId, String companyName, Set<Role> roles) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.passwordHash = passwordHash;
        this.fullName = fullName;
        this.active = active;
        this.image = image;
        this.registrationNumber = registrationNumber;
        this.phone = phone;
        this.cpf = cpf;
        this.companyId = companyId;
        this.companyName = companyName;
        this.roles = roles != null ? new HashSet<>(roles) : new HashSet<>();
    }

    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPasswordHash() {
        return passwordHash;
    }

    public void setPasswordHash(String passwordHash) {
        this.passwordHash = passwordHash;
    }
    
    public String getFullName() {
        return fullName;
    }
    
    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }
    
    public String getImage() {
        return image;
    }
    
    public void setImage(String image) {
        this.image = image;
    }
    
    public String getRegistrationNumber() {
        return registrationNumber;
    }
    
    public void setRegistrationNumber(String registrationNumber) {
        this.registrationNumber = registrationNumber;
    }
    
    public String getPhone() {
        return phone;
    }
    
    public void setPhone(String phone) {
        this.phone = phone;
    }
    
    public String getCpf() {
        return cpf;
    }
    
    public void setCpf(String cpf) {
        this.cpf = cpf;
    }
    
    public Long getCompanyId() {
        return companyId;
    }
    
    public void setCompanyId(Long companyId) {
        this.companyId = companyId;
    }
    
    public String getCompanyName() {
        return companyName;
    }
    
    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public Set<Role> getRoles() {
        return Collections.unmodifiableSet(roles);
    }
    
    public void setRoles(Set<Role> roles) {
        this.roles = roles != null ? new HashSet<>(roles) : new HashSet<>();
    }

    /**
     * Adds a role to the user.
     *
     * @param role the role to add
     * @return true if the role was added, false if it was already present
     */
    public boolean addRole(Role role) {
        return roles.add(role);
    }

    /**
     * Removes a role from the user.
     *
     * @param role the role to remove
     * @return true if the role was removed, false if it was not present
     */
    public boolean removeRole(Role role) {
        return roles.remove(role);
    }

    /**
     * Checks if the user has a specific role.
     *
     * @param roleName the name of the role to check
     * @return true if the user has the role, false otherwise
     */
    public boolean hasRole(String roleName) {
        return roles.stream()
                .anyMatch(role -> role.getName().equals(roleName));
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return Objects.equals(id, user.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
