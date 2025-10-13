package com.ocoelhogabriel.manager_user_security.model.dto;

import com.ocoelhogabriel.manager_user_security.model.entity.Medicao;
import com.ocoelhogabriel.manager_user_security.utils.Utils;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

@Schema(description = "Medicão")
public class MedicaoDTO {

	@NotBlank
	@Schema(description = "data", example = "2021-08-01T00:00:00.000Z")
	private String data;
	@NotBlank
	@Schema(description = "modulo", example = "1")
	private SiloModuloDTO modulo;
	@NotBlank
	@Schema(description = "Umidade", example = "1.0")
	private Double umidade;
	@NotBlank
	@Schema(description = "Analogico", example = "1.0")
	private Double analogico;
	@NotBlank
	@Schema(description = "Barometro", example = "1.0")
	private Double barometro;
	@NotBlank
	@Schema(description = "Temperatura", example = "1.0")
	private Double temperatura;
	@NotBlank
	@Schema(description = "Distancia", example = "1.0")
	private Long distancia;

	public MedicaoDTO() {
	}

	public static String filtrarDirecao(String str) {
		switch (str.toUpperCase()) {
		case "DATA" -> {
			return "msidth";
		}
		case "MODULO" -> {
			return "smocod";
		}
		default -> throw new AssertionError();
		}
	}

	public MedicaoDTO(String data, SiloModuloDTO modulo, Double umidade, Double analogico, Double barometro, Double temperatura, Long distancia) {
		this.data = data;
		this.modulo = modulo;
		this.umidade = umidade;
		this.analogico = analogico;
		this.barometro = barometro;
		this.temperatura = temperatura;
		this.distancia = distancia;
	}

	public MedicaoDTO(Medicao medEntity) {
		this.data = Utils.sdfDateforString(medEntity.getMsidth());
		this.modulo = new SiloModuloDTO(medEntity.getModulo());
		this.umidade = medEntity.getMsiumi();
		this.analogico = medEntity.getMsiana();
		this.barometro = medEntity.getMsibar();
		this.temperatura = medEntity.getMsitem();
		this.distancia = Utils.converterMmParaMLong(Math.round(medEntity.getMsidis()));
	}

	public String getData() {
		return data;
	}

	public void setData(String data) {
		this.data = data;
	}

	public SiloModuloDTO getModulo() {
		return modulo;
	}

	public void setModulo(SiloModuloDTO modulo) {
		this.modulo = modulo;
	}

	public Double getUmidade() {
		return umidade;
	}

	public void setUmidade(Double umidade) {
		this.umidade = umidade;
	}

	public Double getAnalogico() {
		return analogico;
	}

	public void setAnalogico(Double analogico) {
		this.analogico = analogico;
	}

	public Double getBarometro() {
		return barometro;
	}

	public void setBarometro(Double barometro) {
		this.barometro = barometro;
	}

	public Double getTemperatura() {
		return temperatura;
	}

	public void setTemperatura(Double temperatura) {
		this.temperatura = temperatura;
	}

	public Long getDistancia() {
		return distancia;
	}

	public void setDistancia(Long distancia) {
		this.distancia = distancia;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("MedicaoDTO [data=");
		builder.append(data);
		builder.append(", modulo=");
		builder.append(modulo);
		builder.append(", umidade=");
		builder.append(umidade);
		builder.append(", ana=");
		builder.append(analogico);
		builder.append(", barometro=");
		builder.append(barometro);
		builder.append(", temperatura=");
		builder.append(temperatura);
		builder.append(", distancia=");
		builder.append(distancia);
		builder.append("]");
		return builder.toString();
	}

}
