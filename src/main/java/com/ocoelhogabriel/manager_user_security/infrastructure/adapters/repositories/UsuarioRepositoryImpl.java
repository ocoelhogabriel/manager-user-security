package com.ocoelhogabriel.manager_user_security.infrastructure.adapters.repositories;

import java.util.List;
import java.util.Optional;


import org.springframework.stereotype.Repository;

import com.ocoelhogabriel.manager_user_security.domain.repositories.UsuarioRepository;
import com.ocoelhogabriel.manager_user_security.infrastructure.adapters.persistence.entities.Usuario;
import com.ocoelhogabriel.manager_user_security.domain.entities.UsuarioModel;

/**
 * Implementação do repository de Usuario
 * Faz a ponte entre Domain Layer e Infrastructure Layer
 * Aplica Adapter Pattern
 */
@Repository
public class UsuarioRepositoryImpl implements UsuarioRepository {
    
    private final UsuarioJpaRepository jpaRepository;
    
    public UsuarioRepositoryImpl(UsuarioJpaRepository jpaRepository) {
        this.jpaRepository = jpaRepository;
    }
    
    @Override
    public UsuarioModel save(UsuarioModel usuario) {
        Usuario entidade = mapToEntity(usuario);
        Usuario saved = jpaRepository.save(entidade);
        return mapToModel(saved);
    }
    
    @Override
    public Optional<UsuarioModel> findById(Long id) {
        return jpaRepository.findById(id)
                .map(this::mapToModel);
    }
    
    @Override
    public Optional<UsuarioModel> findByLogin(String login) {
        return jpaRepository.findByUsulog(login)
                .map(this::mapToModel);
    }
    
    @Override
    public Optional<UsuarioModel> findByEmail(String email) {
        // Implementação simplificada - na realidade seria um método específico no JPA
        return jpaRepository.findAll().stream()
                .filter(u -> email.equals(u.getEmail()))
                .map(this::mapToModel)
                .findFirst();
    }
    
    @Override
    public List<UsuarioModel> findAllActive() {
        // Implementação simplificada - assumindo que todos são ativos
        return jpaRepository.findAll().stream()
                .map(this::mapToModel)
                .toList();
    }
    
    @Override
    public void delete(Long id) {
        jpaRepository.deleteById(id);
    }
    
    @Override
    public boolean existsByLogin(String login) {
        return jpaRepository.findByUsulog(login).isPresent();
    }
    
    @Override
    public boolean existsByEmail(String email) {
        return findByEmail(email).isPresent();
    }
    
    @Override
    public long countActive() {
        return findAllActive().size();
    }
    
    @Override
    public void activate(Long id) {
        // Implementação futura - campo ativo não existe na entidade atual
        throw new UnsupportedOperationException("Campo ativo não implementado na entidade Usuario");
    }
    
    @Override
    public void deactivate(Long id) {
        // Implementação futura - campo ativo não existe na entidade atual
        throw new UnsupportedOperationException("Campo ativo não implementado na entidade Usuario");
    }
    
    @Override
    public void changePassword(Long id, String newPassword) {
        jpaRepository.findById(id).ifPresent(usuario -> {
            usuario.setPassword(newPassword);
            jpaRepository.save(usuario);
        });
    }
    
    /**
     * Converte Entity para Model
     */
    private UsuarioModel mapToModel(Usuario entity) {
        UsuarioModel model = new UsuarioModel();
        model.setNome(entity.getNome());
        model.setCpf(entity.getCpf());
        model.setLogin(entity.getUsername());
        model.setSenha(entity.getPassword());
        model.setEmail(entity.getEmail());
        // Mapear outros campos conforme necessário
        return model;
    }
    
    /**
     * Converte Model para Entity
     */
    private Usuario mapToEntity(UsuarioModel model) {
        Usuario entity = new Usuario();
        entity.setNome(model.getNome());
        entity.setCpf(model.getCpf());
        entity.setUsername(model.getLogin());
        entity.setPassword(model.getSenha());
        entity.setEmail(model.getEmail());
        // Mapear outros campos conforme necessário
        return entity;
    }
}
