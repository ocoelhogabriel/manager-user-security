package com.ocoelhogabriel.manager_user_security.handler;

import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

import com.ocoelhogabriel.manager_user_security.services.impl.PerfilPermissaoServiceImpl;
import com.ocoelhogabriel.manager_user_security.services.impl.RecursoServiceImpl;

@Configuration
public class PermissaoHandler {

	@Autowired
	private RecursoServiceImpl recursoService;
	@Autowired
	private PerfilPermissaoServiceImpl perfilPermissaoService;

	public boolean checkPermission(String perfil, URLValidator urlValidator, String method) {
		String nomeRecurso = urlValidator.getRecursoMapEnum().getNome();
		Objects.requireNonNull(nomeRecurso, "Nome do recurso está nulo");
		Objects.requireNonNull(perfil, "Perfil está nulo");
		Objects.requireNonNull(urlValidator, "UrlValidator está nulo");

		var recursoEntity = recursoService.findByIdEntity(nomeRecurso);
		var perfilEntity = perfilPermissaoService.findByIdPerfilEntity(perfil);
		var entity = perfilPermissaoService.findByPerfilAndRecurso(perfilEntity, recursoEntity);

		switch (method) {
		case "GET" -> {
			if (urlValidator.getMessage().equalsIgnoreCase("BUSCAR")) {
				if (entity.getPembus() == 1)
					return true;
			} else {
				if (entity.getPemlis() == 1)
					return true;
			}
		}
		case "POST" -> {
			if (entity.getPemcri() == 1)
				return true;
		}
		case "PUT" -> {
			if (entity.getPemedi() == 1)
				return true;
		}
		case "DELETE" -> {
			if (entity.getPemdel() == 1)
				return true;
		}
		default -> {
			return false;
		}
		}
		return false;
	}

}
