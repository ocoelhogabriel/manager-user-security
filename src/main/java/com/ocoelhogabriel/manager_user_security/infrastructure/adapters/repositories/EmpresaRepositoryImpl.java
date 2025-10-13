package com.ocoelhogabriel.manager_user_security.infrastructure.adapters.repositories;

import com.ocoelhogabriel.manager_user_security.domain.repositories.EmpresaRepository;
import com.ocoelhogabriel.manager_user_security.infrastructure.adapters.persistence.entities.Empresa;
import com.ocoelhogabriel.manager_user_security.domain.entities.EmpresaModel;

import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Implementação do repositório de Empresa seguindo Clean Architecture.
 * Atua como adapter entre a camada de domínio e a infraestrutura JPA.
 */
@Repository
public class EmpresaRepositoryImpl implements EmpresaRepository {

    private final EmpresaJpaRepository jpaRepository;

    public EmpresaRepositoryImpl(EmpresaJpaRepository jpaRepository) {
        this.jpaRepository = jpaRepository;
    }

    @Override
    public EmpresaModel save(EmpresaModel model) {
        Empresa entity = toEntity(model);
        Empresa savedEntity = jpaRepository.save(entity);
        return toModel(savedEntity);
    }

    @Override
    public Optional<EmpresaModel> findById(Long id) {
        return jpaRepository.findById(id)
                .map(this::toModel);
    }

    @Override
    public Optional<EmpresaModel> findByCnpj(String cnpj) {
        // Converte String CNPJ para Long (assumindo que está formatado corretamente)
        try {
            Long cnpjLong = Long.valueOf(cnpj.replaceAll("\\D", "")); // Remove caracteres não numéricos
            return jpaRepository.findByEmpcnp(cnpjLong)
                    .map(this::toModel);
        } catch (NumberFormatException e) {
            return Optional.empty();
        }
    }

    @Override
    public List<EmpresaModel> findByNome(String nome) {
        // Como não temos método específico no JPA repository, buscaremos todas e filtraremos
        return jpaRepository.findAll()
                .stream()
                .filter(empresa -> empresa.getName() != null && 
                                  empresa.getName().toLowerCase().contains(nome.toLowerCase()))
                .map(this::toModel)
                .toList();
    }

    @Override
    public List<EmpresaModel> findAllActive() {
        // Como não temos campo de ativo, retornamos todas as empresas
        return jpaRepository.findAll()
                .stream()
                .map(this::toModel)
                .toList();
    }

    @Override
    public void delete(Long id) {
        jpaRepository.deleteById(id);
    }

    @Override
    public boolean existsByCnpj(String cnpj) {
        try {
            Long cnpjLong = Long.valueOf(cnpj.replaceAll("\\D", ""));
            return jpaRepository.findByEmpcnp(cnpjLong).isPresent();
        } catch (NumberFormatException e) {
            return false;
        }
    }

    @Override
    public boolean existsByNome(String nome) {
        return jpaRepository.findAll()
                .stream()
                .anyMatch(empresa -> empresa.getName() != null && 
                                   empresa.getName().equalsIgnoreCase(nome));
    }

    @Override
    public long countActive() {
        // Como não temos campo de ativo, contamos todas as empresas
        return jpaRepository.count();
    }

    /**
     * Converte entidade JPA para modelo de domínio
     */
    private EmpresaModel toModel(Empresa entity) {
        if (entity == null) {
            return null;
        }

        EmpresaModel model = new EmpresaModel();
        model.setCnpj(entity.getCnpj());
        model.setNome(entity.getName());
        model.setNomeFantasia(entity.getFantasyName());
        model.setTelefone(entity.getPhone());
        
        return model;
    }

    /**
     * Converte modelo de domínio para entidade JPA
     */
    private Empresa toEntity(EmpresaModel model) {
        if (model == null) {
            return null;
        }

        Empresa entity = new Empresa();
        // EmpresaModel não tem ID - será gerado automaticamente pela JPA
        entity.setName(model.getNome());
        entity.setCnpj(model.getCnpj());
        entity.setFantasyName(model.getNomeFantasia());
        entity.setPhone(model.getTelefone());
        
        return entity;
    }
}
