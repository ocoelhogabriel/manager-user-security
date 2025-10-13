package com.ocoelhogabriel.manager_user_security.model.dto;

import com.ocoelhogabriel.manager_user_security.model.entity.Empresa;

public class EmpresaDTO extends CodigoExtends {
	private Long cnpj;
	private String nome;
	private String nomeFantasia;
	private String telefone;

	public Long getCnpj() {
		return cnpj;
	}

	public void setCnpj(Long cnpj) {
		this.cnpj = cnpj;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public String getNomeFantasia() {
		return nomeFantasia;
	}

	public void setNomeFantasia(String nomeFantasia) {
		this.nomeFantasia = nomeFantasia;
	}

	public String getTelefone() {
		return telefone;
	}

	public void setTelefone(String telefone) {
		this.telefone = telefone;
	}

	public static String consultaPagable(String value) {
		switch (value.toUpperCase()) {
		case "CODIGO":
			return "empcod";
		case "CNPJ":
			return "empcnp";
		case "NOME":
			return "empnom";
		case "NOMEFANTASIA":
			return "empfan";
		case "TELEFONE":
			return "emptel";
		default:
			throw new IllegalArgumentException("Unexpected value: " + value);
		}
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("EmpresaDto [codigo=");
		builder.append(getCodigo());
		builder.append(", cnpj=");
		builder.append(cnpj);
		builder.append(", nome=");
		builder.append(nome);
		builder.append(", nomeFantasia=");
		builder.append(nomeFantasia);
		builder.append(", telefone=");
		builder.append(telefone);
		builder.append("]");
		return builder.toString();
	}

	public EmpresaDTO(Long codigo, Long cnpj, String nome, String nomeFantasia, String telefone) {
		super(codigo);
		this.cnpj = cnpj;
		this.nome = nome;
		this.nomeFantasia = nomeFantasia;
		this.telefone = telefone;
	}

	public EmpresaDTO(Empresa emp) {
		super();
		this.setCodigo(emp.getEmpcod());
		this.cnpj = emp.getEmpcnp();
		this.nome = emp.getEmpnom();
		this.nomeFantasia = emp.getEmpfan();
		this.telefone = emp.getEmptel();
	}

	public EmpresaDTO() {
		super();
	}

}
