package com.ocoelhogabriel.manager_user_security.model;

import com.ocoelhogabriel.manager_user_security.model.enums.PendenciaEnum;
import com.ocoelhogabriel.manager_user_security.model.enums.StatusEnum;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

@Schema(description = "Modelo de Pendência")
public class PendenciaModel {

	@NotBlank(message = "O campo 'numSerie' é obrigatório e não pode estar em branco.")
	@Schema(name = "numSerie", description = "Número de Série do Módulo", example = "N123124", format = "String")
	private String numSerie;

	@NotBlank(message = "O campo 'tipoPendencia' é obrigatório e não pode estar em branco.")
	@Schema(name = "tipoPendencia", description = "Tipo da Pendência", example = "DATA_HORA", format = "String")
	private PendenciaEnum tipoPendencia;

	@NotBlank(message = "O campo 'status' é obrigatório e não pode estar em branco.")
	@Schema(name = "status", description = "Status da Pendência", example = "PENDENCIA", format = "String")
	private StatusEnum status;

	@NotBlank(message = "O campo 'descricao' é obrigatório e não pode estar em branco.")
	@Schema(name = "descricao", description = "Descrição da Pendência", example = "Descrição", format = "String")
	private String descricao;

	@Schema(name = "firmware", description = "Se for pendência, coloque o código do firmware cadastrado.", example = "1", format = "Long")
	private Long firmware;

	public String getNumSerie() {
		return numSerie;
	}

	public void setNumSerie(String numSerie) {
		this.numSerie = numSerie;
	}

	public PendenciaEnum getTipoPendencia() {
		return tipoPendencia;
	}

	public void setTipoPendencia(PendenciaEnum tipoPendencia) {
		this.tipoPendencia = tipoPendencia;
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

	public Long getFirmware() {
		return firmware;
	}

	public void setFirmware(Long firmware) {
		this.firmware = firmware;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("PendenciaModel [");
		if (numSerie != null) {
			builder.append("numSerie=").append(numSerie).append(", ");
		}
		if (tipoPendencia != null) {
			builder.append("tipoPendencia=").append(tipoPendencia).append(", ");
		}
		if (status != null) {
			builder.append("status=").append(status).append(", ");
		}
		if (descricao != null) {
			builder.append("descricao=").append(descricao).append(", ");
		}
		if (firmware != null) {
			builder.append("firmware=").append(firmware);
		}
		builder.append("]");
		return builder.toString();
	}

	public PendenciaModel(String numSerie, PendenciaEnum tipoPendencia, StatusEnum status, String descricao, Long firmware) {
		super();
		this.numSerie = numSerie;
		this.tipoPendencia = tipoPendencia;
		this.status = status;
		this.descricao = descricao;
		this.firmware = firmware;
	}

	public PendenciaModel() {
		super();
	}

}
