package com.ocoelhogabriel.manager_user_security.interfaces.web.dtos;

import com.ocoelhogabriel.manager_user_security.infrastructure.adapters.persistence.entities.Recurso;

public class RecursoDTO extends CodigoExtends {

	private String nome;

	private String descricao;

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public String getDescricao() {
		return descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("RecursoDTO [");
		if (nome != null) {
			builder.append("nome=").append(nome).append(", ");
		}
		if (descricao != null) {
			builder.append("descricao=").append(descricao);
		}
		builder.append("]");
		return builder.toString();
	}

	public RecursoDTO(Long codigo, String nome, String descricao) {
		super(codigo);
		this.nome = nome;
		this.descricao = descricao;
	}

	public RecursoDTO(Recurso rec) {
		super();
		this.setCodigo(rec.getId());
		this.setNome(rec.getNome());
		this.setDescricao(rec.getDescricao());

	}

	public RecursoDTO() {
		super();

	}

	public RecursoDTO(Long codigo) {
		super(codigo);

	}

}
