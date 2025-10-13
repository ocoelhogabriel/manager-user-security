package com.ocoelhogabriel.manager_user_security.interfaces.web.dtos;

import com.fasterxml.jackson.databind.JsonNode;
import com.ocoelhogabriel.manager_user_security.infrastructure.adapters.persistence.entities.AbrangenciaDetalhes;
import com.ocoelhogabriel.manager_user_security.utils.JsonNodeConverter;

public class AbrangenciaDetalhesDTO {

	private String recurso;

	private Integer hierarquia;

	private JsonNode dados;

	public String getRecurso() {
		return recurso;
	}

	public Integer getHierarquia() {
		return hierarquia;
	}

	public void setHierarquia(Integer hierarquia) {
		this.hierarquia = hierarquia;
	}

	public JsonNode getDados() {
		return dados;
	}

	public void setDados(JsonNode dados) {
		this.dados = dados;
	}

	public void setRecurso(String recurso) {
		this.recurso = recurso;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("AbrangenciaDetalhes [");
		if (recurso != null) {
			builder.append("recurso=").append(recurso).append(", ");
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

	public AbrangenciaDetalhesDTO(String recurso, Integer hierarquia, JsonNode dados) {
		super();
		this.recurso = recurso;
		this.hierarquia = hierarquia;
		this.dados = dados;
	}

	public AbrangenciaDetalhesDTO(AbrangenciaDetalhes abrDetalhes) {
		super();
		JsonNodeConverter jsonNode = new JsonNodeConverter();

		this.recurso = abrDetalhes.getRecurso().getNome();
		this.hierarquia = abrDetalhes.getHierarquia();
		this.dados = jsonNode.convertToEntityAttribute(abrDetalhes.getDados());
	}

	public AbrangenciaDetalhesDTO() {
		super();

	}

}
