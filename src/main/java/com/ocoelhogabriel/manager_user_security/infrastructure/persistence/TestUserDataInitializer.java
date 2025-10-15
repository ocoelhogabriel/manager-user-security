package com.ocoelhogabriel.manager_user_security.infrastructure.persistence;

import com.ocoelhogabriel.manager_user_security.infrastructure.persistence.entity.UserEntity;
import com.ocoelhogabriel.manager_user_security.infrastructure.persistence.entity.PermissionEntity;
import com.ocoelhogabriel.manager_user_security.infrastructure.persistence.entity.RoleEntity;
import com.ocoelhogabriel.manager_user_security.infrastructure.persistence.entity.ResourceEntity;
import com.ocoelhogabriel.manager_user_security.infrastructure.persistence.repository.UserJpaRepository;
import com.ocoelhogabriel.manager_user_security.infrastructure.persistence.repository.PermissionJpaRepository;
import com.ocoelhogabriel.manager_user_security.infrastructure.persistence.repository.RoleJpaRepository;
import com.ocoelhogabriel.manager_user_security.infrastructure.persistence.repository.ResourceJpaRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.beans.factory.annotation.Autowired;

@Component
public class TestUserDataInitializer implements CommandLineRunner {
    @Autowired
    private UserJpaRepository userRepository;
    @Autowired
    private PermissionJpaRepository permissionRepository;
    @Autowired
    private RoleJpaRepository roleRepository;
    @Autowired
    private ResourceJpaRepository resourceRepository;

    @Override
    public void run(String... args) throws Exception {
        // Criação de permissão de teste
        PermissionEntity permission = new PermissionEntity();
        permission.setName("TEST_PERMISSION");
        permission.setDescription("Permissão de teste");
        // Criação de recurso de teste
        ResourceEntity resource = new ResourceEntity();
        resource.setName("TEST_RESOURCE");
        resource.setDescription("Recurso de teste");
        resource = resourceRepository.save(resource);
        permission.setResource(resource); // Corrigido: associar recurso à permissão
        permission = permissionRepository.save(permission);
        // Criação de papel de teste
        RoleEntity role = new RoleEntity();
        role.setName("TEST_ROLE");
        role.setDescription("Papel de teste");
        role.getPermissions().add(permission);
        role = roleRepository.save(role);
        // Criação de usuário de teste
        UserEntity user = new UserEntity();
        user.setUsername("testuser");
        user.setPassword("testpassword"); // Ideal: criptografar
        user.setEmail("testuser@example.com");
        user.getRoles().add(role);
        user = userRepository.save(user);
    }
}
