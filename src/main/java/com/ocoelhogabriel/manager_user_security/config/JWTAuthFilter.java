package com.ocoelhogabriel.manager_user_security.config;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.lang.NonNull;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.auth0.jwt.exceptions.TokenExpiredException;
import com.ocoelhogabriel.manager_user_security.application.records.TokenDeviceRecord;
import com.ocoelhogabriel.manager_user_security.application.use_cases.PermissaoHandler;
import com.ocoelhogabriel.manager_user_security.application.use_cases.URLValidator;
import com.ocoelhogabriel.manager_user_security.domain.constraints.MessageConstraints;
import com.ocoelhogabriel.manager_user_security.domain.constraints.MessageFormatterUtil;
import com.ocoelhogabriel.manager_user_security.domain.services.AuthService;
import com.ocoelhogabriel.manager_user_security.exception.CustomAccessDeniedHandler;
import com.ocoelhogabriel.manager_user_security.utils.AuthDeviceUtil;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class JWTAuthFilter extends OncePerRequestFilter {

	private static final Logger LOGGER = LoggerFactory.getLogger(JWTAuthFilter.class);
	private static final String BEARER_PREFIX = "Bearer ";
	
	private final CustomAccessDeniedHandler accessDeniedHandler;
	private final AuthService authRepository;
	private final UserDetailsService userDetailsService;
	private final PermissaoHandler permissionHandler;

	/**
	 * Construtor para injeção de dependências
	 * 
	 * @param authRepository Serviço de autenticação
	 * @param userDetailsService Serviço para carregar detalhes do usuário
	 * @param permissionHandler Manipulador de permissões
	 * @param accessDeniedHandler Manipulador para acesso negado
	 */
	public JWTAuthFilter(AuthService authRepository, 
						UserDetailsService userDetailsService,
						PermissaoHandler permissionHandler,
						CustomAccessDeniedHandler accessDeniedHandler) {
		this.authRepository = authRepository;
		this.userDetailsService = userDetailsService;
		this.permissionHandler = permissionHandler;
		this.accessDeniedHandler = accessDeniedHandler;
	}

	@Override
	protected void doFilterInternal(@NonNull HttpServletRequest request, @NonNull HttpServletResponse response, @NonNull FilterChain filterChain) throws ServletException, IOException {
		try {
			TokenDeviceRecord token = this.recoverToken(request);
			String requestURI = request.getRequestURI();
			String method = request.getMethod();
			
			if (LOGGER.isDebugEnabled()) {
				LOGGER.debug("Processing request: {} {}", method, requestURI);
			}
			
			if (token != null && token.device() != null) {
				handleDeviceAuthentication(request, response, filterChain, requestURI, method);
				return;
			}

			if (token != null && token.token() != null) {
				handleTokenAuthentication(request, response, filterChain, token, requestURI, method);
				return;
			}
			
			filterChain.doFilter(request, response);
		} catch (TokenExpiredException e) {
			if (LOGGER.isInfoEnabled()) {
				LOGGER.info(MessageConstraints.SECURITY_TOKEN_EXPIRED);
			}
			accessDeniedHandler.handle(request, response, 
				new AccessDeniedException(MessageConstraints.SECURITY_TOKEN_EXPIRED));
		} catch (AccessDeniedException e) {
			if (LOGGER.isInfoEnabled()) {
				LOGGER.info(MessageFormatterUtil.format(MessageConstraints.SECURITY_AUTH_FAILURE, e.getMessage()));
			}
			accessDeniedHandler.handle(request, response, 
				new AccessDeniedException(MessageConstraints.SECURITY_ACCESS_DENIED));
		} catch (Exception e) {
			// Catch all other exceptions to prevent security filter chain from breaking
			if (LOGGER.isErrorEnabled()) {
				LOGGER.error("Unexpected error in authentication filter: {}", e.getMessage(), e);
			}
			accessDeniedHandler.handle(request, response,
				new AccessDeniedException(MessageConstraints.SECURITY_AUTH_FAILURE));
		}
	}

	private void handleDeviceAuthentication(HttpServletRequest request, HttpServletResponse response, 
										  FilterChain filterChain, String requestURI, String method) 
										  throws IOException, ServletException {
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("Handling device authentication for request: {} {}", method, requestURI);
		}
		
		UserDetails user = userDetailsService.loadUserByUsername("device");
		setAuthentication(user);
		
		if (!isAuthenticationValid()) {
			if (LOGGER.isInfoEnabled()) {
				LOGGER.info("Device authentication failed - invalid authentication");
			}
			denyAccess(request, response, MessageConstraints.SECURITY_ACCESS_DENIED);
			return;
		}
		
		URLValidator validationResponse = URLValidator.validateURL(requestURI, method);
		if (validationResponse == null || validationResponse.getRecursoMapEnum() == null) {
			if (LOGGER.isInfoEnabled()) {
				LOGGER.info("Device authentication failed - invalid URL validation: {}", requestURI);
			}
			denyAccess(request, response, MessageConstraints.SECURITY_ACCESS_DENIED);
			return;
		}
		
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("Device authentication successful for: {} {}", method, requestURI);
		}
		filterChain.doFilter(request, response);
	}

	private void handleTokenAuthentication(HttpServletRequest request, HttpServletResponse response, 
										 FilterChain filterChain, TokenDeviceRecord token, 
										 String requestURI, String method) throws IOException, ServletException {
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("Handling token authentication for request: {} {}", method, requestURI);
		}
		
		String login = authRepository.validToken(token.token());
		UserDetails user = userDetailsService.loadUserByUsername(login);
		setAuthentication(user);

		if (!isAuthenticationValid()) {
			if (LOGGER.isInfoEnabled()) {
				LOGGER.info("Token authentication failed - invalid authentication");
			}
			denyAccess(request, response, MessageConstraints.SECURITY_ACCESS_DENIED);
			return;
		}

		String role = getCurrentUserRole();
		URLValidator validationResponse = URLValidator.validateURL(requestURI, method);
		
		if (isInvalidUrlValidation(validationResponse)) {
			if (LOGGER.isInfoEnabled()) {
				LOGGER.info("Token authentication failed - invalid URL: {}", validationResponse.getMessage());
			}
			denyAccess(request, response, 
				MessageFormatterUtil.format(MessageConstraints.SECURITY_INVALID_URL, validationResponse.getMessage()));
			return;
		}
		
		if (!hasPermission(role, validationResponse, method)) {
			if (LOGGER.isInfoEnabled()) {
				LOGGER.info("Token authentication failed - permission denied for role {} on {} {}", 
					role, method, requestURI);
			}
			denyAccess(request, response, MessageConstraints.SECURITY_NOT_AUTHORIZED_ACTION);
			return;
		}
		
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("Token authentication successful for role {} on: {} {}", role, method, requestURI);
		}
		filterChain.doFilter(request, response);
	}

	private void setAuthentication(UserDetails user) {
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("Setting authentication for user: {}", user.getUsername());
		}
		UsernamePasswordAuthenticationToken authenticationToken = 
			new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities());
		SecurityContextHolder.getContext().setAuthentication(authenticationToken);
	}

	private boolean isAuthenticationValid() {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		return authentication != null && authentication.isAuthenticated();
	}

	private String getCurrentUserRole() {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		String role = authentication.getAuthorities().stream()
			.map(GrantedAuthority::getAuthority)
			.findFirst()
			.orElse(null);
			
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("Current user role: {}", role);
		}
		return role;
	}

	private boolean isInvalidUrlValidation(URLValidator validationResponse) {
		if (validationResponse == null) {
			return true;
		}
		
		String message = validationResponse.getMessage();
		boolean isInvalid = message.startsWith("Erro") || 
			   message.startsWith("Erro ao processar a URL!") || 
			   message.contains("código ausente ou inválido");
			   
		if (isInvalid && LOGGER.isDebugEnabled()) {
			LOGGER.debug(MessageConstraints.SECURITY_INVALID_URL, message);
		}
		
		return isInvalid;
	}

	private boolean hasPermission(String role, URLValidator validationResponse, String method) {
		boolean hasPermission = permissionHandler.checkPermission(role, validationResponse, method);
		
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("Permission check for role {} on resource {}: {}", 
				role, 
				validationResponse.getRecursoMapEnum() != null ? validationResponse.getRecursoMapEnum().name() : "null", 
				hasPermission ? "GRANTED" : "DENIED");
		}
		
		return hasPermission;
	}

	private void denyAccess(HttpServletRequest request, HttpServletResponse response, String message) 
			throws IOException {
		if (LOGGER.isInfoEnabled()) {
			LOGGER.info("Denying access: {} - {}", request.getRequestURI(), message);
		}
		accessDeniedHandler.handle(request, response, new AccessDeniedException(message));
	}

	private TokenDeviceRecord recoverToken(HttpServletRequest request) {
		String authHeader = request.getHeader("Authorization");
		if (authHeader == null) {
			return null;
		}

		String token = authHeader.replace(BEARER_PREFIX, "");
		var tokenDevice = AuthDeviceUtil.validarTokenBaseReturn(token);
		if (tokenDevice != null) {
			return new TokenDeviceRecord(null, tokenDevice.numeroSerie());
		}

		return new TokenDeviceRecord(token, null);
	}
}
