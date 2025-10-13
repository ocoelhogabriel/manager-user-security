package com.ocoelhogabriel.manager_user_security.model.enums;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public enum StatusDeviceEnum {
	ONLINE("ONLINE"), OFFLINE("OFFLINE"), ATIVO("ATIVO"), INATIVO("INATIVO");

	private static final Logger logger = LoggerFactory.getLogger(StatusDeviceEnum.class);
	private final String status;

	StatusDeviceEnum(String status) {
		this.status = status;
	}

	public String getStatus() {
		return status;
	}

	public static String mapDescricaoToStatusDeviceString(String descricao) {
		for (StatusDeviceEnum du : StatusDeviceEnum.values()) {
			if (du.getStatus().equalsIgnoreCase(descricao)) {
				return du.getStatus();
			}
		}
		logger.error("Descrição não mapeada: " + descricao);
		return null;
	}

	public static StatusDeviceEnum mapDescricaoToStatusDevice(String descricao) {
		for (StatusDeviceEnum du : StatusDeviceEnum.values()) {
			if (du.getStatus().equalsIgnoreCase(descricao)) {
				return du;
			}
		}
		logger.error("Descrição não mapeada: " + descricao);
		return null;
	}
}
