package com.ocoelhogabriel.manager_user_security.interfaces.web.dtos;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ocoelhogabriel.manager_user_security.domain.constraints.MessageConstraints;
import com.ocoelhogabriel.manager_user_security.infrastructure.adapters.persistence.entities.Usuario;

public class UsuarioDTO extends CodigoExtends {
    private static final Logger LOGGER = LoggerFactory.getLogger(UsuarioDTO.class);

	private String nome;
	private Long cpf;
	private String login;
	private String senha;
	private String email;
	private EmpresaDTO empresa;
	private PerfilDTO perfil;
	private AbrangenciaDTO abrangencia;

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

	public EmpresaDTO getEmpresa() {
		return empresa;
	}

	public void setEmpresa(EmpresaDTO empresa) {
		this.empresa = empresa;
	}

	public PerfilDTO getPerfil() {
		return perfil;
	}

	public void setPerfil(PerfilDTO perfil) {
		this.perfil = perfil;
	}

	public AbrangenciaDTO getAbrangencia() {
		return abrangencia;
	}

	public void setAbrangencia(AbrangenciaDTO abrangencia) {
		this.abrangencia = abrangencia;
	}

	public static String consultaPagable(String value) {
		try {
			return switch (value) {
				case "codigo" -> "usucod";
				case "nome" -> "usunom";
				case "cpf" -> "usucpf";
				case "login" -> "usulog";
				case "senha" -> "ususen";
				case "email" -> "usuema";
				default -> {
					if (LOGGER.isWarnEnabled()) {
						LOGGER.warn(MessageConstraints.VALIDATION_INVALID_FORMAT, "campo de ordenação");
					}
					throw new IllegalArgumentException(MessageConstraints.MessageFormatter.format(
						MessageConstraints.VALIDATION_INVALID_FORMAT, "campo de ordenação"));
				}
			};
		} catch (Exception e) {
			if (LOGGER.isErrorEnabled()) {
				LOGGER.error("Erro ao converter campo para consulta paginada: {}", e.getMessage(), e);
			}
			throw e;
		}
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("UsuarioDTO [");
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

	public UsuarioDTO(Long codigo, String nome, Long cpf, String login, String senha, String email, EmpresaDTO empresa) {
		super(codigo);
		this.nome = nome;
		this.cpf = cpf;
		this.login = login;
		this.senha = senha;
		this.email = email;
		this.empresa = empresa;
	}
	
	// Método builder para construir objetos complexos (padrão Builder)
	public static class Builder {
		private Long codigo;
		private String nome;
		private Long cpf;
		private String login;
		private String senha;
		private String email;
		private EmpresaDTO empresa;
		private PerfilDTO perfil;
		private AbrangenciaDTO abrangencia;
		
		public Builder codigo(Long codigo) {
			this.codigo = codigo;
			return this;
		}
		
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
		
		public Builder empresa(EmpresaDTO empresa) {
			this.empresa = empresa;
			return this;
		}
		
		public Builder perfil(PerfilDTO perfil) {
			this.perfil = perfil;
			return this;
		}
		
		public Builder abrangencia(AbrangenciaDTO abrangencia) {
			this.abrangencia = abrangencia;
			return this;
		}
		
		public UsuarioDTO build() {
			UsuarioDTO usuario = new UsuarioDTO(codigo, nome, cpf, login, senha, email, empresa);
			usuario.setPerfil(perfil);
			usuario.setAbrangencia(abrangencia);
			return usuario;
		}
	}
	
	public static Builder builder() {
		return new Builder();
	}

	public UsuarioDTO(Usuario user) {
		super();
		this.setCodigo(user.getId());
		this.nome = user.getNome();
		this.cpf = user.getCpf();
		this.login = user.getUsername();
		this.senha = user.getPassword();
		this.email = user.getEmail();
		this.empresa = new EmpresaDTO(user.getEmpresa());
		this.perfil = new PerfilDTO(user.getPerfil());
		this.abrangencia = new AbrangenciaDTO(user.getAbrangencia());
	}

	public UsuarioDTO() {
		super();
	}

	public UsuarioDTO(Long codigo) {
		super(codigo);
	}

}
