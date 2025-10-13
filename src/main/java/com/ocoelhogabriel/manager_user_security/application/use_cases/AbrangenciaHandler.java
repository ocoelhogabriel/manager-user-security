package com.ocoelhogabriel.manager_user_security.application.use_cases;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ocoelhogabriel.manager_user_security.application.services.AbrangenciaService;
import com.ocoelhogabriel.manager_user_security.application.services.RecursoService;
import com.ocoelhogabriel.manager_user_security.application.services.UsuarioService;
import com.ocoelhogabriel.manager_user_security.domain.constraints.MessageConstraints;
import com.ocoelhogabriel.manager_user_security.domain.constraints.MessageFormatterUtil;
import com.ocoelhogabriel.manager_user_security.domain.value_objects.CheckAbrangenciaRec;
import com.ocoelhogabriel.manager_user_security.infrastructure.adapters.persistence.entities.Usuario;

import jakarta.persistence.EntityNotFoundException;

@Component
public class AbrangenciaHandler {

	private static final Logger LOGGER = LoggerFactory.getLogger(AbrangenciaHandler.class);

	private final AbrangenciaService abrangenciaService;
	private final RecursoService recursoService;
	private final UsuarioService usuarioService;

	public AbrangenciaHandler(@Lazy AbrangenciaService abrangenciaService,
			@Lazy RecursoService recursoService,
			@Lazy UsuarioService usuarioService) {
		this.abrangenciaService = abrangenciaService;
		this.recursoService = recursoService;
		this.usuarioService = usuarioService;
	}

	public CheckAbrangenciaRec checkAbrangencia(String text) {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		String currentUserName = authentication.getName();
		Usuario user = usuarioService.findLoginEntity(currentUserName);

		if (user == null) {
			if (LOGGER.isWarnEnabled()) {
				LOGGER.warn(MessageFormatterUtil.format(MessageConstraints.AUTH_USER_NOT_FOUND, currentUserName));
			}
			throw new EntityNotFoundException(MessageFormatterUtil.format(MessageConstraints.USER_NOT_FOUND));
		}

		var recurso = recursoService.findByIdEntity(text);
		var abrangencia = abrangenciaService.findByAbrangenciaAndRecursoContainingAbrangencia(user.getAbrangencia(),
				recurso);

		if (abrangencia == null) {
			String errorMessage = "No coverage details found for user " + currentUserName
					+ " in resource " + text;
			if (LOGGER.isWarnEnabled()) {
				LOGGER.warn(errorMessage);
			}
			throw new IllegalArgumentException(errorMessage);
		}

		List<Long> ids;
		try {
			ids = new ObjectMapper().readValue(abrangencia.getDados(), new TypeReference<List<Long>>() {
			});
		} catch (JsonProcessingException e) {
			if (LOGGER.isErrorEnabled()) {
				LOGGER.error("Error retrieving coverage item: ", e);
			}
			ids = null;
		}
		return new CheckAbrangenciaRec(ids, abrangencia.getHierarquia());
	}

	public Long findIdAbrangenciaPermi(CheckAbrangenciaRec checkAbrangencia, Long codigo) {
		return checkAbrangencia.isHier() == 0 ? codigo
				: checkAbrangencia.listAbrangencia().stream().filter(map -> map.equals(codigo)).findFirst()
						.orElse(null);
	}

}
