package com.ocoelhogabriel.manager_user_security.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.lang.NonNull;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Value("${cors.allowed-origins:http://localhost:3000,http://localhost:8080}")
    private String[] allowedOrigins;

	@Override
	public void addCorsMappings(@NonNull CorsRegistry registry) {
		registry.addMapping("/**")
               .allowedOriginPatterns(allowedOrigins)
               .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
               .allowedHeaders("Authorization", "Content-Type", "Accept")
               .exposedHeaders("Authorization")
               .allowCredentials(true)
               .maxAge(3600); // 1 hora de cache para pre-flight requests
	}

	@Override
	public void addResourceHandlers(@NonNull ResourceHandlerRegistry registry) {
		registry.addResourceHandler("/**").addResourceLocations("classpath:/static/");
	}

	@Override
	public void addViewControllers(@NonNull ViewControllerRegistry registry) {
		registry.addViewController("/").setViewName("forward:/index.html");
		registry.addRedirectViewController("/", "/conex/swagger-ui/");
	}
}
