package com.ocoelhogabriel.manager_user_security.model;

import com.ocoelhogabriel.manager_user_security.model.enums.StatusDeviceEnum;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

@Schema(description = "Modelo de cadastro de um Módulo do Silo")
public class SiloModuloModel {

	@NotNull(message = "O código do silo é obrigatório e não pode ser nulo.")
	@Schema(name = "silo", description = "Código do Silo", example = "1", format = "Long")
	private Long silo;

	@NotBlank(message = "A descrição do módulo é obrigatória e não pode estar em branco.")
	@Schema(name = "descricao", description = "Descrição do módulo", example = "Módulo X, em para isso.", format = "String")
	private String descricao;

	@NotNull(message = "O número total de sensores é obrigatório e não pode ser nulo.")
	@Schema(name = "totalSensor", description = "Número de sensores instalados", example = "4", format = "Long")
	private Long totalSensor;

	@NotBlank(message = "O número de série é obrigatório e não pode estar em branco.")
	@Schema(name = "numSerie", description = "Número de Série (não pode ser repetido)", example = "N123124", format = "String")
	private String numSerie;

	@NotNull(message = "O intervalo de timeout do KeepAlive é obrigatório e não pode ser nulo.")
	@Schema(name = "timeoutKeepAlive", description = "Intervalo entre os keepalives para constar offline. (em minutos)", example = "10", format = "Long")
	private Long timeoutKeepAlive;

	@NotNull(message = "O intervalo de timeout da medição é obrigatório e não pode ser nulo.")
	@Schema(name = "timeoutMedicao", description = "Intervalo entre as medições para constar offline. (em minutos)", example = "10", format = "Long")
	private Long timeoutMedicao;

	@NotNull(message = "O fuso horário local (GMT) é obrigatório e não pode ser nulo.")
	@Schema(name = "gmt", description = "Valor em minutos do fuso horário local", example = "-180", format = "Integer")
	private Integer gmt;

	@NotBlank(message = "A cor do alerta do KeepAlive é obrigatória e não pode estar em branco.")
	@Schema(name = "corKeepAlive", description = "Cor do alerta do KeepAlive caso extrapole o intervalo configurado", example = "#FF0000", format = "String")
	private String corKeepAlive;

	@NotBlank(message = "A cor do alerta da medição é obrigatória e não pode estar em branco.")
	@Schema(name = "corMedicao", description = "Cor do alerta da medição caso extrapole o intervalo configurado", example = "#00FF00", format = "String")
	private String corMedicao;

	@NotNull(message = "O status do equipamento é obrigatório e não pode ser nulo.")
	@Schema(name = "status", description = "Status do equipamento, para módulo usar ATIVO/INATIVO", example = "ATIVO", format = "StatusDeviceEnum")
	private StatusDeviceEnum status;

	public SiloModuloModel(Long silo, String descricao, Long totalSensor, String numSerie, Long timeoutKeepAlive, Long timeoutMedicao, Integer gmt, String corKeepAlive, String corMedicao, StatusDeviceEnum status) {
		this.silo = silo;
		this.descricao = descricao;
		this.totalSensor = totalSensor;
		this.numSerie = numSerie;
		this.timeoutKeepAlive = timeoutKeepAlive;
		this.timeoutMedicao = timeoutMedicao;
		this.gmt = gmt;
		this.corKeepAlive = corKeepAlive;
		this.corMedicao = corMedicao;
		this.status = status;
	}

	public SiloModuloModel() {
		super();
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("SiloModuloModel [");
		if (silo != null)
			builder.append("silo=").append(silo).append(", ");
		if (descricao != null)
			builder.append("descricao=").append(descricao).append(", ");
		if (totalSensor != null)
			builder.append("totalSensor=").append(totalSensor).append(", ");
		if (numSerie != null)
			builder.append("numSerie=").append(numSerie).append(", ");
		if (timeoutKeepAlive != null)
			builder.append("timeoutKeepAlive=").append(timeoutKeepAlive).append(", ");
		if (timeoutMedicao != null)
			builder.append("timeoutMedicao=").append(timeoutMedicao).append(", ");
		if (gmt != null)
			builder.append("gmt=").append(gmt).append(", ");
		if (corKeepAlive != null)
			builder.append("corKeepAlive=").append(corKeepAlive).append(", ");
		if (corMedicao != null)
			builder.append("corMedicao=").append(corMedicao).append(", ");
		if (status != null)
			builder.append("status=").append(status);
		builder.append("]");
		return builder.toString();
	}

	public Long getSilo() {
		return silo;
	}

	public void setSilo(Long silo) {
		this.silo = silo;
	}

	public String getDescricao() {
		return descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}

	public Long getTotalSensor() {
		return totalSensor;
	}

	public void setTotalSensor(Long totalSensor) {
		this.totalSensor = totalSensor;
	}

	public String getNumSerie() {
		return numSerie;
	}

	public void setNumSerie(String numSerie) {
		this.numSerie = numSerie;
	}

	public Long getTimeoutKeepAlive() {
		return timeoutKeepAlive;
	}

	public void setTimeoutKeepAlive(Long timeoutKeepAlive) {
		this.timeoutKeepAlive = timeoutKeepAlive;
	}

	public Long getTimeoutMedicao() {
		return timeoutMedicao;
	}

	public void setTimeoutMedicao(Long timeoutMedicao) {
		this.timeoutMedicao = timeoutMedicao;
	}

	public Integer getGmt() {
		return gmt;
	}

	public void setGmt(Integer gmt) {
		this.gmt = gmt;
	}

	public String getCorKeepAlive() {
		return corKeepAlive;
	}

	public void setCorKeepAlive(String corKeepAlive) {
		this.corKeepAlive = corKeepAlive;
	}

	public String getCorMedicao() {
		return corMedicao;
	}

	public void setCorMedicao(String corMedicao) {
		this.corMedicao = corMedicao;
	}

	public StatusDeviceEnum getStatus() {
		return status;
	}

	public void setStatus(StatusDeviceEnum status) {
		this.status = status;
	}

}
