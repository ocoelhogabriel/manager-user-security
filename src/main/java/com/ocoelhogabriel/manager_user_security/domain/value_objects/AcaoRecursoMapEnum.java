package com.ocoelhogabriel.manager_user_security.domain.value_objects;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public enum AcaoRecursoMapEnum {

	AUTH("/auth"), 
	VALIDATE("/validate"), 
	AUTHVALIDATE("/auth-validate"), 
	CODIGO("/codigo"),
	BUSCARCNPJ("/buscar-cnpj"),
	CNPJ("/cnpj"),
	PAGINADO("/paginado"),
	PERMISSAO("/permissao"),
	LOGGER("/logger"),
	LISTAITENSABRANGENTES("/lista-items-abrangentes");

	private static final Logger logger = LoggerFactory.getLogger(AcaoRecursoMapEnum.class);
	private final String action;

	AcaoRecursoMapEnum(String versao) {
		this.action = versao;
	}

	public String getAction() {
		return action;
	}

	public static String mapDescricaoToAction(String descricao) {
		for (AcaoRecursoMapEnum du : AcaoRecursoMapEnum.values()) {
			if (descricao.toUpperCase().startsWith(du.getAction().toUpperCase())) {
				return du.getAction();
			}
		}
		logger.debug("Descrição não mapeada: " + descricao);
		return null;
	}

}
