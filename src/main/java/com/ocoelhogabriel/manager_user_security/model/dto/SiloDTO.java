package com.ocoelhogabriel.manager_user_security.model.dto;

import com.ocoelhogabriel.manager_user_security.model.entity.Silo;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Silo")
public class SiloDTO extends CodigoExtends {

	@Schema(description = "Tipo do Silo", example = "1", nullable = false)
	private TipoSiloDTO tipoSilo;
	@Schema(description = "Código Planta", example = "1", nullable = true)
	private PlantaDTO planta;
	@Schema(description = "Nome", example = "Silo 1", nullable = true)
	private String nome;
	@Schema(description = "latitude", example = "1111", nullable = true)
	private Double latitude;
	@Schema(description = "longitude", example = "2222", nullable = true)
	private Double longitude;

	public static String filtrarDirecao(String str) {
		switch (str.toUpperCase()) {
		case "CODIGO" -> {
			return "silcod";
		}
		case "TIPOSILO" -> {
			return "tsicod";
		}
		case "PLANTA" -> {
			return "placod";
		}
		case "NOME" -> {
			return "silnom";
		}
		default -> throw new AssertionError();
		}
	}

	public SiloDTO() {
	}

	public SiloDTO(Silo entity) {
		this.setCodigo(entity.getSilcod());
		this.tipoSilo = new TipoSiloDTO(entity.getTipoSilo());
		this.planta = new PlantaDTO(entity.getPlanta());
		this.nome = entity.getSilnom();
		this.longitude = entity.getSillon();
		this.latitude = entity.getSillat();
	}

	public SiloDTO(Long codigo, TipoSiloDTO tipoSilo, PlantaDTO planta, String nome) {
		super(codigo);
		this.tipoSilo = tipoSilo;
		this.planta = planta;
		this.nome = nome;
	}

	public TipoSiloDTO getTipoSilo() {
		return tipoSilo;
	}

	public void setTipoSilo(TipoSiloDTO tipoSilo) {
		this.tipoSilo = tipoSilo;
	}

	public PlantaDTO getPlanta() {
		return planta;
	}

	public void setPlanta(PlantaDTO planta) {
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
		builder.append("SiloDTO [");
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
