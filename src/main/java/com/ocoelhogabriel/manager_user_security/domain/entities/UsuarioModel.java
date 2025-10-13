package com.ocoelhogabriel.manager_user_security.domain.entities;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

@Schema(description = "Modelo de Usuário")
public class UsuarioModel {

	@NotBlank(message = "O campo 'nome' é obrigatório e não pode estar em branco.")
	@Schema(name = "nome", description = "Cadastro do nome do usuário.", example = "Administrador", format = "String")
	private String nome;

	@NotNull(message = "O campo 'cpf' é obrigatório. Informe apenas números.")
	@Schema(name = "cpf", description = "Cadastro do CPF do usuário. (Somente números)", example = "12332123212", format = "Long")
	private Long cpf;

	@NotBlank(message = "O campo 'login' é obrigatório e não pode estar em branco.")
	@Schema(name = "login", description = "Cadastro do login do usuário.", example = "admin", format = "String")
	private String login;

	@NotBlank(message = "O campo 'senha' é obrigatório e não pode estar em branco.")
	@Schema(name = "senha", description = "Cadastro do senha do usuário.", example = "admin", format = "String")
	private String senha;

	@NotBlank(message = "O campo 'email' é obrigatório e não pode estar em branco.")
	@Email(message = "Formato de email inválido")
	@Schema(name = "email", description = "Cadastro do email do usuário.", example = "admin@admin.com", format = "String")
	private String email;

	@NotNull(message = "O campo 'empresa' é obrigatório.")
	@Schema(name = "empresa", description = "Código da empresa do usuário.", example = "1", format = "Long")
	private Long empresa;

	@NotNull(message = "O campo 'perfil' é obrigatório.")
	@Schema(name = "perfil", description = "Código do perfil do usuário.", example = "1", format = "Long")
	private Long perfil;

	@NotNull(message = "O campo 'abrangencia' é obrigatório.")
	@Schema(name = "abrangencia", description = "Código da abrangência do usuário.", example = "1", format = "Long")
	private Long abrangencia;

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public Long getCpf() {
		return cpf;
	}

	public void setCpf(Long cpf) {
		this.cpf = cpf;
	}

	public String getLogin() {
		return login;
	}

	public void setLogin(String login) {
		this.login = login;
	}

	public String getSenha() {
		return senha;
	}

	public void setSenha(String senha) {
		this.senha = senha;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public Long getEmpresa() {
		return empresa;
	}

	public void setEmpresa(Long empresa) {
		this.empresa = empresa;
	}

	public Long getPerfil() {
		return perfil;
	}

	public void setPerfil(Long perfil) {
		this.perfil = perfil;
	}

	public Long getAbrangencia() {
		return abrangencia;
	}

	public void setAbrangencia(Long abrangencia) {
		this.abrangencia = abrangencia;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("Usuario [");
		if (nome != null) {
			builder.append("nome=").append(nome).append(", ");
		}
		if (cpf != null) {
			builder.append("cpf=").append(cpf).append(", ");
		}
		if (login != null) {
			builder.append("login=").append(login).append(", ");
		}
		if (senha != null) {
			builder.append("senha=").append(senha).append(", ");
		}
		if (email != null) {
			builder.append("email=").append(email).append(", ");
		}
		if (empresa != null) {
			builder.append("empresa=").append(empresa).append(", ");
		}
		if (perfil != null) {
			builder.append("perfil=").append(perfil).append(", ");
		}
		if (abrangencia != null) {
			builder.append("abrangencia=").append(abrangencia);
		}
		builder.append("]");
		return builder.toString();
	}

	/**
	 * Construtor privado para uso com o padrão Builder
	 */
	private UsuarioModel(Builder builder) {
		this.nome = builder.nome;
		this.cpf = builder.cpf;
		this.login = builder.login;
		this.senha = builder.senha;
		this.email = builder.email;
		this.empresa = builder.empresa;
		this.perfil = builder.perfil;
		this.abrangencia = builder.abrangencia;
	}

	/**
	 * Construtor padrão
	 */
	public UsuarioModel() {
		super();
	}
	
	/**
	 * Cria um novo Builder para construir instâncias de UsuarioModel
	 * @return novo Builder
	 */
	public static Builder builder() {
		return new Builder();
	}
	
	/**
	 * Classe Builder para construção de objetos UsuarioModel
	 */
	public static class Builder {
		private String nome;
		private Long cpf;
		private String login;
		private String senha;
		private String email;
		private Long empresa;
		private Long perfil;
		private Long abrangencia;
		
		public Builder nome(String nome) {
			this.nome = nome;
			return this;
		}
		
		public Builder cpf(Long cpf) {
			this.cpf = cpf;
			return this;
		}
		
		public Builder login(String login) {
			this.login = login;
			return this;
		}
		
		public Builder senha(String senha) {
			this.senha = senha;
			return this;
		}
		
		public Builder email(String email) {
			this.email = email;
			return this;
		}
		
		public Builder empresa(Long empresa) {
			this.empresa = empresa;
			return this;
		}
		
		public Builder perfil(Long perfil) {
			this.perfil = perfil;
			return this;
		}
		
		public Builder abrangencia(Long abrangencia) {
			this.abrangencia = abrangencia;
			return this;
		}
		
		public UsuarioModel build() {
			return new UsuarioModel(this);
		}
	}

}
