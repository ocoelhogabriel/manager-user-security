package com.ocoelhogabriel.manager_user_security.infrastructure.adapters.persistence.entities;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

import jakarta.persistence.Table;
import jakarta.persistence.Transient;
import jakarta.persistence.criteria.Predicate;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

/**
 * Entidade JPA para Usuário do Sistema
 * Implementa UserDetails do Spring Security para autenticação/autorização
 * Aplica padrões de clean code com validação e encapsulamento
 */
@Entity
@Table(name = "usuario")
@EntityListeners(AuditingEntityListener.class)
public class Usuario implements UserDetails {

	private static final long serialVersionUID = 1L;
    private static final String EMAIL_PATTERN = 
        "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
    
    // Constantes para campos de auditoria e notificações
    private static final String FIELD_ID = "id";
    private static final String FIELD_USERNAME = "username";
    private static final String FIELD_NOME = "nome";
    private static final String FIELD_EMAIL = "email";
    private static final String FIELD_ATIVO = "ativo";
    private static final String FIELD_PERFIL = "perfil";

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id") 
	private Long id;

	@NotBlank(message = "Nome é obrigatório")
    @Size(min = 3, max = 100, message = "Nome deve ter entre 3 e 100 caracteres")
	@Column(name = "nome", nullable = false, length = 100)
	private String nome;

    @NotNull(message = "CPF é obrigatório")
	@Column(name = "cpf", nullable = false, unique = true)
	private Long cpf;

    @NotBlank(message = "Nome de usuário é obrigatório")
    @Size(min = 3, max = 50, message = "Nome de usuário deve ter entre 3 e 50 caracteres")
	@Column(name = "username", nullable = false, unique = true, length = 50)
	private String username;

    @NotBlank(message = "Senha é obrigatória")
    @Size(min = 8, message = "Senha deve ter no mínimo 8 caracteres")
	@Column(name = "password", nullable = false, length = 100)
	private String password;

    @NotBlank(message = "Email é obrigatório")
    @Email(message = "Email deve ser válido")
    @jakarta.validation.constraints.Pattern(regexp = EMAIL_PATTERN, message = "Email deve ser válido")
	@Column(name = "email", nullable = false, unique = true, length = 100)
	private String email;

	@NotNull(message = "Empresa é obrigatória")
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "empresa_id", nullable = false)
	private Empresa empresa;

	@NotNull(message = "Perfil é obrigatório")
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "perfil_id", nullable = false)
	private Perfil perfil;

	@NotNull(message = "Abrangência é obrigatória")
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "abrangencia_id", nullable = false)
	private Abrangencia abrangencia;

	// Campo obsoleto - usando apenas o campo password
	@Transient
	private String passwordHash;

	@NotNull(message = "Status é obrigatório")
	@Column(name = "ativo", nullable = false)
	private Boolean ativo = true;
    
    @Column(name = "reset_password_token", length = 100)
    private String resetPasswordToken;
    
    @Column(name = "reset_password_expires")
    private LocalDateTime resetPasswordExpires;
    
    @Column(name = "last_login_at")
    private LocalDateTime lastLoginAt;
    
    @Column(name = "last_login_ip", length = 50)
    private String lastLoginIp;
    
    @Column(name = "last_login_user_agent", length = 255)
    private String lastLoginUserAgent;
    
    @Column(name = "login_attempts", columnDefinition = "integer default 0")
    private Integer loginAttempts = 0;
    
    @Column(name = "account_locked_until")
    private LocalDateTime accountLockedUntil;
    
    @Column(name = "password_changed_at")
    private LocalDateTime passwordChangedAt;

    @CreatedDate
	@Column(name = "created_at", nullable = false, updatable = false)
	private LocalDateTime createdAt;

    @LastModifiedDate
	@Column(name = "updated_at", nullable = false)
	private LocalDateTime updatedAt;

	public Usuario(Usuario usuario) {
		super();
		this.id = usuario.getId();
		this.cpf = usuario.getCpf();
		this.nome = usuario.getNome();
		this.username = usuario.getUsername();
		this.password = usuario.getPassword();
		this.email = usuario.getEmail();
		this.empresa = usuario.getEmpresa();
		this.perfil = usuario.getPerfil();
		this.abrangencia = usuario.getAbrangencia();
		this.ativo = usuario.isAtivo();
		this.createdAt = usuario.getCreatedAt() != null ? usuario.getCreatedAt() : LocalDateTime.now();
		this.updatedAt = LocalDateTime.now();
	}

	/**
	 * @deprecated Use o Builder pattern em vez deste construtor: Usuario.builder()...build()
	 * Será removido em versões futuras.
	 */
	@Deprecated(since = "1.1.0", forRemoval = true)
	public Usuario(Long id, Long cpf, String nome, String username, String password, String email, Empresa empresa,
			Perfil perfil, Abrangencia abrangencia) {
		super();
		this.id = id;
		this.cpf = cpf;
		this.nome = nome;
		this.username = username;
		this.password = password;
		this.email = email;
		this.empresa = empresa;
		this.perfil = perfil;
		this.abrangencia = abrangencia;
		this.ativo = true;
		this.createdAt = LocalDateTime.now();
		this.updatedAt = LocalDateTime.now();
	}

	@Override
	public String toString() {
        return String.format(
            "Usuario {id=%d, nome='%s', username='%s', email='%s', ativo=%s, perfil=%s}",
            id, nome, username, email, ativo, perfil != null ? perfil.getNome() : "null"
        );
	}
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        
        Usuario usuario = (Usuario) o;
        
        // ID é a chave primária, então se ambos tiverem ID, comparamos apenas por ID
        if (id != null && usuario.id != null) {
            return Objects.equals(id, usuario.id);
        }
        
        // Se não tiver ID (objeto novo), comparamos por CPF e username que são únicos
        return Objects.equals(cpf, usuario.cpf) && 
               Objects.equals(username, usuario.username);
    }
    
    @Override
    public int hashCode() {
        // Se tiver ID, usa o ID para o hashCode
        if (id != null) {
            return Objects.hash(id);
        }
        // Senão usa CPF e username que são campos únicos
        return Objects.hash(cpf, username);
    }

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getCpf() {
		return cpf;
	}

	public void setCpf(Long cpf) {
		this.cpf = cpf;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
        if (nome == null || nome.trim().isEmpty()) {
            throw new IllegalArgumentException("Nome não pode ser nulo ou vazio");
        }
		this.nome = nome;
        this.updatedAt = LocalDateTime.now();
	}

	@Override
	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		if (username == null || username.trim().isEmpty()) {
			throw new IllegalArgumentException("Username não pode ser nulo ou vazio");
		}
		this.username = username;
		this.updatedAt = LocalDateTime.now();
	}

	@Override
	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		if (password == null || password.trim().isEmpty()) {
			throw new IllegalArgumentException("Senha não pode ser nula ou vazia");
		}
		this.password = password;
		// Atualiza data de modificação
		this.updatedAt = LocalDateTime.now();
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		if (email == null || email.trim().isEmpty()) {
			throw new IllegalArgumentException("Email não pode ser nulo ou vazio");
		}
		// Uma validação simples de formato de email poderia ser adicionada aqui
		this.email = email;
		this.updatedAt = LocalDateTime.now();
	}

	public Empresa getEmpresa() {
		return empresa;
	}

	public void setEmpresa(Empresa empresa) {
		if (empresa == null) {
			throw new IllegalArgumentException("Empresa não pode ser nula");
		}
		this.empresa = empresa;
		this.updatedAt = LocalDateTime.now();
	}

	public Perfil getPerfil() {
		return perfil;
	}

	public void setPerfil(Perfil perfil) {
		if (perfil == null) {
			throw new IllegalArgumentException("Perfil não pode ser nulo");
		}
		this.perfil = perfil;
		this.updatedAt = LocalDateTime.now();
	}

	public Abrangencia getAbrangencia() {
		return abrangencia;
	}

	public void setAbrangencia(Abrangencia abrangencia) {
		if (abrangencia == null) {
			throw new IllegalArgumentException("Abrangência não pode ser nula");
		}
		this.abrangencia = abrangencia;
		this.updatedAt = LocalDateTime.now();
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	
	/**
	 * Método de fábrica para obter um Builder e criar um usuário
	 * @return Uma nova instância de Builder
	 */
	public static Builder builder() {
		return new Builder();
	}

	/**
	 * Construtor padrão para JPA
	 */
	/**
     * Construtor padrão para JPA
     */
	public Usuario() {
		super();
		this.createdAt = LocalDateTime.now();
		this.updatedAt = LocalDateTime.now();
		this.ativo = true;
	}
	
	/**
	 * Construtor com Builder Pattern para facilitar a criação de usuários com muitos atributos
	 * @param builder O builder contendo os atributos do usuário
	 */
	private Usuario(Builder builder) {
		this.id = builder.id;
		this.cpf = Objects.requireNonNull(builder.cpf, "CPF não pode ser nulo");
		this.nome = Objects.requireNonNull(builder.nome, "Nome não pode ser nulo");
		this.username = Objects.requireNonNull(builder.username, "Username não pode ser nulo");
		this.password = Objects.requireNonNull(builder.password, "Password não pode ser nulo");
		this.email = Objects.requireNonNull(builder.email, "Email não pode ser nulo");
		this.empresa = Objects.requireNonNull(builder.empresa, "Empresa não pode ser nula");
		this.perfil = Objects.requireNonNull(builder.perfil, "Perfil não pode ser nulo");
		this.abrangencia = Objects.requireNonNull(builder.abrangencia, "Abrangência não pode ser nula");
		this.ativo = builder.ativo == null || builder.ativo;
        this.resetPasswordToken = builder.resetPasswordToken;
        this.resetPasswordExpires = builder.resetPasswordExpires;
        this.lastLoginAt = builder.lastLoginAt;
        this.lastLoginIp = builder.lastLoginIp;
        this.lastLoginUserAgent = builder.lastLoginUserAgent;
        this.loginAttempts = builder.loginAttempts != null ? builder.loginAttempts : 0;
        this.accountLockedUntil = builder.accountLockedUntil;
        this.passwordChangedAt = builder.passwordChangedAt;
		this.createdAt = builder.createdAt != null ? builder.createdAt : LocalDateTime.now();
		this.updatedAt = builder.updatedAt != null ? builder.updatedAt : LocalDateTime.now();
	}

	// Implementação de métodos de UserDetails interface
	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		if (this.perfil == null) {
			return Collections.emptyList();
		}
		
		// Inicializa lista de autoridades com o perfil principal
		List<SimpleGrantedAuthority> authorities = new ArrayList<>();
		
		// Adiciona o perfil principal com prefixo ROLE_
		authorities.add(new SimpleGrantedAuthority("ROLE_" + this.perfil.getNome().toUpperCase()));
		
		// Se no futuro houver permissões extras associadas ao perfil,
		// elas poderão ser adicionadas aqui
		
		return authorities;
	}
    
    /**
     * Verifica se o usuário possui determinada permissão
     * 
     * @param permissao A permissão a ser verificada
     * @return true se o usuário possui a permissão, false caso contrário
     */
    public boolean possuiPermissao(String permissao) {
        if (permissao == null || permissao.trim().isEmpty()) {
            return false;
        }
        
        // Normaliza a permissão para verificação
        String permissaoNormalizada = permissao.toUpperCase().trim();
        
        // Verifica na lista de autoridades
        return getAuthorities().stream()
            .map(GrantedAuthority::getAuthority)
            .anyMatch(auth -> auth.toUpperCase().equals(permissaoNormalizada) ||
                     auth.toUpperCase().equals("ROLE_" + permissaoNormalizada));
    }
    
    /**
     * Verifica se o usuário pode acessar os dados da empresa informada
     * baseado na sua abrangência
     * 
     * @param empresaId O ID da empresa a ser verificada
     * @return true se o usuário pode acessar, false caso contrário
     */
    public boolean podeAcessarEmpresa(Long empresaId) {
        if (empresaId == null || abrangencia == null) {
            return false;
        }
        
        // Se for administrador, pode acessar qualquer empresa
        if (isAdmin()) {
            return true;
        }
        
        // Se abrangência for somente a própria empresa
        if (empresa != null && abrangencia.getNome() != null &&
            abrangencia.getNome().toUpperCase().contains("PROPRIA")) {
            return empresa.getId().equals(empresaId);
        }
        
        // Implementar lógica adicional de verificação de acesso conforme necessário
        // Por exemplo, verificar se a empresa pertence ao grupo de empresas permitidas
        
        return false; // Por padrão, nega acesso se não atender às condições acima
    }

	@Override
	public boolean isAccountNonExpired() {
		return true; // Não implementamos expiração de conta
	}

	@Override
	public boolean isAccountNonLocked() {
		// Verifica se a conta está temporariamente bloqueada por excesso de tentativas
		return !isContaBloqueadaTemporariamente();
	}

	@Override
	public boolean isCredentialsNonExpired() {
		// Implementação padrão: senha expira após 90 dias
		// Este valor poderia vir de uma configuração do sistema
		final int DIAS_PARA_EXPIRAR_SENHA = 90;
		return !isSenhaExpirada(DIAS_PARA_EXPIRAR_SENHA);
	}

	@Override
	public boolean isEnabled() {
		return this.ativo != null && this.ativo;
	}
	
	/**
     * Ativa o usuário
     */
    public void ativar() {
        this.ativo = true;
        this.updatedAt = LocalDateTime.now();
    }

    /**
     * Desativa o usuário
     */
    public void desativar() {
        this.ativo = false;
        this.updatedAt = LocalDateTime.now();
    }
    
    /**
     * Atualiza as informações do usuário
     * 
     * @param nome Novo nome
     * @param email Novo email
     * @param perfil Novo perfil
     * @param abrangencia Nova abrangência
     */
    public void atualizarInformacoes(String nome, String email, Perfil perfil, Abrangencia abrangencia) {
        if (nome != null && !nome.trim().isEmpty()) {
            this.nome = nome;
        }
        if (email != null && !email.trim().isEmpty()) {
            this.email = email;
        }
        if (perfil != null) {
            this.perfil = perfil;
        }
        if (abrangencia != null) {
            this.abrangencia = abrangencia;
        }
        this.updatedAt = LocalDateTime.now();
    }
    
    /**
     * Verifica se o usuário tem perfil de administrador
     * 
     * @return true se o usuário for administrador, false caso contrário
     */
    public boolean isAdmin() {
        return this.perfil != null && 
               this.perfil.getNome() != null && 
               this.perfil.getNome().toUpperCase().contains("ADMIN");
    }
    
    /**
     * Verifica se a senha fornecida corresponde à senha armazenada
     * Utiliza BCrypt para comparação segura
     * 
     * @param rawPassword A senha em texto puro para verificar
     * @return true se a senha corresponder, false caso contrário
     */
    public boolean verificarSenha(String rawPassword) {
        if (rawPassword == null || password == null) {
            return false;
        }
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        return encoder.matches(rawPassword, password);
    }
    
    /**
     * Atualiza a senha do usuário, aplicando hash BCrypt
     * 
     * @param rawPassword A nova senha em texto puro
     */
    public void atualizarSenha(String rawPassword) {
        if (rawPassword == null || rawPassword.trim().isEmpty()) {
            throw new IllegalArgumentException("Nova senha não pode ser nula ou vazia");
        }
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        this.password = encoder.encode(rawPassword);
        this.passwordChangedAt = LocalDateTime.now();
        this.limparTokenRecuperacao(); // Limpa o token após a senha ser alterada
        this.updatedAt = LocalDateTime.now();
    }
    
    /**
     * Verifica se a senha do usuário precisa ser alterada com base na política de expiração
     * 
     * @param diasParaExpirar Número de dias após os quais a senha expira
     * @return true se a senha expirou e precisa ser alterada
     */
    public boolean isSenhaExpirada(int diasParaExpirar) {
        if (this.passwordChangedAt == null) {
            // Se nunca alterou a senha, considera a data de criação
            return this.createdAt.plusDays(diasParaExpirar).isBefore(LocalDateTime.now());
        }
        
        return this.passwordChangedAt.plusDays(diasParaExpirar).isBefore(LocalDateTime.now());
    }
    
    /**
     * Verifica se o login atual parece ser suspeito, com base na mudança de IP
     * 
     * @param currentIp IP atual do usuário tentando fazer login
     * @return true se o login parece suspeito (IP diferente do último login)
     */
    public boolean isLoginSuspeito(String currentIp) {
        // Se não tivermos registro de último IP, não é suspeito
        if (this.lastLoginIp == null || this.lastLoginIp.trim().isEmpty()) {
            return false;
        }
        
        // Se for o primeiro login registrado, não é suspeito
        if (this.lastLoginAt == null) {
            return false;
        }
        
        // Compara o IP atual com o último registrado
        return !this.lastLoginIp.equals(currentIp);
    }
    
    /**
     * Gera um token para recuperação de senha
     * O token expira após 24 horas
     * 
     * @return O token gerado
     */
    public String gerarTokenRecuperacaoSenha() {
        this.resetPasswordToken = UUID.randomUUID().toString();
        this.resetPasswordExpires = LocalDateTime.now().plusHours(24);
        this.updatedAt = LocalDateTime.now();
        return this.resetPasswordToken;
    }
    
    /**
     * Verifica se um token de recuperação de senha é válido
     * 
     * @param token O token a ser verificado
     * @return true se o token for válido e não expirado, false caso contrário
     */
    public boolean isTokenRecuperacaoValido(String token) {
        return this.resetPasswordToken != null && 
               this.resetPasswordToken.equals(token) && 
               this.resetPasswordExpires != null && 
               LocalDateTime.now().isBefore(this.resetPasswordExpires);
    }
    
    /**
     * Limpa o token de recuperação de senha
     */
    public void limparTokenRecuperacao() {
        this.resetPasswordToken = null;
        this.resetPasswordExpires = null;
        this.updatedAt = LocalDateTime.now();
    }
    
    /**
     * Registra um login bem-sucedido
     * Reseta contadores de tentativas de login e atualiza o timestamp de último login
     */
    public void registrarLoginBemSucedido() {
        this.lastLoginAt = LocalDateTime.now();
        this.loginAttempts = 0;
        this.accountLockedUntil = null;
        this.updatedAt = LocalDateTime.now();
    }
    
    /**
     * Registra um login bem-sucedido com informações de origem
     * 
     * @param ipAddress Endereço IP de origem do login
     * @param userAgent User-Agent do navegador/cliente
     */
    public void registrarLoginBemSucedido(String ipAddress, String userAgent) {
        this.lastLoginAt = LocalDateTime.now();
        this.lastLoginIp = ipAddress;
        this.lastLoginUserAgent = userAgent;
        this.loginAttempts = 0;
        this.accountLockedUntil = null;
        this.updatedAt = LocalDateTime.now();
    }
    
    /**
     * Registra uma tentativa de login falha
     * Incrementa contador e pode bloquear a conta temporariamente após múltiplas tentativas
     * 
     * @return true se a conta foi bloqueada, false caso contrário
     */
    public boolean registrarTentativaLoginFalha() {
        this.loginAttempts = (this.loginAttempts != null ? this.loginAttempts : 0) + 1;
        this.updatedAt = LocalDateTime.now();
        
        // Após 5 tentativas falhas, bloqueia a conta por 30 minutos
        if (this.loginAttempts >= 5) {
            this.accountLockedUntil = LocalDateTime.now().plusMinutes(30);
            return true;
        }
        
        return false;
    }
    
    /**
     * Verifica se a conta está temporariamente bloqueada devido a múltiplas tentativas de login falhas
     * 
     * @return true se a conta está bloqueada, false caso contrário
     */
    public boolean isContaBloqueadaTemporariamente() {
        return this.accountLockedUntil != null && 
               LocalDateTime.now().isBefore(this.accountLockedUntil);
    }
    
    /**
     * Desbloqueia uma conta bloqueada temporariamente
     */
    public void desbloquearConta() {
        this.loginAttempts = 0;
        this.accountLockedUntil = null;
        this.updatedAt = LocalDateTime.now();
    }
    
    /**
     * Gera um mapa com as diferenças entre o estado atual e outro usuário
     * Útil para auditar alterações
     * 
     * @param outro O outro usuário para comparar
     * @return Mapa com os campos alterados e seus novos valores
     */
    public Map<String, String> gerarMapaDiferencas(Usuario outro) {
        Map<String, String> diferencas = new HashMap<>();
        
        // Compara campos relevantes e adiciona ao mapa se forem diferentes
        if (!Objects.equals(this.nome, outro.getNome())) {
            diferencas.put(FIELD_NOME, outro.getNome());
        }
        
        if (!Objects.equals(this.email, outro.getEmail())) {
            diferencas.put(FIELD_EMAIL, outro.getEmail());
        }
        
        if (!Objects.equals(this.ativo, outro.isAtivo())) {
            diferencas.put(FIELD_ATIVO, String.valueOf(outro.isAtivo()));
        }
        
        if (this.perfil != null && outro.getPerfil() != null &&
            !Objects.equals(this.perfil.getId(), outro.getPerfil().getId())) {
            diferencas.put(FIELD_PERFIL, outro.getPerfil().getNome());
        }
        
        if (this.abrangencia != null && outro.getAbrangencia() != null &&
            !Objects.equals(this.abrangencia.getId(), outro.getAbrangencia().getId())) {
            diferencas.put("abrangencia", outro.getAbrangencia().getNome());
        }
        
        // Não incluímos senha por segurança
        
        return diferencas;
    }
    
    /**
     * Cria uma versão JSON-friendly dos dados do usuário para logs
     * Omite dados sensíveis como senha
     * 
     * @return Um mapa com os principais dados do usuário
     */
    public Map<String, Object> toAuditLog() {
        Map<String, Object> auditLog = new HashMap<>();
        
        // Campos de identificação
        auditLog.put(FIELD_ID, this.id);
        auditLog.put(FIELD_USERNAME, this.username);
        auditLog.put(FIELD_NOME, this.nome);
        auditLog.put(FIELD_EMAIL, this.email);
        auditLog.put("cpf", this.cpf);
        auditLog.put(FIELD_ATIVO, this.ativo);
        
        // Campos de relacionamento
        if (this.perfil != null) {
            auditLog.put("perfilId", this.perfil.getId());
            auditLog.put("perfilNome", this.perfil.getNome());
        }
        
        if (this.empresa != null) {
            auditLog.put("empresaId", this.empresa.getId());
            auditLog.put("empresaNome", this.empresa.getName());
        }
        
        // Dados de login e acesso
        if (this.lastLoginAt != null) {
            auditLog.put("ultimoLogin", this.lastLoginAt.toString());
            auditLog.put("ultimoLoginIp", this.lastLoginIp);
        }
        
        // Dados de segurança
        if (this.passwordChangedAt != null) {
            auditLog.put("ultimaAlteracaoSenha", this.passwordChangedAt.toString());
        }
        
        if (this.loginAttempts != null && this.loginAttempts > 0) {
            auditLog.put("tentativasLogin", this.loginAttempts);
        }
        
        if (this.accountLockedUntil != null && LocalDateTime.now().isBefore(this.accountLockedUntil)) {
            auditLog.put("contaBloqueadaAte", this.accountLockedUntil.toString());
        }
        
        // Datas de auditoria
        auditLog.put("dataCriacao", this.createdAt.toString());
        auditLog.put("dataAtualizacao", this.updatedAt.toString());
        
        return auditLog;
    }
    
    /**
     * Verifica se o usuário deve receber notificação de segurança
     * (login suspeito, bloqueio de conta, etc)
     * 
     * @return true se alguma notificação de segurança deve ser enviada
     */
    public boolean deveReceberNotificacaoSeguranca() {
        return  // Se tiver tentativas de login falhas recentes
                (this.loginAttempts != null && this.loginAttempts >= 3) ||
                
                // Se a conta estiver bloqueada temporariamente
                isContaBloqueadaTemporariamente() ||
                
                // Se houve mudança de senha recente (últimas 24 horas)
                (this.passwordChangedAt != null && 
                this.passwordChangedAt.isAfter(LocalDateTime.now().minusHours(24)));
    }
    
    /**
     * Gera dados para notificação do usuário
     * Útil para envio de emails ou notificações push
     * 
     * @param tipoNotificacao O tipo de notificação a ser gerada
     * @return Um mapa com dados para a notificação
     */
    public Map<String, Object> gerarDadosNotificacao(String tipoNotificacao) {
        Map<String, Object> dados = new HashMap<>();
        
        // Dados básicos para qualquer notificação
        dados.put(FIELD_ID, this.id);
        dados.put(FIELD_NOME, this.nome);
        dados.put(FIELD_EMAIL, this.email);
        dados.put(FIELD_USERNAME, this.username);
        dados.put("tipo", tipoNotificacao);
        
        switch (tipoNotificacao.toUpperCase()) {
            case "SENHA_ALTERADA":
                dados.put("dataAlteracao", this.passwordChangedAt);
                break;
                
            case "CONTA_BLOQUEADA":
                dados.put("dataDesbloqueio", this.accountLockedUntil);
                dados.put("tentativas", this.loginAttempts);
                break;
                
            case "LOGIN_SUSPEITO":
                dados.put("ultimoLoginIp", this.lastLoginIp);
                dados.put("ultimoLogin", this.lastLoginAt);
                break;
                
            case "SENHA_EXPIRADA":
                long diasExpirados = 0;
                if (this.passwordChangedAt != null) {
                    diasExpirados = java.time.temporal.ChronoUnit.DAYS.between(
                        this.passwordChangedAt.toLocalDate(), 
                        LocalDateTime.now().toLocalDate());
                } else {
                    diasExpirados = java.time.temporal.ChronoUnit.DAYS.between(
                        this.createdAt.toLocalDate(), 
                        LocalDateTime.now().toLocalDate());
                }
                dados.put("diasExpirada", diasExpirados);
                break;
                
            default:
                // Caso padrão para tipos de notificação não reconhecidos
                dados.put("info", "Tipo de notificação não especificado");
                break;
        }
        
        return dados;
    }
    
    /**
     * Cria uma cópia do usuário para fins de auditoria
     * A cópia contém apenas os atributos relevantes para comparação
     * 
     * @return Um objeto Usuario com os dados atuais
     */
    public Usuario criarCopiaParaAuditoria() {
        return Usuario.builder()
            .id(this.id)
            .cpf(this.cpf)
            .nome(this.nome)
            .email(this.email)
            .username(this.username)
            // Não incluímos a senha
            .empresa(this.empresa)
            .perfil(this.perfil)
            .abrangencia(this.abrangencia)
            .ativo(this.ativo)
            .createdAt(this.createdAt)
            .build();
    }

	public Boolean isAtivo() {
		return ativo;
	}

	public void setAtivo(Boolean ativo) {
		this.ativo = Objects.requireNonNull(ativo, "Status ativo não pode ser nulo");
		this.updatedAt = LocalDateTime.now();
	}

	public LocalDateTime getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(LocalDateTime createdAt) {
		this.createdAt = Objects.requireNonNull(createdAt, "Data de criação não pode ser nula");
	}

	public LocalDateTime getUpdatedAt() {
		return updatedAt;
	}

	public void setUpdatedAt(LocalDateTime updatedAt) {
		this.updatedAt = Objects.requireNonNull(updatedAt, "Data de atualização não pode ser nula");
	}
    
    public String getResetPasswordToken() {
        return resetPasswordToken;
    }
    
    public void setResetPasswordToken(String resetPasswordToken) {
        this.resetPasswordToken = resetPasswordToken;
        this.updatedAt = LocalDateTime.now();
    }
    
    public LocalDateTime getResetPasswordExpires() {
        return resetPasswordExpires;
    }
    
    public void setResetPasswordExpires(LocalDateTime resetPasswordExpires) {
        this.resetPasswordExpires = resetPasswordExpires;
        this.updatedAt = LocalDateTime.now();
    }
    
    public LocalDateTime getLastLoginAt() {
        return lastLoginAt;
    }
    
    public void setLastLoginAt(LocalDateTime lastLoginAt) {
        this.lastLoginAt = lastLoginAt;
        this.updatedAt = LocalDateTime.now();
    }
    
    public String getLastLoginIp() {
        return lastLoginIp;
    }
    
    public void setLastLoginIp(String lastLoginIp) {
        this.lastLoginIp = lastLoginIp;
        this.updatedAt = LocalDateTime.now();
    }
    
    public String getLastLoginUserAgent() {
        return lastLoginUserAgent;
    }
    
    public void setLastLoginUserAgent(String lastLoginUserAgent) {
        this.lastLoginUserAgent = lastLoginUserAgent;
        this.updatedAt = LocalDateTime.now();
    }
    
    public LocalDateTime getPasswordChangedAt() {
        return passwordChangedAt;
    }
    
    public void setPasswordChangedAt(LocalDateTime passwordChangedAt) {
        this.passwordChangedAt = passwordChangedAt;
        this.updatedAt = LocalDateTime.now();
    }
    
    public Integer getLoginAttempts() {
        return loginAttempts != null ? loginAttempts : 0;
    }
    
    public void setLoginAttempts(Integer loginAttempts) {
        this.loginAttempts = loginAttempts;
        this.updatedAt = LocalDateTime.now();
    }
    
    public LocalDateTime getAccountLockedUntil() {
        return accountLockedUntil;
    }
    
    public void setAccountLockedUntil(LocalDateTime accountLockedUntil) {
        this.accountLockedUntil = accountLockedUntil;
        this.updatedAt = LocalDateTime.now();
    }

	public Boolean getActive() {
        return this.ativo;
    }
    public void setActive(Boolean ativo) {
        this.ativo = ativo;
    }
    public String getPasswordHash() {
        return this.passwordHash;
    }
	/**
	 * Especificação para filtrar usuários por vários campos
	 * 
	 * @param searchTerm Termo de busca para filtrar por nome, username, email, id ou CPF
	 * @param status Filtro de status: true para ativos, false para inativos, null para todos
	 * @param empresaId Filtrar por empresa específica, null para todas
	 * @param perfilId Filtrar por perfil específico, null para todos
	 * @return Specification para uso em queries dinâmicas
	 */
	public static Specification<Usuario> filterByFields(
            final String searchTerm, 
            final Boolean status, 
            final Long empresaId,
            final Long perfilId) {
            
		return (root, query, criteriaBuilder) -> {
			final List<Predicate> predicates = new ArrayList<>();

			// Filtragem por termo de busca
			if (searchTerm != null && !searchTerm.trim().isEmpty()) {
				final String likePattern = "%" + searchTerm.toLowerCase() + "%";
				final List<Predicate> searchPredicates = new ArrayList<>();

				// Adiciona predicados para campos de texto
				searchPredicates.add(criteriaBuilder.like(criteriaBuilder.lower(root.get(FIELD_NOME)), likePattern));
				searchPredicates.add(criteriaBuilder.like(criteriaBuilder.lower(root.get(FIELD_USERNAME)), likePattern));
				searchPredicates.add(criteriaBuilder.like(criteriaBuilder.lower(root.get(FIELD_EMAIL)), likePattern));

				// Tenta converter o termo para Long para buscar por ID e CPF
				try {
					final Long searchTermLong = Long.valueOf(searchTerm);
					searchPredicates.add(criteriaBuilder.equal(root.get("id"), searchTermLong));
					searchPredicates.add(criteriaBuilder.equal(root.get("cpf"), searchTermLong));
				} catch (NumberFormatException e) {
					// Ignora erro se a conversão falhar
				}

				predicates.add(criteriaBuilder.or(searchPredicates.toArray(Predicate[]::new)));
			}
            
            // Filtragem por status (ativo/inativo)
            if (status != null) {
                predicates.add(criteriaBuilder.equal(root.get(FIELD_ATIVO), status));
            }
            
            // Filtragem por empresa
            if (empresaId != null) {
                predicates.add(criteriaBuilder.equal(root.get("empresa").get("id"), empresaId));
            }
            
            // Filtragem por perfil
            if (perfilId != null) {
                predicates.add(criteriaBuilder.equal(root.get(FIELD_PERFIL).get("id"), perfilId));
            }

			return criteriaBuilder.and(predicates.toArray(Predicate[]::new));
		};
	}
    
    /**
     * Versão simplificada do método de filtro
     * 
     * @param searchTerm Termo de busca
     * @return Specification para filtragem
     */
    public static Specification<Usuario> filterByFields(final String searchTerm) {
        return filterByFields(searchTerm, null, null, null);
    }
	
	/**
	 * Builder para criação de usuários
	 */
	public static class Builder {
		private Long id;
		private Long cpf;
		private String nome;
		private String username;
		private String password;
		private String email;
		private Empresa empresa;
		private Perfil perfil;
		private Abrangencia abrangencia;
		private Boolean ativo;
        private String resetPasswordToken;
        private LocalDateTime resetPasswordExpires;
        private LocalDateTime lastLoginAt;
        private String lastLoginIp;
        private String lastLoginUserAgent;
        private Integer loginAttempts;
        private LocalDateTime accountLockedUntil;
        private LocalDateTime passwordChangedAt;
		private LocalDateTime createdAt;
		private LocalDateTime updatedAt;
		
		/**
		 * Construtor padrão que inicializa um builder vazio
		 */
		public Builder() {
			// Construtor vazio para iniciar o processo de construção
		}
		
		public Builder id(Long id) {
			this.id = id;
			return this;
		}
		
		public Builder cpf(Long cpf) {
			this.cpf = cpf;
			return this;
		}
		
		public Builder nome(String nome) {
			this.nome = nome;
			return this;
		}
		
		public Builder username(String username) {
			this.username = username;
			return this;
		}
		
		public Builder password(String password) {
			this.password = password;
			return this;
		}
		
		public Builder email(String email) {
			this.email = email;
			return this;
		}
		
		public Builder empresa(Empresa empresa) {
			this.empresa = empresa;
			return this;
		}
		
		public Builder perfil(Perfil perfil) {
			this.perfil = perfil;
			return this;
		}
		
		public Builder abrangencia(Abrangencia abrangencia) {
			this.abrangencia = abrangencia;
			return this;
		}
		
		public Builder ativo(Boolean ativo) {
			this.ativo = ativo;
			return this;
		}
		
		public Builder createdAt(LocalDateTime createdAt) {
			this.createdAt = createdAt;
			return this;
		}
		
		public Builder updatedAt(LocalDateTime updatedAt) {
			this.updatedAt = updatedAt;
			return this;
		}
        
        public Builder resetPasswordToken(String resetPasswordToken) {
            this.resetPasswordToken = resetPasswordToken;
            return this;
        }
        
        public Builder resetPasswordExpires(LocalDateTime resetPasswordExpires) {
            this.resetPasswordExpires = resetPasswordExpires;
            return this;
        }
        
        public Builder lastLoginAt(LocalDateTime lastLoginAt) {
            this.lastLoginAt = lastLoginAt;
            return this;
        }
        
        public Builder loginAttempts(Integer loginAttempts) {
            this.loginAttempts = loginAttempts;
            return this;
        }
        
        public Builder accountLockedUntil(LocalDateTime accountLockedUntil) {
            this.accountLockedUntil = accountLockedUntil;
            return this;
        }
        
        public Builder lastLoginIp(String lastLoginIp) {
            this.lastLoginIp = lastLoginIp;
            return this;
        }
        
        public Builder lastLoginUserAgent(String lastLoginUserAgent) {
            this.lastLoginUserAgent = lastLoginUserAgent;
            return this;
        }
        
        public Builder passwordChangedAt(LocalDateTime passwordChangedAt) {
            this.passwordChangedAt = passwordChangedAt;
            return this;
        }
		
		public Usuario build() {
			// Validações obrigatórias
			if (nome == null || nome.trim().isEmpty()) {
				throw new IllegalArgumentException("Nome não pode ser nulo ou vazio");
			}
			if (username == null || username.trim().isEmpty()) {
				throw new IllegalArgumentException("Username não pode ser nulo ou vazio");
			}
			if (password == null || password.trim().isEmpty()) {
                throw new IllegalArgumentException("Senha não pode ser nula ou vazia");
            }
			if (email == null || email.trim().isEmpty()) {
				throw new IllegalArgumentException("Email não pode ser nulo ou vazio");
			}
			if (empresa == null) {
				throw new IllegalArgumentException("Empresa não pode ser nula");
			}
			if (perfil == null) {
				throw new IllegalArgumentException("Perfil não pode ser nulo");
			}
			if (abrangencia == null) {
				throw new IllegalArgumentException("Abrangência não pode ser nula");
			}
			
			return new Usuario(this);
		}
	}
}
