# Guia de Segurança

Este documento descreve as melhores práticas de segurança implementadas no Manager User Security, bem como recomendações para garantir a proteção de dados e a integridade do sistema.

## 1. Autenticação e Autorização

### 1.1. Autenticação com JWT

O sistema utiliza JSON Web Tokens (JWT) para autenticação stateless. As seguintes práticas são implementadas:

- Tokens com tempo de expiração curto (15-30 minutos)
- Refresh tokens para renovação de acesso
- Assinatura de tokens com algoritmo HMAC-SHA256
- Validação completa de claims em cada requisição
- Blacklist de tokens revogados

#### Implementação Segura de JWT

```java
@Service
public class JwtTokenServiceImpl implements JwtTokenService {

    @Value("${jwt.secret}")
    private String jwtSecret;
    
    @Value("${jwt.expiration}")
    private Long jwtExpiration;
    
    @Autowired
    private TokenBlacklistRepository tokenBlacklistRepository;
    
    public String generateToken(Authentication authentication) {
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        
        Map<String, Object> claims = new HashMap<>();
        claims.put("roles", userDetails.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toList()));
        
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(userDetails.getUsername())
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + jwtExpiration))
                .signWith(Keys.hmacShaKeyFor(jwtSecret.getBytes()), SignatureAlgorithm.HS256)
                .compact();
    }
    
    public boolean validateToken(String token) {
        try {
            if (tokenBlacklistRepository.isBlacklisted(token)) {
                return false;
            }
            
            Jwts.parserBuilder()
                    .setSigningKey(Keys.hmacShaKeyFor(jwtSecret.getBytes()))
                    .build()
                    .parseClaimsJws(token);
                    
            return true;
        } catch (Exception e) {
            return false;
        }
    }
    
    public void revokeToken(String token) {
        tokenBlacklistRepository.addToBlacklist(token);
    }
}
```

### 1.2. Controle de Acesso Baseado em Perfis (RBAC)

O sistema implementa RBAC com os seguintes componentes:

- **Roles**: Papéis gerais como USER, ADMIN, MANAGER
- **Permissions**: Permissões granulares para operações específicas
- **Security Expressions**: Verificações personalizadas com Spring Security

#### Configuração do RBAC

```java
@Configuration
@EnableMethodSecurity
public class SecurityConfig {
    
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .csrf(csrf -> csrf.disable())
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/api/v1/auth/**").permitAll()
                .requestMatchers("/api/v1/public/**").permitAll()
                .requestMatchers("/actuator/health", "/actuator/info").permitAll()
                .requestMatchers("/actuator/**").hasRole("ADMIN")
                .requestMatchers("/api/v1/admin/**").hasRole("ADMIN")
                .requestMatchers("/api/v1/manager/**").hasAnyRole("ADMIN", "MANAGER")
                .anyRequest().authenticated()
            )
            .sessionManagement(session -> session
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            );
            
        return http.build();
    }
}
```

#### Verificações de Permissão em Nível de Método

```java
@RestController
@RequestMapping("/api/v1/usuarios")
public class UsuarioController {

    @PreAuthorize("hasRole('ADMIN') or @securityService.isResourceOwner(authentication, #id)")
    @GetMapping("/{id}")
    public ResponseEntity<UsuarioDTO> getUsuario(@PathVariable Long id) {
        // Implementação
    }
    
    @PreAuthorize("hasAuthority('USER_WRITE')")
    @PostMapping
    public ResponseEntity<UsuarioDTO> createUsuario(@Valid @RequestBody UsuarioDTO usuarioDTO) {
        // Implementação
    }
}
```

## 2. Proteção de Dados Sensíveis

### 2.1. Criptografia de Senhas

Senhas são armazenadas utilizando algoritmos de hash seguros:

```java
@Configuration
public class PasswordEncoderConfig {
    
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(12);
    }
}
```

### 2.2. Proteção de Dados em Trânsito

- Todo tráfego deve usar HTTPS/TLS 1.3
- Certificados confiáveis (não auto-assinados em produção)
- HSTS (HTTP Strict Transport Security) habilitado

### 2.3. Proteção de Dados em Repouso

- Criptografia de dados sensíveis no banco de dados
- Utilização de campos criptografados para informações pessoais sensíveis
- Mascaramento de dados sensíveis em logs e respostas da API

```java
@Entity
@Table(name = "usuarios")
public class UsuarioEntity {

    @Convert(converter = AttributeEncryptor.class)
    @Column(name = "documento")
    private String documento;
    
    // Outros campos
}

@Component
public class AttributeEncryptor implements AttributeConverter<String, String> {

    private static final String AES = "AES";
    private final Key key;
    private final Cipher cipher;

    public AttributeEncryptor(@Value("${encryption.key}") String secretKey) throws Exception {
        key = new SecretKeySpec(secretKey.getBytes(), AES);
        cipher = Cipher.getInstance(AES);
    }

    @Override
    public String convertToDatabaseColumn(String attribute) {
        try {
            cipher.init(Cipher.ENCRYPT_MODE, key);
            return Base64.getEncoder().encodeToString(
                    cipher.doFinal(attribute.getBytes()));
        } catch (Exception e) {
            throw new IllegalStateException("Error encrypting", e);
        }
    }

    @Override
    public String convertToEntityAttribute(String dbData) {
        try {
            cipher.init(Cipher.DECRYPT_MODE, key);
            return new String(cipher.doFinal(
                    Base64.getDecoder().decode(dbData)));
        } catch (Exception e) {
            throw new IllegalStateException("Error decrypting", e);
        }
    }
}
```

## 3. Proteção Contra Ataques Comuns

### 3.1. Prevenção contra Injeção SQL

- Uso de JPA/Hibernate com consultas parametrizadas
- Validação de entrada de dados
- Princípio do menor privilégio para o usuário do banco de dados

### 3.2. Prevenção contra XSS

- Codificação de saída usando ResponseBodyAdvice
- Content Security Policy (CSP) configurado
- Validação de entrada rigorosa

```java
@ControllerAdvice
public class XssProtectionAdvice implements ResponseBodyAdvice<Object> {

    @Override
    public boolean supports(MethodParameter returnType, Class<? extends HttpMessageConverter<?>> converterType) {
        return true;
    }

    @Override
    public Object beforeBodyWrite(Object body, MethodParameter returnType, MediaType selectedContentType,
                                 Class<? extends HttpMessageConverter<?>> selectedConverterType,
                                 ServerHttpRequest request, ServerHttpResponse response) {
        
        // Adiciona cabeçalhos de segurança
        response.getHeaders().add("X-Content-Type-Options", "nosniff");
        response.getHeaders().add("X-Frame-Options", "DENY");
        response.getHeaders().add("X-XSS-Protection", "1; mode=block");
        
        // Configura Content Security Policy
        response.getHeaders().add("Content-Security-Policy", 
            "default-src 'self'; script-src 'self'; object-src 'none'; frame-ancestors 'none'");
        
        return body;
    }
}
```

### 3.3. Proteção contra CSRF

Em APIs REST com autenticação baseada em token, a proteção CSRF pode ser desativada com segurança, pois os tokens JWT já fornecem proteção contra CSRF. No entanto, se houver partes do sistema que usam autenticação baseada em cookies, a proteção CSRF deve ser ativada para essas partes:

```java
@Configuration
public class WebSecurityConfig {
    
    @Bean
    public SecurityFilterChain apiSecurityFilterChain(HttpSecurity http) throws Exception {
        http
            .securityMatcher("/api/**")
            .csrf(csrf -> csrf.disable()) // Desativado para API REST com JWT
            // Outras configurações
            ;
        
        return http.build();
    }
    
    @Bean
    public SecurityFilterChain webSecurityFilterChain(HttpSecurity http) throws Exception {
        http
            .securityMatcher("/web/**")
            .csrf(csrf -> csrf.csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse()))
            // Outras configurações
            ;
        
        return http.build();
    }
}
```

### 3.4. Proteção contra Ataques de Força Bruta

- Limitação de taxa de tentativas de login
- Bloqueio temporário de contas após várias tentativas malsucedidas
- CAPTCHAs para proteger formulários de login

```java
@Service
public class LoginAttemptService {

    private final int MAX_ATTEMPT = 5;
    private LoadingCache<String, Integer> attemptsCache;
    
    public LoginAttemptService() {
        attemptsCache = CacheBuilder.newBuilder()
                .expireAfterWrite(1, TimeUnit.DAYS)
                .build(new CacheLoader<String, Integer>() {
                    @Override
                    public Integer load(String key) {
                        return 0;
                    }
                });
    }
    
    public void loginSucceeded(String key) {
        attemptsCache.invalidate(key);
    }
    
    public void loginFailed(String key) {
        int attempts;
        try {
            attempts = attemptsCache.get(key);
        } catch (ExecutionException e) {
            attempts = 0;
        }
        attempts++;
        attemptsCache.put(key, attempts);
    }
    
    public boolean isBlocked(String key) {
        try {
            return attemptsCache.get(key) >= MAX_ATTEMPT;
        } catch (ExecutionException e) {
            return false;
        }
    }
}
```

## 4. Logs de Segurança e Auditoria

### 4.1. Logging de Eventos de Segurança

Eventos de segurança importantes são registrados com informações relevantes:

```java
@Aspect
@Component
public class SecurityAuditAspect {
    
    private static final Logger logger = LoggerFactory.getLogger(SecurityAuditAspect.class);
    
    @Pointcut("execution(* com.ocoelhogabriel.manager_user_security.infrastructure.security.*.*(..))")
    private void securityMethods() {}
    
    @AfterReturning(pointcut = "execution(* com.ocoelhogabriel.manager_user_security.infrastructure.security.AuthenticationService.authenticate(..))", returning = "result")
    public void logSuccessfulLogin(JoinPoint joinPoint, Object result) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        logger.info("Successful authentication: user={}, roles={}, remoteIp={}", 
            auth.getName(), 
            auth.getAuthorities(), 
            getCurrentRequest().getRemoteAddr());
    }
    
    @AfterThrowing(pointcut = "execution(* com.ocoelhogabriel.manager_user_security.infrastructure.security.AuthenticationService.authenticate(..))", throwing = "ex")
    public void logFailedLogin(JoinPoint joinPoint, Exception ex) {
        Object[] args = joinPoint.getArgs();
        String username = "";
        if (args.length > 0 && args[0] instanceof AuthenticationRequest) {
            username = ((AuthenticationRequest) args[0]).getUsername();
        }
        
        logger.warn("Failed authentication attempt: user={}, error={}, remoteIp={}", 
            username, 
            ex.getMessage(),
            getCurrentRequest().getRemoteAddr());
    }
    
    private HttpServletRequest getCurrentRequest() {
        return ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();
    }
}
```

### 4.2. Trilha de Auditoria

O sistema mantém uma trilha de auditoria para todas as operações sensíveis:

```java
@Entity
@Table(name = "audit_log")
public class AuditLogEntity {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "timestamp")
    private LocalDateTime timestamp;
    
    @Column(name = "user_id")
    private String userId;
    
    @Column(name = "action")
    private String action;
    
    @Column(name = "entity_type")
    private String entityType;
    
    @Column(name = "entity_id")
    private String entityId;
    
    @Column(name = "details", length = 4000)
    private String details;
    
    @Column(name = "ip_address")
    private String ipAddress;
    
    // Getters, Setters, etc.
}
```

## 5. Configuração Segura

### 5.1. Variáveis de Ambiente Seguras

Informações sensíveis como senhas e chaves são armazenadas em variáveis de ambiente ou secrets, nunca hardcoded:

```yaml
# application.yml
spring:
  datasource:
    url: jdbc:postgresql://${POSTGRES_HOST:localhost}:${POSTGRES_PORT:5432}/${POSTGRES_DB:manager_user_security}
    username: ${POSTGRES_USER:postgres}
    password: ${POSTGRES_PASSWORD}
  
jwt:
  secret: ${JWT_SECRET}
  expiration: ${JWT_EXPIRATION:86400000}
  
encryption:
  key: ${ENCRYPTION_KEY}
```

### 5.2. Atualizações de Segurança

- Dependências atualizadas regularmente para incorporar correções de segurança
- Uso de Dependabot ou similar para alertas de segurança
- Política de atualizações de emergência para vulnerabilidades críticas

## 6. Segurança em Ambiente de Produção

### 6.1. Hardening do Servidor

- Apenas portas necessárias abertas
- Princípio do menor privilégio para todos os usuários e serviços
- Updates de segurança do sistema operacional aplicados regularmente
- Firewall corretamente configurado

### 6.2. Monitoramento e Detecção de Intrusões

- Monitoramento de tentativas de login suspeitas
- Alertas para comportamentos anormais
- Integração com SIEM (Security Information and Event Management)

## 7. Política de Resposta a Incidentes

### 7.1. Plano de Resposta

1. **Detecção**: Identificação do incidente de segurança
2. **Contenção**: Limitação do impacto do incidente
3. **Erradicação**: Remoção da causa raiz
4. **Recuperação**: Restauração dos sistemas afetados
5. **Lições Aprendidas**: Análise e melhorias

### 7.2. Contatos de Emergência

- Equipe de resposta a incidentes: security-team@example.com
- Gerente de segurança: security-manager@example.com
- Número de emergência: +XX XX XXXX-XXXX

## 8. Testes de Segurança

### 8.1. Teste de Penetração

- Testes de penetração regulares (a cada 6 meses)
- Resolução de vulnerabilidades identificadas
- Análise de código estática para segurança

### 8.2. Varredura de Vulnerabilidades

- Varreduras automatizadas semanais
- Análise de dependências com ferramentas como OWASP Dependency Check
- Verificação de conformidade com padrões de segurança

## 9. Diretrizes de Desenvolvimento Seguro

### 9.1. Revisão de Código de Segurança

Todo código que implementa funcionalidades relacionadas à segurança deve passar por uma revisão específica de segurança antes de ser mesclado:

- Verificação de vulnerabilidades comuns
- Validação adequada de entrada
- Tratamento adequado de erros
- Sem exposição de informações sensíveis

### 9.2. Lista de Verificação de Segurança

- [ ] Validação adequada de entrada para todos os dados fornecidos pelo usuário
- [ ] Criptografia para dados sensíveis
- [ ] Controle de acesso apropriado para todos os endpoints
- [ ] Gerenciamento seguro de sessão/token
- [ ] Registro adequado de eventos de segurança
- [ ] Tratamento de erros sem vazamento de informações sensíveis
- [ ] Proteção contra vulnerabilidades comuns (OWASP Top 10)

## Referências

- [OWASP Top 10](https://owasp.org/www-project-top-ten/)
- [NIST Cybersecurity Framework](https://www.nist.gov/cyberframework)
- [Spring Security Documentation](https://docs.spring.io/spring-security/reference/index.html)
- [JWT Best Practices](https://auth0.com/blog/a-look-at-the-latest-draft-for-jwt-bcp/)
- [OWASP Secure Coding Practices](https://owasp.org/www-project-secure-coding-practices-quick-reference-guide/)
