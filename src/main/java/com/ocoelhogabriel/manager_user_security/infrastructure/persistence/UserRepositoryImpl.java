package com.ocoelhogabriel.manager_user_security.infrastructure.persistence;

import com.ocoelhogabriel.manager_user_security.domain.model.User;
import com.ocoelhogabriel.manager_user_security.domain.model.value_objects.*;
import com.ocoelhogabriel.manager_user_security.domain.ports.out.UserRepository;
import com.ocoelhogabriel.manager_user_security.infrastructure.adapters.persistence.entities.Abrangencia;
import com.ocoelhogabriel.manager_user_security.infrastructure.adapters.persistence.entities.Empresa;
import com.ocoelhogabriel.manager_user_security.infrastructure.adapters.persistence.entities.Perfil;
import com.ocoelhogabriel.manager_user_security.infrastructure.adapters.persistence.entities.Usuario;
import com.ocoelhogabriel.manager_user_security.infrastructure.adapters.repositories.UsuarioJpaRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Driven Adapter that implements the UserRepository port.
 * This class adapts the domain's User entity to the persistence layer's Usuario JPA entity.
 */
@Repository("userRepositoryAdapter")
public class UserRepositoryImpl implements UserRepository {

    private final UsuarioJpaRepository jpaRepository;

    public UserRepositoryImpl(final UsuarioJpaRepository jpaRepository) {
        this.jpaRepository = Objects.requireNonNull(jpaRepository);
    }

    @Override
    public User save(final User user) {
        final Usuario usuarioEntity;
        if (user.hasId()) {
            // UPDATE PATH
            usuarioEntity = jpaRepository.findById(user.id().value())
                .orElseThrow(() -> new IllegalStateException("Cannot update a non-existent user with ID: " + user.id().value()));
        } else {
            // CREATION PATH
            usuarioEntity = new Usuario();
        }
        UserMapper.mapDomainToEntity(user, usuarioEntity);
        final Usuario savedEntity = jpaRepository.save(usuarioEntity);
        return UserMapper.toDomain(savedEntity);
    }

    @Override
    public Optional<User> findById(final UserId id) {
        return jpaRepository.findById(id.value()).map(UserMapper::toDomain);
    }

    @Override
    public Optional<User> findByUsername(final Username username) {
        return jpaRepository.findByUsulog(username.value()).map(UserMapper::toDomain);
    }

    @Override
    public Optional<User> findByEmail(final Email email) {
        return jpaRepository.findByEmail(email.value()).map(UserMapper::toDomain);
    }

    @Override
    public PagedResult<User> findAllActive(final PageQuery query) {
        final PageRequest pageRequest = PageRequest.of(query.page(), query.size());
        final Page<Usuario> page = jpaRepository.findByAtivo(true, pageRequest);

        final List<User> users = page.getContent().stream()
            .map(UserMapper::toDomain)
            .collect(Collectors.toList());

        return new PagedResult<>(users, page.getTotalElements());
    }

    @Override
    public boolean existsByUsername(final Username username) {
        return jpaRepository.findByUsulog(username.value()).isPresent();
    }

    @Override
    public boolean existsByEmail(final Email email) {
        return jpaRepository.existsByEmail(email.value());
    }

    @Override
    public boolean existsByCpf(final CPF cpf) {
        return jpaRepository.existsByCpf(Long.valueOf(cpf.value()));
    }

    @Override
    public long countActive() {
        return jpaRepository.countByAtivo(true);
    }

    private static class UserMapper {

        public static User toDomain(final Usuario entity) {
            if (entity == null) return null;
            return User.builder()
                .id(new UserId(entity.getId()))
                .name(new Name(entity.getNome()))
                .cpf(new CPF(String.valueOf(entity.getCpf())))
                .username(new Username(entity.getUsername()))
                .email(new Email(entity.getEmail()))
                .password(new HashedPassword(entity.getPassword()))
                .empresaId(entity.getEmpresa() != null ? new EmpresaId(entity.getEmpresa().getId()) : null)
                .perfilId(entity.getPerfil() != null ? new PerfilId(entity.getPerfil().getId()) : null)
                .abrangenciaId(entity.getAbrangencia() != null ? new AbrangenciaId(entity.getAbrangencia().getId()) : null)
                .active(entity.isAtivo())
                .createdAt(entity.getCreatedAt())
                .updatedAt(entity.getUpdatedAt())
                .build();
        }

        public static void mapDomainToEntity(final User domain, final Usuario entity) {
            entity.setNome(domain.name().value());
            entity.setCpf(Long.valueOf(domain.cpf().value()));
            entity.setUsername(domain.username().value());
            entity.setEmail(domain.email().value());
            entity.setPassword(domain.password().value());
            entity.setAtivo(domain.isActive());

            if (domain.empresaId() != null) {
                Empresa empresaReference = new Empresa();
                empresaReference.setId(domain.empresaId().value());
                entity.setEmpresa(empresaReference);
            }

            if (domain.perfilId() != null) {
                Perfil perfilReference = new Perfil();
                perfilReference.setId(domain.perfilId().value());
                entity.setPerfil(perfilReference);
            }

            if (domain.abrangenciaId() != null) {
                Abrangencia abrangenciaReference = new Abrangencia();
                abrangenciaReference.setId(domain.abrangenciaId().value());
                entity.setAbrangencia(abrangenciaReference);
            }

            if (!domain.hasId()) { // Creation
                entity.setCreatedAt(domain.createdAt());
            }
            entity.setUpdatedAt(domain.updatedAt());
        }
    }
}
