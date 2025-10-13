package com.ocoelhogabriel.manager_user_security.model.dto;

import com.ocoelhogabriel.manager_user_security.model.entity.Firmware;

public class FirmwareDTO extends CodigoExtends {

	private String modelo;
	private String nome;
	private String descricao;
	private String arquivo;
	private Long codidoModulo;

	public String getModelo() {
		return modelo;
	}

	public void setModelo(String modelo) {
		this.modelo = modelo;
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

	public String getArquivo() {
		return arquivo;
	}

	public void setArquivo(String arquivo) {
		this.arquivo = arquivo;
	}

	public Long getCodidoModulo() {
		return codidoModulo;
	}

	public void setCodidoModulo(Long codidoModulo) {
		this.codidoModulo = codidoModulo;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("FirmwareDTO [");
		if (modelo != null) {
			builder.append("modelo=").append(modelo).append(", ");
		}
		if (nome != null) {
			builder.append("nome=").append(nome).append(", ");
		}
		if (descricao != null) {
			builder.append("descricao=").append(descricao).append(", ");
		}
		if (arquivo != null) {
			builder.append("arquivo=").append(arquivo).append(", ");
		}
		if (codidoModulo != null) {
			builder.append("codidoModulo=").append(codidoModulo);
		}
		builder.append("]");
		return builder.toString();
	}

	public FirmwareDTO(Long codigo, String modelo, String nome, String descricao, String arquivo, Long codidoModulo) {
		super(codigo);
		this.modelo = modelo;
		this.nome = nome;
		this.descricao = descricao;
		this.arquivo = arquivo;
		this.codidoModulo = codidoModulo;
	}

	public FirmwareDTO(Firmware firm) {
		super();
		this.setCodigo(firm.getFircod());
		this.modelo = firm.getFirmod();
		this.nome = firm.getFirnam();
		this.descricao = firm.getFirdesc();
		this.arquivo = "Arquivo de audio. Size: " + firm.getFirarq().length;
	}

	public FirmwareDTO() {
		super();

	}

	public FirmwareDTO(Long codigo) {
		super(codigo);

	}

}
