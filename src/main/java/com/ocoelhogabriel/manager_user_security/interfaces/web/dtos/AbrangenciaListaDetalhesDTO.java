package com.ocoelhogabriel.manager_user_security.interfaces.web.dtos;

import com.ocoelhogabriel.manager_user_security.infrastructure.adapters.persistence.entities.Abrangencia;

import java.util.List;

public class AbrangenciaListaDetalhesDTO extends CodigoExtends {

	private String nome;

	private String descricao;

	private List<AbrangenciaDetalhesDTO> recursos;

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

	public List<AbrangenciaDetalhesDTO> getRecursos() {
		return recursos;
	}

	public void setRecursos(List<AbrangenciaDetalhesDTO> recursos) {
		this.recursos = recursos;
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
		if (recursos != null) {
			builder.append("recursos=").append(recursos);
		}
		builder.append("]");
		return builder.toString();
	}

	public AbrangenciaListaDetalhesDTO(Abrangencia abr, List<AbrangenciaDetalhesDTO> recursos) {
		super();
		this.setCodigo(abr.getId());
		this.nome = abr.getNome();
		this.descricao = abr.getDescricao();
		this.recursos = recursos;

	}

	public AbrangenciaListaDetalhesDTO(Long codigo, String nome, String descricao, List<AbrangenciaDetalhesDTO> recursos) {
		super(codigo);
		this.nome = nome;
		this.descricao = descricao;
		this.recursos = recursos;
	}

	public AbrangenciaListaDetalhesDTO() {
		super();

	}

	public AbrangenciaListaDetalhesDTO(Long codigo) {
		super(codigo);

	}

}
