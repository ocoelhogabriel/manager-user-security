package com.ocoelhogabriel.manager_user_security.infrastructure.persistence;

import com.ocoelhogabriel.manager_user_security.domain.model.Empresa;
import com.ocoelhogabriel.manager_user_security.domain.model.value_objects.CNPJ;
import com.ocoelhogabriel.manager_user_security.domain.model.value_objects.EmpresaId;
import com.ocoelhogabriel.manager_user_security.domain.model.value_objects.Name;
import com.ocoelhogabriel.manager_user_security.domain.ports.out.EmpresaRepository;
import com.ocoelhogabriel.manager_user_security.domain.ports.out.UserRepository.PageQuery;
import com.ocoelhogabriel.manager_user_security.domain.ports.out.UserRepository.PagedResult;
import com.ocoelhogabriel.manager_user_security.infrastructure.adapters.repositories.EmpresaJpaRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Driven Adapter that implements the EmpresaRepository port.
 */
@Repository
public class EmpresaRepositoryImpl implements EmpresaRepository {

    private final EmpresaJpaRepository jpaRepository;

    public EmpresaRepositoryImpl(final EmpresaJpaRepository jpaRepository) {
        this.jpaRepository = Objects.requireNonNull(jpaRepository);
    }

    @Override
    public Empresa save(Empresa empresa) {
        final com.ocoelhogabriel.manager_user_security.infrastructure.adapters.persistence.entities.Empresa empresaEntity;
        if (empresa.hasId()) {
            empresaEntity = jpaRepository.findById(empresa.id().value())
                .orElseThrow(() -> new IllegalStateException("Cannot update a non-existent company with ID: " + empresa.id().value()));
        } else {
            empresaEntity = new com.ocoelhogabriel.manager_user_security.infrastructure.adapters.persistence.entities.Empresa();
        }
        EmpresaMapper.mapDomainToEntity(empresa, empresaEntity);
        final com.ocoelhogabriel.manager_user_security.infrastructure.adapters.persistence.entities.Empresa savedEntity = jpaRepository.save(empresaEntity);
        return EmpresaMapper.toDomain(savedEntity);
    }

    @Override
    public Optional<Empresa> findById(EmpresaId id) {
        return jpaRepository.findById(id.value()).map(EmpresaMapper::toDomain);
    }

    @Override
    public PagedResult<Empresa> findAll(PageQuery query) {
        PageRequest pageRequest = PageRequest.of(query.page(), query.size());
        Page<com.ocoelhogabriel.manager_user_security.infrastructure.adapters.persistence.entities.Empresa> page = jpaRepository.findAll(pageRequest);
        List<Empresa> empresas = page.getContent().stream()
            .map(EmpresaMapper::toDomain)
            .collect(Collectors.toList());
        return new PagedResult<>(empresas, page.getTotalElements());
    }

    @Override
    public boolean existsByCnpj(CNPJ cnpj) {
        return jpaRepository.existsByCnpj(Long.valueOf(cnpj.value()));
    }

    private static class EmpresaMapper {

        public static Empresa toDomain(com.ocoelhogabriel.manager_user_security.infrastructure.adapters.persistence.entities.Empresa entity) {
            if (entity == null) return null;
            return Empresa.builder()
                .id(new EmpresaId(entity.getId()))
                .cnpj(new CNPJ(String.valueOf(entity.getCnpj())))
                .name(new Name(entity.getName()))
                .fantasyName(entity.getFantasyName())
                .phone(entity.getPhone())
                .active(true) // Assuming active, as the JPA entity doesn't have this field
                .createdAt(LocalDateTime.now()) // Placeholder, JPA entity doesn't have this
                .updatedAt(LocalDateTime.now()) // Placeholder, JPA entity doesn't have this
                .build();
        }

        public static void mapDomainToEntity(Empresa domain, com.ocoelhogabriel.manager_user_security.infrastructure.adapters.persistence.entities.Empresa entity) {
            entity.setCnpj(Long.valueOf(domain.cnpj().value()));
            entity.setName(domain.name().value());
            entity.setFantasyName(domain.fantasyName());
            entity.setPhone(domain.phone());
        }
    }
}
