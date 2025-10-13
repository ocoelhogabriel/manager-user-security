package com.ocoelhogabriel.manager_user_security.model.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "permissao")
public class Permissao {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "pemcod", nullable = false)
	private Long pemcod;

	@ManyToOne
	@JoinColumn(name = "percod", nullable = false)
	private Perfil perfil;

	@ManyToOne
	@JoinColumn(name = "reccod", nullable = false)
	private Recurso recurso;

	@Column(name = "pemlis", nullable = false)
	private Integer pemlis;

	@Column(name = "pembus", nullable = false)
	private Integer pembus;

	@Column(name = "pemcri", nullable = false)
	private Integer pemcri;

	@Column(name = "pemedi", nullable = false)
	private Integer pemedi;

	@Column(name = "pemdel", nullable = false)
	private Integer pemdel;

	public Long getPemcod() {
		return pemcod;
	}

	public void setPemcod(Long pemcod) {
		this.pemcod = pemcod;
	}

	public Perfil getPerfil() {
		return perfil;
	}

	public void setPerfil(Perfil perfil) {
		this.perfil = perfil;
	}

	public Recurso getRecurso() {
		return recurso;
	}

	public void setReccod(Recurso recnom) {
		this.recurso = recnom;
	}

	public Integer getPemlis() {
		return pemlis;
	}

	public void setPemlis(Integer pemlis) {
		this.pemlis = pemlis;
	}

	public Integer getPembus() {
		return pembus;
	}

	public void setPembus(Integer pembus) {
		this.pembus = pembus;
	}

	public Integer getPemcri() {
		return pemcri;
	}

	public void setPemcri(Integer pemcri) {
		this.pemcri = pemcri;
	}

	public Integer getPemedi() {
		return pemedi;
	}

	public void setPemedi(Integer pemedi) {
		this.pemedi = pemedi;
	}

	public Integer getPemdel() {
		return pemdel;
	}

	public void setPemdel(Integer pemdel) {
		this.pemdel = pemdel;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("Permissao [");
		if (pemcod != null) {
			builder.append("pemcod=").append(pemcod).append(", ");
		}
		if (perfil != null) {
			builder.append("perfil=").append(perfil).append(", ");
		}
		if (recurso != null) {
			builder.append("recnom=").append(recurso).append(", ");
		}
		if (pemlis != null) {
			builder.append("pemlis=").append(pemlis).append(", ");
		}
		if (pembus != null) {
			builder.append("pembus=").append(pembus).append(", ");
		}
		if (pemcri != null) {
			builder.append("pemcri=").append(pemcri).append(", ");
		}
		if (pemedi != null) {
			builder.append("pemedi=").append(pemedi).append(", ");
		}
		if (pemdel != null) {
			builder.append("pemdel=").append(pemdel);
		}
		builder.append("]");
		return builder.toString();
	}

	public Permissao(Long pemcod, Perfil perfil, Recurso recurso, Integer pemlis, Integer pembus, Integer pemcri, Integer pemedi, Integer pemdel) {
		super();
		this.pemcod = pemcod;
		this.perfil = perfil;
		this.recurso = recurso;
		this.pemlis = pemlis;
		this.pembus = pembus;
		this.pemcri = pemcri;
		this.pemedi = pemedi;
		this.pemdel = pemdel;
	}

	public Permissao() {
		super();

	}

}
