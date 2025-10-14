package com.ocoelhogabriel.manager_user_security.model;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;

@Schema(description = "Modelo de Usuário")
public class UsuarioModel {

    @NotBlank(message = "O campo 'nome' é obrigatório e não pode estar em branco.")
    @Schema(name = "nome", description = "Nome do usuário.", example = "Administrador", format = "String")
    private String nome;

    @NotNull(message = "O campo 'cpf' é obrigatório. Informe apenas números.")
    @Schema(name = "cpf", description = "CPF do usuário (apenas números).", example = "12332123212", format = "Long")
    private Long cpf;

    @NotBlank(message = "O campo 'login' é obrigatório e não pode estar em branco.")
    @Schema(name = "login", description = "Login do usuário.", example = "admin", format = "String")
    private String login;

    @NotBlank(message = "O campo 'senha' é obrigatório e não pode estar em branco.")
    @Schema(name = "senha", description = "Senha do usuário.", example = "admin", format = "String")
    private String senha;

    @Email(message = "O campo 'email' deve ser um endereço de e-mail válido.")
    @Schema(name = "email", description = "E-mail do usuário.", example = "admin@admin.com", format = "String")
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
        builder.append("UsuarioModel [");
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

    public UsuarioModel(String nome, Long cpf, String login, String senha, String email, Long empresa, Long perfil, Long abrangencia) {
        this.nome = nome;
        this.cpf = cpf;
        this.login = login;
        this.senha = senha;
        this.email = email;
        this.empresa = empresa;
        this.perfil = perfil;
        this.abrangencia = abrangencia;
    }

    public UsuarioModel() {
        // Construtor vazio
    }
}