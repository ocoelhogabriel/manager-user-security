package com.ocoelhogabriel.manager_user_security.model.entity;

import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "abrangencia_detalhes")
public class AbrangenciaDetalhes {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "abdcod", nullable = false)
	private Long abdcod;

	@ManyToOne
	@JoinColumn(name = "abrcod", nullable = false)
	private Abrangencia abrangencia;

	@ManyToOne
	@JoinColumn(name = "reccod", nullable = false)
	private Recurso recurso;

	@Column(name = "abdhie", nullable = false)
	private Integer abdhie;

	@JdbcTypeCode(SqlTypes.JSON)
	@Column(nullable = false, columnDefinition = "jsonb")
	private String abddat;

	public Long getAbdcod() {
		return abdcod;
	}

	public void setAbdcod(Long abdcod) {
		this.abdcod = abdcod;
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

	public void setReccod(Recurso recnom) {
		this.recurso = recnom;
	}

	public Integer getAbdhie() {
		return abdhie;
	}

	public void setAbdhie(Integer abdhie) {
		this.abdhie = abdhie;
	}

	public String getAbddat() {
		return abddat;
	}

	public void setAbddat(String abddat) {
		this.abddat = abddat;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("AbrangenciaDetalhes [");
		if (abdcod != null) {
			builder.append("abdcod=").append(abdcod).append(", ");
		}
		if (abrangencia != null) {
			builder.append("abrangencia=").append(abrangencia).append(", ");
		}
		if (recurso != null) {
			builder.append("reccod=").append(recurso).append(", ");
		}
		if (abdhie != null) {
			builder.append("abdhie=").append(abdhie).append(", ");
		}
		if (abddat != null) {
			builder.append("abddat=").append(abddat);
		}
		builder.append("]");
		return builder.toString();
	}

	public AbrangenciaDetalhes(Long abdcod, Abrangencia abrangencia, Recurso recurso, Integer abdhie, String abddat) {
		this.abdcod = abdcod;
		this.abrangencia = abrangencia;
		this.recurso = recurso;
		this.abdhie = abdhie;
		this.abddat = abddat;
	}

	public AbrangenciaDetalhes() {
		super();

	}

}
