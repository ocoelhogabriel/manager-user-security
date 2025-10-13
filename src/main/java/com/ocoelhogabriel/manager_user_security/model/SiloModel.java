package com.ocoelhogabriel.manager_user_security.model;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

@Schema(description = "Silo")
public class SiloModel {

	@NotNull(message = "O campo 'tipoSilo' é obrigatório e não pode estar nulo.")
	@Schema(description = "Tipo do Silo", example = "1", nullable = false)
	private Long tipoSilo;

	@NotNull(message = "O campo 'planta' é obrigatório e não pode estar nulo.")
	@Schema(description = "Código da Planta", example = "1", nullable = true)
	private Long planta;

	@NotBlank(message = "O campo 'nome' é obrigatório e não pode estar em branco.")
	@Schema(description = "Nome", example = "Silo 1", nullable = true)
	private String nome;

	@NotNull(message = "O campo 'latitude' é obrigatório e não pode estar em branco.")
	@Schema(description = "latitude", example = "1111", nullable = true)
	private Double latitude;
	@NotNull(message = "O campo 'longitude' é obrigatório e não pode estar em branco.")
	@Schema(description = "longitude", example = "2222", nullable = true)
	private Double longitude;

	public SiloModel() {
	}

	public SiloModel(Double latitude, Double longitude, String nome, Long planta, Long tipoSilo) {
		this.latitude = latitude;
		this.longitude = longitude;
		this.nome = nome;
		this.planta = planta;
		this.tipoSilo = tipoSilo;
	}

	public Long getTipoSilo() {
		return tipoSilo;
	}

	public void setTipoSilo(Long tipoSilo) {
		this.tipoSilo = tipoSilo;
	}

	public Long getPlanta() {
		return planta;
	}

	public void setPlanta(Long planta) {
		this.planta = planta;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public Double getLatitude() {
		return latitude;
	}

	public void setLatitude(Double latitude) {
		this.latitude = latitude;
	}

	public Double getLongitude() {
		return longitude;
	}

	public void setLongitude(Double longitude) {
		this.longitude = longitude;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("SiloModel [");
		if (tipoSilo != null)
			builder.append("tipoSilo=").append(tipoSilo).append(", ");
		if (planta != null)
			builder.append("planta=").append(planta).append(", ");
		if (nome != null)
			builder.append("nome=").append(nome).append(", ");
		if (latitude != null)
			builder.append("latitude=").append(latitude).append(", ");
		if (longitude != null)
			builder.append("longitude=").append(longitude);
		builder.append("]");
		return builder.toString();
	}

}
