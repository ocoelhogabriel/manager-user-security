package com.ocoelhogabriel.manager_user_security.infrastructure.adapters.repositories;

import org.springframework.stereotype.Repository;

import com.ocoelhogabriel.manager_user_security.domain.entities.PerfilModel;
import com.ocoelhogabriel.manager_user_security.infrastructure.adapters.persistence.entities.Perfil;



@Repository
public class PerfilRepositoryImpl {
    
    private final PerfilRepository perfilRepository;

    public PerfilRepositoryImpl(PerfilRepository perfilRepository) {
        this.perfilRepository = perfilRepository;
    }

    public PerfilModel findByNome(String nome) {
        return perfilRepository.findByPernom(nome)
            .map(this::mapToModel)
            .orElse(null);
    }
    
    public PerfilModel save(PerfilModel perfilModel) {
        Perfil perfil = mapToEntity(perfilModel);
        Perfil savedPerfil = perfilRepository.save(perfil);
        return mapToModel(savedPerfil);
    }
    
    private PerfilModel mapToModel(Perfil perfil) {
        PerfilModel model = new PerfilModel();
        model.setNome(perfil.getNome());
        model.setDescricao(perfil.getDescricao());
        return model;
    }
    
    private Perfil mapToEntity(PerfilModel model) {
        Perfil perfil = new Perfil();
        perfil.atualizarNome(model.getNome());
        perfil.setDescricao(model.getDescricao());
        return perfil;
    }
}