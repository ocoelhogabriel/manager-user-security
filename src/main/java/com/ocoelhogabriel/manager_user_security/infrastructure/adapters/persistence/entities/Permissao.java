package com.ocoelhogabriel.manager_user_security.infrastructure.adapters.persistence.entities;

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
	@Column(name = "id", nullable = false)
	private Long id;

	@ManyToOne
	@JoinColumn(name = "perfil_id", nullable = false)
	private Perfil perfil;

	@ManyToOne
	@JoinColumn(name = "recurso_id", nullable = false)
	private Recurso recurso;

	@Column(name = "listar", nullable = false)
	private Boolean listar;

	@Column(name = "buscar", nullable = false)
	private Boolean buscar;

	@Column(name = "criar", nullable = false)
	private Boolean criar;

	@Column(name = "editar", nullable = false)
	private Boolean editar;

	@Column(name = "deletar", nullable = false)
	private Boolean deletar;

	public Permissao(Boolean buscar, Boolean criar, Boolean deletar, Boolean editar, Long id, Boolean listar,
			Perfil perfil, Recurso recurso) {
		this.buscar = buscar;
		this.criar = criar;
		this.deletar = deletar;
		this.editar = editar;
		this.id = id;
		this.listar = listar;
		this.perfil = perfil;
		this.recurso = recurso;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("Permissao [id=").append(id).append(", perfil=").append(perfil).append(", recurso=")
				.append(recurso).append(", listar=").append(listar).append(", buscar=").append(buscar)
				.append(", criar=").append(criar).append(", editar=").append(editar).append(", deletar=")
				.append(deletar).append("]");
		return builder.toString();
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
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

	public void setRecurso(Recurso recurso) {
		this.recurso = recurso;
	}

	public Boolean getListar() {
		return listar;
	}

	public void setListar(Boolean listar) {
		this.listar = listar;
	}

	public Boolean getBuscar() {
		return buscar;
	}

	public void setBuscar(Boolean buscar) {
		this.buscar = buscar;
	}

	public Boolean getCriar() {
		return criar;
	}

	public void setCriar(Boolean criar) {
		this.criar = criar;
	}

	public Boolean getEditar() {
		return editar;
	}

	public void setEditar(Boolean editar) {
		this.editar = editar;
	}

	public Boolean getDeletar() {
		return deletar;
	}

	public void setDeletar(Boolean deletar) {
		this.deletar = deletar;
	}

	public Permissao() {
		super();

	}

}
