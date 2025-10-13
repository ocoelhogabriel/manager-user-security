package com.ocoelhogabriel.manager_user_security.model.dto;

import com.ocoelhogabriel.manager_user_security.model.entity.Abrangencia;

public class AbrangenciaDTO extends CodigoExtends {

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
		builder.append("AbrangenciaDTO [");
		if (nome != null) {
			builder.append("nome=").append(nome).append(", ");
		}
		if (descricao != null) {
			builder.append("descricao=").append(descricao).append(", ");
		}
		builder.append("]");
		return builder.toString();
	}

	public AbrangenciaDTO(Abrangencia abr) {
		super();
		this.setCodigo(abr.getAbrcod());
		this.nome = abr.getAbrnom();
		this.descricao = abr.getAbrdes();

	}

	public AbrangenciaDTO(Long codigo, String nome, String descricao) {
		super(codigo);
		this.nome = nome;
		this.descricao = descricao;
	}

	public AbrangenciaDTO() {
		super();

	}

	public AbrangenciaDTO(Long codigo) {
		super(codigo);

	}

}
