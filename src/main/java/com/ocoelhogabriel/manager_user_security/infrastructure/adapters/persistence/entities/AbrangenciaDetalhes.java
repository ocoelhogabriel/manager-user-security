package com.ocoelhogabriel.manager_user_security.infrastructure.adapters.persistence.entities;

import jakarta.persistence.*;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

@Entity
@Table(name = "abrangencia_detalhes")
public class AbrangenciaDetalhes {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "abrangencia_id", nullable = false)
    private Abrangencia abrangencia;

    @ManyToOne
    @JoinColumn(name = "recurso_id", nullable = false)
    private Recurso recurso;

    @Column(name = "hierarquia", nullable = false)
    private Integer hierarquia;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(nullable = false, columnDefinition = "jsonb")
    private String dados;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Abrangencia getAbrangencia() {
        return abrangencia;
    }

    public void setAbrangencia(Abrangencia abrangencia) {
        this.abrangencia = abrangencia;
    }

    public Recurso getRecurso() {
        return recurso;
    }

    public void setRecurso(Recurso recurso) {
        this.recurso = recurso;
    }

    public Integer getHierarquia() {
        return hierarquia;
    }

    public void setHierarquia(Integer hierarquia) {
        this.hierarquia = hierarquia;
    }

    public String getDados() {
        return dados;
    }

    public void setDados(String dados) {
        this.dados = dados;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("AbrangenciaDetalhes [");
        if (id != null) {
            builder.append("id=").append(id).append(", ");
        }
        if (abrangencia != null) {
            builder.append("abrangencia=").append(abrangencia).append(", ");
        }
        if (recurso != null) {
            builder.append("reccod=").append(recurso).append(", ");
        }
        if (hierarquia != null) {
            builder.append("hierarquia=").append(hierarquia).append(", ");
        }
        if (dados != null) {
            builder.append("dados=").append(dados);
        }
        builder.append("]");
        return builder.toString();
    }

    public AbrangenciaDetalhes(Long id, Abrangencia abrangencia, Recurso recurso, Integer hierarquia, String dados) {
        this.id = id;
        this.abrangencia = abrangencia;
        this.recurso = recurso;
        this.hierarquia = hierarquia;
        this.dados = dados;
    }

    public AbrangenciaDetalhes() {
        super();

    }

}
