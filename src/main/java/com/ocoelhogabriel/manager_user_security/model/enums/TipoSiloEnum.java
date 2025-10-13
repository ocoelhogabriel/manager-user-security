package com.ocoelhogabriel.manager_user_security.model.enums;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public enum TipoSiloEnum {
	VERTICAL("VERTICAL"), HORIZONTAL("HORIZONTAL");

	private static final Logger logger = LoggerFactory.getLogger(TipoSiloEnum.class);
	private final String tipo;

	TipoSiloEnum(String tipo) {
		this.tipo = tipo;
	}

	public String getTipo() {
		return tipo;
	}

	public static String mapDescricaoToStatusDeviceString(String descricao) {
		for (TipoSiloEnum du : TipoSiloEnum.values()) {
			if (du.getTipo().equalsIgnoreCase(descricao)) {
				return du.getTipo();
			}
		}
		logger.error("Descrição não mapeada: " + descricao);
		return null;
	}

	public static TipoSiloEnum mapDescricaoToStatusDevice(String descricao) {
		for (TipoSiloEnum du : TipoSiloEnum.values()) {
			if (du.getTipo().equalsIgnoreCase(descricao)) {
				return du;
			}
		}
		logger.error("Descrição não mapeada: " + descricao);
		return null;
	}
}
