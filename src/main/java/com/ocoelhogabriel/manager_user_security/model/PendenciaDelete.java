package com.ocoelhogabriel.manager_user_security.model;

import com.ocoelhogabriel.manager_user_security.model.enums.StatusEnum;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

@Schema(description = "Modelo para deletar uma pendência")
public class PendenciaDelete {

	@NotBlank(message = "O campo 'idPendencia' é obrigatório e não pode estar em branco.")
	@Schema(name = "idPendencia", description = "ID da pendência", example = "1", format = "Long")
	private Long idPendencia;

	@NotBlank(message = "O campo 'status' é obrigatório e não pode estar em branco.")
	@Schema(name = "status", description = "Status da Pendência", example = "EXECUTANDO", format = "String")
	private StatusEnum status;

	@NotBlank(message = "O campo 'descricao' é obrigatório e não pode estar em branco.")
	@Schema(name = "descricao", description = "Descrição da pendência", example = "Descrição", format = "String")
	private String descricao;

	public Long getIdPendencia() {
		return idPendencia;
	}

	public void setIdPendencia(Long idPendencia) {
		this.idPendencia = idPendencia;
	}

	public StatusEnum getStatus() {
		return status;
	}

	public void setStatus(StatusEnum status) {
		this.status = status;
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
		builder.append("PendenciaDelete [");
		if (idPendencia != null) {
			builder.append("IdPendencia=").append(idPendencia).append(", ");
		}
		if (status != null) {
			builder.append("status=").append(status).append(", ");
		}
		if (descricao != null) {
			builder.append("descricao=").append(descricao);
		}
		builder.append("]");
		return builder.toString();
	}

	public PendenciaDelete(Long idPendencia, StatusEnum status, String descricao) {
		super();
		this.idPendencia = idPendencia;
		this.status = status;
		this.descricao = descricao;
	}

	public PendenciaDelete() {
		super();

	}

}
