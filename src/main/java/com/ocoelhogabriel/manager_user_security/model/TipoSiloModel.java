package com.ocoelhogabriel.manager_user_security.model;

import com.ocoelhogabriel.manager_user_security.model.enums.TipoSiloEnum;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

@Schema(description = "Tipo Silo")
public class TipoSiloModel {

	@NotBlank(message = "O campo 'nome' é obrigatório e não pode estar em branco.")
	@Schema(description = "Nome do tipo de Silo", example = "Tipo 1")
	private String nome;

	@NotBlank(message = "O campo 'descricao' é obrigatório e não pode estar em branco.")
	@Schema(description = "Descrição", example = "Tipo 1 descrição")
	private String descricao;

	@NotBlank(message = "O campo 'tipoSilo' é obrigatório e não pode estar em branco.")
	@Schema(description = "tipoSilo", example = "VERTICAL")
	private TipoSiloEnum tipoSilo;

	@NotNull(message = "O campo 'distanciaSensor' é obrigatório e não pode estar nulo.")
	@Schema(description = "distanciaSensor em centímetro", example = "100.0")
	private Double distanciaSensor;
	@NotNull(message = "O campo 'alturaCheio' é obrigatório e não pode estar nulo.")
	@Schema(description = "alturaCheio em centímetro", example = "4000.0")
	private Double alturaCheio;
//	@NotNull(message = "O campo 'volumeCheio' é obrigatório e não pode estar nulo.")
//	@Schema(description = "volumeCheio", example = "100.0")
//	private Double volumeCheio;
	@Schema(description = "raio em centímetro", example = "200.0")
	private Double raio = (double) 0;
	@Schema(description = "largura em centímetro", example = "160.0")
	private Double largura = (double) 0;
	@Schema(description = "comprimento em centímetro", example = "150.0")
	private Double comprimento = (double) 0;

	public TipoSiloModel(@NotBlank(message = "O campo 'nome' é obrigatório e não pode estar em branco.") String nome, @NotBlank(message = "O campo 'descricao' é obrigatório e não pode estar em branco.") String descricao,
			@NotBlank(message = "O campo 'tipoSilo' é obrigatório e não pode estar em branco.") TipoSiloEnum tipoSilo, @NotNull(message = "O campo 'distanciaSensor' é obrigatório e não pode estar nulo.") Double distanciaSensor,
			@NotNull(message = "O campo 'alturaCheio' é obrigatório e não pode estar nulo.") Double alturaCheio, Double raio, Double largura, Double comprimento) {
		super();
		this.nome = nome;
		this.descricao = descricao;
		this.tipoSilo = tipoSilo;
		this.distanciaSensor = distanciaSensor;
		this.alturaCheio = alturaCheio;
		this.raio = raio;
		this.largura = largura;
		this.comprimento = comprimento;
	}

	public TipoSiloModel() {
		super();

	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("TipoSiloModel [");
		if (nome != null) {
			builder.append("nome=").append(nome).append(", ");
		}
		if (descricao != null) {
			builder.append("descricao=").append(descricao).append(", ");
		}
		if (tipoSilo != null) {
			builder.append("tipoSilo=").append(tipoSilo).append(", ");
		}
		if (distanciaSensor != null) {
			builder.append("distanciaSensor=").append(distanciaSensor).append(", ");
		}
		if (alturaCheio != null) {
			builder.append("alturaCheio=").append(alturaCheio).append(", ");
		}
		if (raio != null) {
			builder.append("raio=").append(raio).append(", ");
		}
		if (largura != null) {
			builder.append("largura=").append(largura).append(", ");
		}
		if (comprimento != null) {
			builder.append("comprimento=").append(comprimento);
		}
		builder.append("]");
		return builder.toString();
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public String getDescricao() {
		return descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}

	public TipoSiloEnum getTipoSilo() {
		return tipoSilo;
	}

	public void setTipoSilo(TipoSiloEnum tipoSilo) {
		this.tipoSilo = tipoSilo;
	}

	public Double getDistanciaSensor() {
		return distanciaSensor;
	}

	public void setDistanciaSensor(Double distanciaSensor) {
		this.distanciaSensor = distanciaSensor;
	}

	public Double getAlturaCheio() {
		return alturaCheio;
	}

	public void setAlturaCheio(Double alturaCheio) {
		this.alturaCheio = alturaCheio;
	}

	public Double getRaio() {
		return raio;
	}

	public void setRaio(Double raio) {
		this.raio = raio;
	}

	public Double getLargura() {
		return largura;
	}

	public void setLargura(Double largura) {
		this.largura = largura;
	}

	public Double getComprimento() {
		return comprimento;
	}

	public void setComprimento(Double comprimento) {
		this.comprimento = comprimento;
	}

}
