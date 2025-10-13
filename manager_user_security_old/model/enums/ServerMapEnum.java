package com.ocoelhogabriel.manager_user_security.model.enums;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public enum ServerMapEnum {
	SILO("/silo"), manager_user_security("/manager_user_security"), SIRENEAUTH("/sirene.auth"), SIRENEV1("/sirenev1");

	private static final Logger logger = LoggerFactory.getLogger(ServerMapEnum.class);
	private final String server;

	ServerMapEnum(String versao) {
		this.server = versao;
	}

	public String getServer() {
		return server;
	}

	public static String mapDescricaoToServer(String descricao) {
		for (ServerMapEnum du : ServerMapEnum.values()) {
			if (du.getServer().equalsIgnoreCase(descricao)) {
				return du.getServer();
			}
		}
		logger.error("Descrição não mapeada: " + descricao);
		return null;
	}

}
