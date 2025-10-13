package com.ocoelhogabriel.manager_user_security.domain.entities;

import com.ocoelhogabriel.manager_user_security.domain.value_objects.RecursoMapEnum;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

@Schema(description = "Recurso modelo")
public class RecursoModel {

	@NotBlank(message = "O campo 'nome' é obrigatório e não pode estar em branco.")
	@Schema(name = "nome", description = "Lista dos níveis de permissão. Permissões:  LOGGER, EMPRESA, PLANTA, USUARIO, PERFIL, PERMISSAO, RECURSO, ABRANGENCIA.", example = "PLANTA")
	private RecursoMapEnum nome;

	@Schema(name = "descricao", description = "Descrição do Recurso")
	private String descricao;

	public RecursoMapEnum getNome() {
		return nome;
	}

	public void setNome(RecursoMapEnum nome) {
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
		builder.append("RecursoModel [");
		if (nome != null) {
			builder.append("nome=").append(nome).append(", ");
		}
		if (descricao != null) {
			builder.append("descricao=").append(descricao);
		}
		builder.append("]");
		return builder.toString();
	}

	public RecursoModel(@NotBlank RecursoMapEnum nome, String descricao) {
		super();
		this.nome = nome;
		this.descricao = descricao;
	}

	public RecursoModel() {
		super();

	}

}
