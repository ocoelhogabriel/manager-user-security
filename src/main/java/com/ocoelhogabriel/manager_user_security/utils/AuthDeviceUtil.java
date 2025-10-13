package com.ocoelhogabriel.manager_user_security.utils;

import java.io.IOException;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.ocoelhogabriel.manager_user_security.domain.constraints.MessageConstraints;
import com.ocoelhogabriel.manager_user_security.interfaces.web.dtos.AuthDeviceRecords;
import com.ocoelhogabriel.manager_user_security.interfaces.web.dtos.TokenValidationDeviceDTO;

@Component
public class AuthDeviceUtil {

	private static final Logger LOGGER = LoggerFactory.getLogger(AuthDeviceUtil.class);
	private static final DateTimeFormatter dtfPadrao = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
	private static final String SECRET = "sirene";
	private static final long TIME = 1440; // 24 hours in minutes

	public AuthDeviceUtil() {
	}

	public static String gerarKeyBase() {
		return Utils.encode(SECRET);
	}

	public static boolean validarKeyBase(String token) {
		String tokenDecoded = Utils.decode(token);
		return tokenDecoded.equals(SECRET);
	}

	public static boolean validarTokenBase(String token) {
		if (token == null || token.isBlank()) {
			LOGGER.warn(MessageConstraints.AUTH_TOKEN_NULL);
			return false;
		}

		String decodedToken = Utils.decode(token);

		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("Validando token decodificado: {}", decodedToken);
		}

		String[] parse = splitSenha(decodedToken, "_");
		if (parse.length < 3) {
			if (LOGGER.isDebugEnabled()) {
				LOGGER.debug("Token inválido: formato incorreto");
			}
			return false;
		}

		String secret = parse[1];
		String dateString = parse[2];

		if (!validarKeyBase(secret)) {
			if (LOGGER.isDebugEnabled()) {
				LOGGER.debug("Token inválido: chave base inválida");
			}
			return false;
		}

		LocalDateTime tokenTime;
		try {
			tokenTime = LocalDateTime.parse(dateString, dtfPadrao);
		} catch (Exception e) {
			if (LOGGER.isDebugEnabled()) {
				LOGGER.debug("Token inválido: formato de data inválido", e);
			}
			return false;
		}

		boolean tokenValid = !isTokenExpired(tokenTime, TIME);

		if (LOGGER.isDebugEnabled()) {
			if (tokenValid) {
				LOGGER.debug(MessageConstraints.AUTH_VALIDATION_SUCCESS);
			} else {
				LOGGER.debug(MessageConstraints.AUTH_VALIDATION_FAILED);
			}
		}

		return tokenValid;
	}

	public static String convertDateToString() {
		LocalDateTime localDateTime = LocalDateTime.ofInstant(new Date().toInstant(), ZoneId.systemDefault());
		return dtfPadrao.format(localDateTime);
	}

	public static String[] splitSenha(String senha, String typeSplit) {
		try {
			return senha.split(typeSplit);
		} catch (RuntimeException e) {
			return new String[0];
		}
	}

	public static boolean isValidDate(String dateString) throws IOException {
		try {
			if (LOGGER.isDebugEnabled()) {
				LOGGER.debug("Validando formato de data: {}", dateString);
			}
			LocalDateTime.parse(dateString, dtfPadrao);
			return true;
		} catch (Exception e) {
			if (LOGGER.isWarnEnabled()) {
				LOGGER.warn(MessageConstraints.VALIDATION_INVALID_FORMAT, "data");
			}
			throw new IOException(MessageConstraints.MessageFormatter.format(
					MessageConstraints.VALIDATION_INVALID_FORMAT, "data"));
		}
	}

	public static String gerarTokenBase(String senha) throws IOException {
		try {
			String[] parse = splitSenha(senha, "@");
			if (parse == null || parse.length < 2 || !isValidDate(parse[1])) {
				if (LOGGER.isWarnEnabled()) {
					LOGGER.warn(MessageConstraints.VALIDATION_INVALID_FORMAT, "senha");
				}
				throw new IllegalArgumentException(MessageConstraints.MessageFormatter.format(
						MessageConstraints.VALIDATION_INVALID_FORMAT, "senha"));
			}
			String token = parse[0] + "_" + gerarKeyBase() + "_" + parse[1];
			return Utils.encode(token);
		} catch (RuntimeException e) {
			if (LOGGER.isErrorEnabled()) {
				LOGGER.error("Erro ao gerar token base: {}", e.getMessage(), e);
			}
			return null;
		}
	}

	public static AuthDeviceRecords validarTokenBaseReturn(String token) {
		try {
			if (token == null || token.isBlank()) {
				if (LOGGER.isWarnEnabled()) {
					LOGGER.warn(MessageConstraints.AUTH_TOKEN_NULL);
				}
				return null;
			}

			String decodedToken = Utils.decode(token);
			if (LOGGER.isDebugEnabled()) {
				LOGGER.debug("Validando token decodificado: {}", decodedToken);
			}

			String[] parse = splitSenha(decodedToken, "_");
			if (parse.length < 3) {
				if (LOGGER.isDebugEnabled()) {
					LOGGER.debug("Token inválido: formato incorreto");
				}
				return null;
			}

			try {
				isValidDate(parse[2]);
			} catch (IOException e) {
				if (LOGGER.isDebugEnabled()) {
					LOGGER.debug("Token inválido: data em formato inválido", e);
				}
				return null;
			}

			String numeroSerie = parse[0];
			String secret = parse[1];
			String dateString = parse[2];

			if (!validarKeyBase(secret)) {
				if (LOGGER.isDebugEnabled()) {
					LOGGER.debug("Token inválido: chave base inválida");
				}
				return null;
			}

			LocalDateTime tokenTime = LocalDateTime.parse(dateString, dtfPadrao);
			if (isTokenExpired(tokenTime, TIME)) {
				if (LOGGER.isDebugEnabled()) {
					LOGGER.debug(MessageConstraints.SECURITY_TOKEN_EXPIRED);
				}
				throw new IOException(MessageConstraints.SECURITY_TOKEN_EXPIRED);
			}

			if (LOGGER.isDebugEnabled()) {
				LOGGER.debug(MessageConstraints.AUTH_VALIDATION_SUCCESS);
			}

			return new AuthDeviceRecords(numeroSerie, secret, dateString);
		} catch (IOException | IllegalArgumentException e) {
			if (LOGGER.isWarnEnabled()) {
				LOGGER.warn(MessageConstraints.AUTH_VALIDATION_FAILED, e);
			}
			return null;
		}
	}

	public static AuthDeviceRecords validarTokenBaseString(String token) throws IOException {
		if (token == null || token.isBlank()) {
			if (LOGGER.isWarnEnabled()) {
				LOGGER.warn(MessageConstraints.AUTH_TOKEN_NULL);
			}
			throw new IOException(MessageConstraints.AUTH_TOKEN_NULL);
		}

		String decodedToken = Utils.decode(token);
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("Validando token decodificado: {}", decodedToken);
		}

		String[] parse = splitSenha(decodedToken, "_");
		if (parse.length < 3) {
			if (LOGGER.isWarnEnabled()) {
				LOGGER.warn(MessageConstraints.AUTH_INVALID_TOKEN);
			}
			throw new IOException(MessageConstraints.AUTH_INVALID_TOKEN);
		}

		try {
			isValidDate(parse[2]);
		} catch (IOException e) {
			if (LOGGER.isWarnEnabled()) {
				LOGGER.warn(MessageConstraints.AUTH_INVALID_TOKEN, e);
			}
			throw new IOException(MessageConstraints.AUTH_INVALID_TOKEN);
		}

		String numeroSerie = parse[0];
		String secret = parse[1];
		String dateString = parse[2];

		if (!validarKeyBase(secret)) {
			if (LOGGER.isWarnEnabled()) {
				LOGGER.warn(MessageConstraints.AUTH_INVALID_TOKEN);
			}
			throw new IOException(MessageConstraints.AUTH_INVALID_TOKEN);
		}

		LocalDateTime tokenTime = LocalDateTime.parse(dateString, dtfPadrao);
		if (isTokenExpired(tokenTime, TIME)) {
			if (LOGGER.isWarnEnabled()) {
				LOGGER.warn(MessageConstraints.AUTH_TOKEN_EXPIRED);
			}
			throw new IOException(MessageConstraints.AUTH_TOKEN_EXPIRED);
		}

		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug(MessageConstraints.AUTH_VALIDATION_SUCCESS);
		}

		return new AuthDeviceRecords(numeroSerie, secret, dateString);
	}

	private static boolean isTokenExpired(LocalDateTime tokenTime, long expirationTimeInMinutes) {
		LocalDateTime now = LocalDateTime.now(ZoneId.systemDefault());
		long minutesElapsed = Duration.between(tokenTime, now).toMinutes();
		return minutesElapsed > expirationTimeInMinutes;
	}

	public static String addHoursToDateString(String dateString, long minutesToAdd) {
		LocalDateTime dateTime = LocalDateTime.parse(dateString, dtfPadrao);
		LocalDateTime newDateTime = dateTime.plusMinutes(minutesToAdd);
		return dtfPadrao.format(newDateTime);
	}

	public TokenValidationDeviceDTO validarTokenDevice(String token) throws IOException {
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug(MessageConstraints.LOG_AUTH_TOKEN_VALIDATE);
		}

		AuthDeviceRecords isValid;
		try {
			isValid = validarTokenBaseString(token);
		} catch (IOException e) {
			if (LOGGER.isWarnEnabled()) {
				LOGGER.warn(MessageConstraints.AUTH_VALIDATION_FAILED, e);
			}
			throw new IOException(MessageConstraints.AUTH_INVALID_TOKEN);
		}

		if (isValid == null) {
			if (LOGGER.isWarnEnabled()) {
				LOGGER.warn(MessageConstraints.AUTH_VALIDATION_FAILED);
			}
			throw new IOException(MessageConstraints.AUTH_INVALID_TOKEN);
		}

		String newDataToken = addHoursToDateString(isValid.date(), TIME);

		if (LOGGER.isInfoEnabled()) {
			LOGGER.info(MessageConstraints.AUTH_VALIDATION_SUCCESS);
		}

		return new TokenValidationDeviceDTO(validarTokenBase(token), isValid.date(), newDataToken);
	}
}
