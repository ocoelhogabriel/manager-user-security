package com.ocoelhogabriel.manager_user_security.infrastructure.persistence;

import com.ocoelhogabriel.manager_user_security.domain.model.Perfil;
import com.ocoelhogabriel.manager_user_security.domain.model.value_objects.Name;
import com.ocoelhogabriel.manager_user_security.domain.model.value_objects.NivelAcesso;
import com.ocoelhogabriel.manager_user_security.domain.model.value_objects.PerfilId;
import com.ocoelhogabriel.manager_user_security.domain.ports.out.PerfilRepository;
import com.ocoelhogabriel.manager_user_security.domain.ports.out.UserRepository.PageQuery;
import com.ocoelhogabriel.manager_user_security.domain.ports.out.UserRepository.PagedResult;
import com.ocoelhogabriel.manager_user_security.infrastructure.adapters.repositories.PerfilJpaRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Driven Adapter that implements the PerfilRepository port.
 */
@Repository
public class PerfilRepositoryImpl implements PerfilRepository {

    private final PerfilJpaRepository jpaRepository;

    public PerfilRepositoryImpl(final PerfilJpaRepository jpaRepository) {
        this.jpaRepository = Objects.requireNonNull(jpaRepository);
    }

    @Override
    public Perfil save(Perfil perfil) {
        final com.ocoelhogabriel.manager_user_security.infrastructure.adapters.persistence.entities.Perfil perfilEntity;
        if (perfil.hasId()) {
            perfilEntity = jpaRepository.findById(perfil.id().value())
                .orElseThrow(() -> new IllegalStateException("Cannot update a non-existent profile with ID: " + perfil.id().value()));
        } else {
            perfilEntity = new com.ocoelhogabriel.manager_user_security.infrastructure.adapters.persistence.entities.Perfil();
        }
        PerfilMapper.mapDomainToEntity(perfil, perfilEntity);
        final com.ocoelhogabriel.manager_user_security.infrastructure.adapters.persistence.entities.Perfil savedEntity = jpaRepository.save(perfilEntity);
        return PerfilMapper.toDomain(savedEntity);
    }

    @Override
    public Optional<Perfil> findById(PerfilId id) {
        return jpaRepository.findById(id.value()).map(PerfilMapper::toDomain);
    }

    @Override
    public PagedResult<Perfil> findAllActive(PageQuery query) {
        PageRequest pageRequest = PageRequest.of(query.page(), query.size());
        Page<com.ocoelhogabriel.manager_user_security.infrastructure.adapters.persistence.entities.Perfil> page = jpaRepository.findByAtivo(true, pageRequest);
        List<Perfil> perfis = page.getContent().stream()
            .map(PerfilMapper::toDomain)
            .collect(Collectors.toList());
        return new PagedResult<>(perfis, page.getTotalElements());
    }

    @Override
    public boolean existsByNome(Name nome) {
        return jpaRepository.existsByNome(nome.value());
    }

    private static class PerfilMapper {

        public static Perfil toDomain(com.ocoelhogabriel.manager_user_security.infrastructure.adapters.persistence.entities.Perfil entity) {
            if (entity == null) return null;
            return Perfil.builder()
                .id(new PerfilId(entity.getId()))
                .nome(new Name(entity.getNome()))
                .descricao(entity.getDescricao())
                .nivelAcesso(new NivelAcesso(entity.getNivelAcesso()))
                .active(entity.getAtivo())
                .createdAt(entity.getCreatedAt())
                .updatedAt(entity.getUpdatedAt())
                .build();
        }

        public static void mapDomainToEntity(Perfil domain, com.ocoelhogabriel.manager_user_security.infrastructure.adapters.persistence.entities.Perfil entity) {
            entity.setNome(domain.nome().value());
            entity.setDescricao(domain.descricao());
            entity.setNivelAcesso(domain.nivelAcesso().value());
            entity.setAtivo(domain.isActive());
            if (!domain.hasId()) { // Creation
                entity.setCreatedAt(domain.createdAt());
            }
            entity.setUpdatedAt(domain.updatedAt());
        }
    }
}
