package com.ocoelhogabriel.manager_user_security.handler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ocoelhogabriel.manager_user_security.model.enums.AcaoRecursoMapEnum;
import com.ocoelhogabriel.manager_user_security.model.enums.RecursoMapEnum;
import com.ocoelhogabriel.manager_user_security.model.enums.ServerMapEnum;
import com.ocoelhogabriel.manager_user_security.model.enums.VersaoMapEnum;

public class URLValidator {

	private static final Logger logger = LoggerFactory.getLogger(URLValidator.class);

	private RecursoMapEnum recursoMapEnum;
	private String message;

	public URLValidator(RecursoMapEnum recursoMapEnum, String message) {
		this.recursoMapEnum = recursoMapEnum;
		this.message = message;
	}

	public static URLValidator validateURL(String url, String method) {
		try {
			String[] parts = url.split("/");

			if (parts.length < 5) {
				return new URLValidator(null, "URL inválida!");
			}

			String serverPart = "/" + parts[1];
			String recursoPart = "/api/" + parts[3];
			String versionPart = "/" + parts[4];
			String actionPart = parts.length > 5 ? "/" + parts[5] : null;

			String server = ServerMapEnum.mapDescricaoToServer(serverPart.toUpperCase());
			String recurso = RecursoMapEnum.mapUrlToUrl(recursoPart.toUpperCase());
			RecursoMapEnum recursoEnum = RecursoMapEnum.mapUrlToRecursoMapEnum(recursoPart.toUpperCase());
			String version = VersaoMapEnum.mapDescricaoToVersao(versionPart.toUpperCase());
			String action = "";
			if (actionPart != null)
				action = AcaoRecursoMapEnum.mapDescricaoToAction(actionPart.toUpperCase());

			if (server == null || recurso == null || version == null) {
				return new URLValidator(null, "Erro no mapeamento da URL!");
			}

			if (method.equalsIgnoreCase("GET") && actionPart != null && action == null) {
				if (!actionPart.matches("/\\d+")) {
					return new URLValidator(recursoEnum, "URL inválida para ação BUSCAR: código ausente ou inválido.");
				}
				return new URLValidator(recursoEnum, "BUSCAR");
			}

			return new URLValidator(recursoEnum, "URL válida! Method: " + method + " Server: " + server + ", Recurso: "
					+ recurso + ", Versão: " + version + ", Ação: " + action);
		} catch (Exception e) {
			logger.error("Erro ao validar URL: " + url, e);
			return new URLValidator(null, "Erro ao processar a URL!");
		}
	}

	public RecursoMapEnum getRecursoMapEnum() {
		return recursoMapEnum;
	}

	public void setRecursoMapEnum(RecursoMapEnum recursoMapEnum) {
		this.recursoMapEnum = recursoMapEnum;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

}
