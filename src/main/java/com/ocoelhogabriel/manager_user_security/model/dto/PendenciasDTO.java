package com.ocoelhogabriel.manager_user_security.model.dto;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ocoelhogabriel.manager_user_security.model.entity.Pendencia;
import com.ocoelhogabriel.manager_user_security.model.enums.PendenciaEnum;
import com.ocoelhogabriel.manager_user_security.model.enums.StatusEnum;
import com.ocoelhogabriel.manager_user_security.utils.Utils;

public class PendenciasDTO {

	private Logger logger = LoggerFactory.getLogger(getClass());

	private Long id;
	private PendenciaEnum tipoPendencia;
	private StatusEnum status;
	private String descricao;
	private String dataInicio;
	private String dataFim;
	private Long modulo;
	private Long firmware;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
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

	public String getDataInicio() {
		return dataInicio;
	}

	public void setDataInicio(String dataInicio) {
		this.dataInicio = dataInicio;
	}

	public String getDataFim() {
		return dataFim;
	}

	public void setDataFim(String dataFim) {
		this.dataFim = dataFim;
	}

	public Long getModulo() {
		return modulo;
	}

	public void setModulo(Long modulo) {
		this.modulo = modulo;
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
		builder.append("PendenciasDTO [");
		if (logger != null) {
			builder.append("logger=").append(logger).append(", ");
		}
		if (id != null) {
			builder.append("id=").append(id).append(", ");
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
		if (dataInicio != null) {
			builder.append("dataInicio=").append(dataInicio).append(", ");
		}
		if (dataFim != null) {
			builder.append("dataFim=").append(dataFim).append(", ");
		}
		if (modulo != null) {
			builder.append("modulo=").append(modulo).append(", ");
		}
		if (firmware != null) {
			builder.append("firmware=").append(firmware);
		}
		builder.append("]");
		return builder.toString();
	}

	public PendenciasDTO(Logger logger, Long id, PendenciaEnum tipoPendencia, StatusEnum status, String descricao, String dataInicio, String dataFim, Long modulo, Long firmware) {
		super();
		this.logger = logger;
		this.id = id;
		this.tipoPendencia = tipoPendencia;
		this.status = status;
		this.descricao = descricao;
		this.dataInicio = dataInicio;
		this.dataFim = dataFim;
		this.modulo = modulo;
		this.firmware = firmware;
	}

	public static String consultaPagable(String value) {
		switch (value) {
		case "id" -> {
			return "pencod";
		}
		case "tipoPendencia" -> {
			return "pentip";
		}
		case "status" -> {
			return "pensta";
		}
		case "dataInicio" -> {
			return "penini";
		}
		case "dataFim" -> {
			return "penfim";
		}
		case "modulo" -> {
			return "smocod";
		}
		default -> throw new IllegalArgumentException("Unexpected value: " + value);
		}
	}

	public PendenciasDTO(Pendencia pend) {
		try {
			this.id = pend.getPencod();
			this.tipoPendencia = PendenciaEnum.valueOf(pend.getPentip());
			this.status = StatusEnum.valueOf(pend.getPensta());
			this.descricao = pend.getPendes();
			this.dataInicio = pend.getPenini() == null ? null : Utils.dateToString(pend.getPenini());
			this.dataFim = pend.getPenfim() == null ? null : Utils.dateToString(pend.getPenfim());
			this.modulo = pend.getSmocod();
			this.firmware = pend.getFirmware().getFircod();
		} catch (Exception e) {
			logger.info("Error: ", e);
		}
	}

	public PendenciasDTO() {
		super();
	}

}
