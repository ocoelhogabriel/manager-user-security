package com.ocoelhogabriel.manager_user_security.infrastructure.adapters.persistence.entities;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.Objects;

/**
 * Entidade JPA para Perfil
 * Aplica Object Calisthenics - Regra 5: Use only one dot per line
 * Segue SOLID principles - Single Responsibility Principle
 */
@Entity
@Table(name = "perfil")
public final class Perfil {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "nome", nullable = false, unique = true, length = 100)
    private String nome;

    @Column(name = "descricao", length = 500)
    private String descricao;

    @Column(name = "nivel_acesso", nullable = false)
    private Integer nivelAcesso;

    @Column(name = "ativo", nullable = false)
    private Boolean ativo = true;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    // Construtor padrão para JPA
    public Perfil() {}

    public Perfil(final String nome, final Integer nivelAcesso) {
        this.nome = Objects.requireNonNull(nome, "Nome não pode ser nulo");
        this.nivelAcesso = Objects.requireNonNull(nivelAcesso, "Nível de acesso não pode ser nulo");

        if (nivelAcesso < 1 || nivelAcesso > 10) {
            throw new IllegalArgumentException("Nível de acesso deve estar entre 1 e 10");
        }

        this.ativo = true;
        this.createdAt = LocalDateTime.now();
    }

    // Getters
    public Long getId() { return this.id; }
    public String getNome() { return this.nome; }
    public String getDescricao() { return this.descricao; }
    public Integer getNivelAcesso() { return this.nivelAcesso; }
    public Boolean getAtivo() { return this.ativo; }
    public LocalDateTime getCreatedAt() { return this.createdAt; }
    public LocalDateTime getUpdatedAt() { return this.updatedAt; }

    // Setter for ID to create references
    public void setId(Long id) { this.id = id; }

    // Setters para propriedades opcionais
    public void setDescricao(final String descricao) {
        this.descricao = descricao;
        this.updatedAt = LocalDateTime.now();
    }

    // Métodos de negócio
    public void desativar() {
        this.ativo = false;
        this.updatedAt = LocalDateTime.now();
    }

    public void ativar() {
        this.ativo = true;
        this.updatedAt = LocalDateTime.now();
    }

    public void atualizarNome(final String novoNome) {
        this.nome = Objects.requireNonNull(novoNome, "Nome não pode ser nulo");
        this.updatedAt = LocalDateTime.now();
    }

    public void atualizarNivelAcesso(final Integer novoNivel) {
        Objects.requireNonNull(novoNivel, "Nível de acesso não pode ser nulo");

        if (novoNivel < 1 || novoNivel > 10) {
            throw new IllegalArgumentException("Nível de acesso deve estar entre 1 e 10");
        }

        this.nivelAcesso = novoNivel;
        this.updatedAt = LocalDateTime.now();
    }

    public boolean temNivelSuperiorOuIgual(final Integer nivelComparado) {
        return this.nivelAcesso >= nivelComparado;
    }

    @Override
    public boolean equals(final Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        final Perfil perfil = (Perfil) obj;
        return Objects.equals(this.id, perfil.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    @Override
    public String toString() {
        return String.format("Perfil{id=%d, nome='%s', nivelAcesso=%d, ativo=%s}",
                           this.id, this.nome, this.nivelAcesso, this.ativo);
    }
}
