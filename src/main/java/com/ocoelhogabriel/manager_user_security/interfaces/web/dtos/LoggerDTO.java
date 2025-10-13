package com.ocoelhogabriel.manager_user_security.interfaces.web.dtos;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ocoelhogabriel.manager_user_security.domain.constraints.MessageConstraints;
import com.ocoelhogabriel.manager_user_security.domain.value_objects.LoggerEnum;
import com.ocoelhogabriel.manager_user_security.infrastructure.adapters.persistence.entities.LoggerEntity;
import com.ocoelhogabriel.manager_user_security.utils.Utils;

public class LoggerDTO {
	private final Logger logger = LoggerFactory.getLogger(getClass());

	private String data;
	private LoggerEnum tipoLogger;
	private String mensagem;

	public static String consultaPagable(String value) {
		if (value == null) {
			throw new IllegalArgumentException(MessageConstraints.VALIDATION_REQUIRED_FIELD);
		}
		
		try {
			return switch (value.toUpperCase()) {
				case "DATA" -> "logdat";
				case "TIPOLOGGER" -> "logtip";
				default -> {
					Logger staticLogger = LoggerFactory.getLogger(LoggerDTO.class);
					if (staticLogger.isWarnEnabled()) {
						staticLogger.warn(MessageConstraints.VALIDATION_INVALID_FORMAT, "campo de ordenação");
					}
					throw new IllegalArgumentException(MessageConstraints.MessageFormatter.format(
						MessageConstraints.VALIDATION_INVALID_FORMAT, "campo de ordenação"));
				}
			};
		} catch (Exception e) {
			Logger staticLogger = LoggerFactory.getLogger(LoggerDTO.class);
			if (staticLogger.isErrorEnabled()) {
				staticLogger.error(MessageConstraints.LOG_OPERATION_ERROR, "consulta paginada", e.getMessage(), e);
			}
			throw e;
		}
	}

	public String getData() {
		return data;
	}

	public void setData(String data) {
		this.data = data;
	}

	public LoggerEnum getTipoLogger() {
		return tipoLogger;
	}

	public void setTipoLogger(LoggerEnum tipoLogger) {
		this.tipoLogger = tipoLogger;
	}

	public String getMensagem() {
		return mensagem;
	}

	public void setMensagem(String mensagem) {
		this.mensagem = mensagem;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("LoggerDTO [");
		if (logger != null) {
			builder.append("logger=").append(logger).append(", ");
		}
		if (data != null) {
			builder.append("data=").append(data).append(", ");
		}
		if (tipoLogger != null) {
			builder.append("tipoLogger=").append(tipoLogger).append(", ");
		}
		if (mensagem != null) {
			builder.append("mensagem=").append(mensagem);
		}
		builder.append("]");
		return builder.toString();
	}

	public LoggerDTO(String data, LoggerEnum tipoLogger, String mensagem) {
		super();
		this.data = data;
		this.tipoLogger = tipoLogger;
		this.mensagem = mensagem;
	}

	public LoggerDTO(LoggerEntity pend) {
		try {
			if (pend == null) {
				if (logger.isWarnEnabled()) {
					logger.warn(MessageConstraints.VALIDATION_REQUIRED_FIELD, "loggerEntity");
				}
				return;
			}
			
			LoggerEnum enumLogger = LoggerEnum.valueOf(pend.getLogtip());
			this.data = Utils.dateToString(pend.getLogdat());
			this.tipoLogger = enumLogger;
			this.mensagem = pend.getLogmsg();
		} catch (Exception e) {
			if (logger.isErrorEnabled()) {
				logger.error(MessageConstraints.LOG_OPERATION_ERROR, "construção do LoggerDTO", e.getMessage(), e);
			}
		}
	}

	public LoggerDTO() {
		super();

	}

}
