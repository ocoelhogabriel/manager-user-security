package com.ocoelhogabriel.manager_user_security.interfaces.web.dtos;

import com.ocoelhogabriel.manager_user_security.infrastructure.adapters.persistence.entities.Perfil;

public final class PerfilDTO extends CodigoExtends {

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
		builder.append("PerfilDTO [");
		if (nome != null) {
			builder.append("nome=").append(nome).append(", ");
		}
		if (descricao != null) {
			builder.append("descricao=").append(descricao);
		}
		builder.append("]");
		return builder.toString();
	}

	public PerfilDTO(Perfil per) {
		super();
		this.setCodigo(per.getId());
		this.setNome(per.getNome());
		this.setDescricao(per.getDescricao());

	}

	public PerfilDTO(Long codigo, String nome, String descricao) {
		super(codigo);
		this.nome = nome;
		this.descricao = descricao;
	}

	public PerfilDTO() {
		super();

	}

	public PerfilDTO(Long codigo) {
		super(codigo);

	}

}
