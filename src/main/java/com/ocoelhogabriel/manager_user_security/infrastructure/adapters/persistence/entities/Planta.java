package com.ocoelhogabriel.manager_user_security.infrastructure.adapters.persistence.entities;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.Objects;

/**
 * Entidade JPA para Planta
 * Aplica Object Calisthenics - Regra 5: Use only one dot per line
 * Segue SOLID principles - Single Responsibility Principle
 */
@Entity
@Table(name = "planta")
public final class Planta {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "nome", nullable = false, length = 200)
    private String nome;
    
    @Column(name = "codigo", unique = true, nullable = false, length = 50)
    private String codigo;
    
    @Column(name = "descricao", length = 500)
    private String descricao;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "empresa_id", nullable = false)
    private Empresa empresa;
    
    @Column(name = "endereco", length = 500)
    private String endereco;
    
    @Column(name = "cidade", length = 100)
    private String cidade;
    
    @Column(name = "estado", length = 2)
    private String estado;
    
    @Column(name = "cep", length = 10)
    private String cep;
    
    @Column(name = "latitude")
    private Double latitude;
    
    @Column(name = "longitude")
    private Double longitude;
    
    @Column(name = "capacidade_total")
    private Double capacidadeTotal;
    
    @Column(name = "ativo", nullable = false)
    private Boolean ativo = true;
    
    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt = LocalDateTime.now();
    
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
    
    // Construtor padrão para JPA
    protected Planta() {}
    
    public Planta(final String nome, final String codigo, final Empresa empresa) {
        this.nome = Objects.requireNonNull(nome, "Nome não pode ser nulo");
        this.codigo = Objects.requireNonNull(codigo, "Código não pode ser nulo");
        this.empresa = Objects.requireNonNull(empresa, "Empresa não pode ser nula");
        this.ativo = true;
        this.createdAt = LocalDateTime.now();
    }
    
    // Getters
    public Long getId() { return this.id; }
    public String getNome() { return this.nome; }
    public String getCodigo() { return this.codigo; }
    public String getDescricao() { return this.descricao; }
    public Empresa getEmpresa() { return this.empresa; }
    public String getEndereco() { return this.endereco; }
    public String getCidade() { return this.cidade; }
    public String getEstado() { return this.estado; }
    public String getCep() { return this.cep; }
    public Double getLatitude() { return this.latitude; }
    public Double getLongitude() { return this.longitude; }
    public Double getCapacidadeTotal() { return this.capacidadeTotal; }
    public Boolean getAtivo() { return this.ativo; }
    public LocalDateTime getCreatedAt() { return this.createdAt; }
    public LocalDateTime getUpdatedAt() { return this.updatedAt; }
    
    // Setters para propriedades opcionais
    public void setDescricao(final String descricao) {
        this.descricao = descricao;
        this.updatedAt = LocalDateTime.now();
    }
    
    public void setEndereco(final String endereco) {
        this.endereco = endereco;
        this.updatedAt = LocalDateTime.now();
    }
    
    public void setCidade(final String cidade) {
        this.cidade = cidade;
        this.updatedAt = LocalDateTime.now();
    }
    
    public void setEstado(final String estado) {
        this.estado = estado;
        this.updatedAt = LocalDateTime.now();
    }
    
    public void setCep(final String cep) {
        this.cep = cep;
        this.updatedAt = LocalDateTime.now();
    }
    
    public void setCoordenadas(final Double latitude, final Double longitude) {
        this.latitude = latitude;
        this.longitude = longitude;
        this.updatedAt = LocalDateTime.now();
    }
    
    public void setCapacidadeTotal(final Double capacidadeTotal) {
        this.capacidadeTotal = capacidadeTotal;
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
    
    public boolean temCoordenadas() {
        return this.latitude != null && this.longitude != null;
    }
    
    public boolean pertenceEmpresa(final Long empresaId) {
        return this.empresa != null && Objects.equals(this.empresa.getId(), empresaId);
    }
    
    @Override
    public boolean equals(final Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        final Planta planta = (Planta) obj;
        return Objects.equals(this.id, planta.id);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }
    
    @Override
    public String toString() {
        return String.format("Planta{id=%d, nome='%s', codigo='%s', ativo=%s}", 
                           this.id, this.nome, this.codigo, this.ativo);
    }
}
