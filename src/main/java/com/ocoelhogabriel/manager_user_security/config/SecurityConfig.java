package com.ocoelhogabriel.manager_user_security.config;

import static org.springframework.security.config.http.SessionCreationPolicy.STATELESS;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.ocoelhogabriel.manager_user_security.exception.CustomAccessDeniedHandler;
import com.ocoelhogabriel.manager_user_security.exception.CustomAuthenticationEntryPoint;

import io.swagger.v3.oas.annotations.enums.SecuritySchemeIn;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.security.SecurityScheme;

@Configuration
@EnableWebSecurity
@SecurityScheme(name = "bearerAuth", description = "JWT Authentication", type = SecuritySchemeType.HTTP, scheme = "bearer", bearerFormat = "JWT", in = SecuritySchemeIn.HEADER)
public class SecurityConfig {

	// Lista de URLs públicas que não requerem autenticação
	private static final String[] WHITE_LIST_URL = {
			"/api/autenticacao/v1/**", "/v2/api-docs", "/v3/api-docs", "/v3/api-docs/**", "/swagger-resources",
			"/swagger-resources/**", "/configuration/ui", "/configuration/security", "/swagger-ui/**", "/webjars/**",
			"/swagger-ui.html", "/swagger-ui/index.html", "/actuator/health" };

	@Bean
	SecurityFilterChain filterChain(JWTAuthFilter authFilter, 
								   CustomAccessDeniedHandler accessDeniedHandler,
								   CustomAuthenticationEntryPoint authenticationEntryPoint,
								   HttpSecurity http) throws Exception {
		http.csrf(csrf -> csrf.disable())
				// Filtro de requisição
				.addFilterBefore(authFilter, UsernamePasswordAuthenticationFilter.class)
				.authorizeHttpRequests(requests -> requests
						// list white request
						.requestMatchers(WHITE_LIST_URL).permitAll()
						// .anyRequest().permitAll())
						.anyRequest().authenticated())
				// Sessão
				.sessionManagement(session -> session.sessionCreationPolicy(STATELESS))
				// Lidar com Exceções
				.exceptionHandling(exceptionHandling -> exceptionHandling
						.accessDeniedHandler(accessDeniedHandler)
						.authenticationEntryPoint(authenticationEntryPoint));

		return http.build();
	}

	@Bean
	public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
		return authenticationConfiguration.getAuthenticationManager();
	}

	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
}
