package com.ocoelhogabriel.manager_user_security.model;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

@Schema(description = "Medição")
public class MedicaoModel {

	@NotBlank(message = "O campo 'dataMedicao' é obrigatório e não pode estar em branco.")
	@Schema(description = "Data", example = "2021-08-01T00:00:00.000")
	private String dataMedicao;

	@NotBlank(message = "O campo 'silo' é obrigatório e não pode estar em branco.")
	@Schema(description = "Código", example = "1")
	private Long silo;

	@NotBlank(message = "O campo 'umidade' é obrigatório e não pode estar em branco.")
	@Schema(description = "Umidade", example = "1.0")
	private Double umidade;

	@NotBlank(message = "O campo 'ana' é obrigatório e não pode estar em branco.")
	@Schema(description = "Analogico", example = "1.0")
	private Double analogico;

	@NotBlank(message = "O campo 'barometro' é obrigatório e não pode estar em branco.")
	@Schema(description = "Barômetro", example = "1.0")
	private Double barometro;

	@NotBlank(message = "O campo 'temperatura' é obrigatório e não pode estar em branco.")
	@Schema(description = "Temperatura", example = "1.0")
	private Double temperatura;

	@NotBlank(message = "O campo 'distancia' é obrigatório e não pode estar em branco.")
	@Schema(description = "Distância", example = "1.0")
	private Double distancia;

	public MedicaoModel() {
	}

	public MedicaoModel(String dataMedicao, Long silo, Double umidade, Double ana, Double barometro, Double temperatura, Double distancia) {
		this.dataMedicao = dataMedicao;
		this.silo = silo;
		this.umidade = umidade;
		this.analogico = ana;
		this.barometro = barometro;
		this.temperatura = temperatura;
		this.distancia = distancia;
	}

	public String getDataMedicao() {
		return dataMedicao;
	}

	public void setDataMedicao(String dataMedicao) {
		this.dataMedicao = dataMedicao;
	}

	public Long getSilo() {
		return silo;
	}

	public void setSilo(Long silo) {
		this.silo = silo;
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

	public Double getDistancia() {
		return distancia;
	}

	public void setDistancia(Double distancia) {
		this.distancia = distancia;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("MedicaoDTO [dataMedicao=");
		builder.append(dataMedicao);
		builder.append(", silo=");
		builder.append(silo);
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
