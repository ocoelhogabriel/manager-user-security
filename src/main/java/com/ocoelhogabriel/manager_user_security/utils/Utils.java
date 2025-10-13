package com.ocoelhogabriel.manager_user_security.utils;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.Base64;
import java.util.Date;
import java.util.Objects;
import java.util.TimeZone;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.lang.NonNull;

import com.ocoelhogabriel.manager_user_security.domain.constraints.MessageConstraints;
import com.ocoelhogabriel.manager_user_security.exception.ResponseGlobalModel;

public class Utils {

	private static final Logger LOGGER = LoggerFactory.getLogger(Utils.class);

	private Utils() {
		throw new IllegalStateException("Utility class");
	}

	// Date and time formats
	private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ISO_LOCAL_DATE;
	private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ISO_LOCAL_DATE_TIME;
	private static final DateTimeFormatter dtfEditado = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS");
	private static final DateTimeFormatter dtfPadrao = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
	private static final String DATE_FORMAT_PATTERN = "yyyy-MM-dd'T'HH:mm:ss";
	
	private static final ThreadLocal<SimpleDateFormat> sdfThreadLocal = ThreadLocal.withInitial(() -> 
		new SimpleDateFormat(DATE_FORMAT_PATTERN));
	private static final SimpleDateFormat sdfbase = new SimpleDateFormat(DATE_FORMAT_PATTERN);


	public static LocalDate convertStringToDateForDateTimeFormatter(String date) {
		return LocalDate.parse(date, DATE_FORMATTER);
	}

	public static LocalDateTime convertStringToDateTimeForDateTimeFormatter(String date) {
		return LocalDateTime.parse(date, DATE_TIME_FORMATTER);
	}

	public static String convertDateToString(Date date) {
		LocalDateTime localDateTime = LocalDateTime.ofInstant(date.toInstant(), ZoneId.systemDefault());
		return dtfPadrao.format(localDateTime);
	}

	public static Date addGmtToDateTime(Integer gmt) {
		ZoneOffset offsetGMT = ZoneOffset.ofHours(gmt / 60);
		OffsetDateTime offsetDateTime = OffsetDateTime.parse(getDataHoraGMT0(), DateTimeFormatter.ISO_OFFSET_DATE_TIME);
		OffsetDateTime offsetDateTimeGMT = offsetDateTime.withOffsetSameInstant(offsetGMT);
		return Date.from(offsetDateTimeGMT.toInstant());
	}

	public static String addGmtToDateTimeSendString(Date date) {
		Objects.requireNonNull(date, "A Data de entrada para conversão de Date para String está nula.");
		LocalDateTime localDateTime = LocalDateTime.ofInstant(date.toInstant(), ZoneId.systemDefault());
		return dtfEditado.format(localDateTime);
	}

	public static String removeGmtToDateTime(String horarioComGMT, Integer gmtEmMinutos) {
		LocalDateTime dataAtual = LocalDateTime.parse(horarioComGMT, DateTimeFormatter.ISO_LOCAL_DATE_TIME);
		LocalDateTime dataSemOffset = dataAtual.minusMinutes(gmtEmMinutos);
		return dataSemOffset.format(dtfEditado);
	}

	public static String getDataHoraGMT0() {
		Instant agoraGMT = Instant.now();
		OffsetDateTime offsetDateTimeGMT0 = agoraGMT.atOffset(ZoneOffset.UTC);
		return offsetDateTimeGMT0.format(dtfPadrao);
	}

	public static Date convertStringToDate(String dateString) {
		if (dateString == null) {
			if (LOGGER.isWarnEnabled()) {
				LOGGER.warn(MessageConstraints.VALIDATION_REQUIRED_FIELD, "dateString");
			}
			throw new NullPointerException(MessageConstraints.VALIDATION_REQUIRED_FIELD);
		}
		
		try {
			LocalDateTime localDateTime = LocalDateTime.parse(dateString, dtfEditado);
			return Date.from(localDateTime.atZone(ZoneId.systemDefault()).toInstant());
		} catch (Exception e) {
			if (LOGGER.isErrorEnabled()) {
				LOGGER.error(MessageConstraints.VALIDATION_INVALID_FORMAT, e);
			}
			throw new IllegalArgumentException(MessageConstraints.VALIDATION_INVALID_FORMAT, e);
		}
	}

	public static String dateToString(Date date) {
		if (date == null) {
			if (LOGGER.isWarnEnabled()) {
				LOGGER.warn(MessageConstraints.VALIDATION_REQUIRED_FIELD, "date");
			}
			throw new NullPointerException(MessageConstraints.VALIDATION_REQUIRED_FIELD);
		}
		
		try {
			LocalDateTime localDateTime = LocalDateTime.ofInstant(date.toInstant(), ZoneId.systemDefault());
			return dtfEditado.format(localDateTime);
		} catch (Exception e) {
			if (LOGGER.isErrorEnabled()) {
				LOGGER.error(MessageConstraints.VALIDATION_INVALID_FORMAT, e);
			}
			throw new IllegalArgumentException(MessageConstraints.VALIDATION_INVALID_FORMAT, e);
		}
	}

	public static String newDateString() {
		return dateToString(new Date());
	}

	public static String bytesParaBase64(byte[] bytes) {
		return Base64.getEncoder().encodeToString(bytes);
	}

	public static byte[] base64ParaBytes(String base64String) {
		return Base64.getDecoder().decode(base64String);
	}

	public static String encode(String input) {
		if (input == null) {
			if (LOGGER.isWarnEnabled()) {
				LOGGER.warn(MessageConstraints.VALIDATION_REQUIRED_FIELD, "input");
			}
			throw new NullPointerException(MessageConstraints.VALIDATION_REQUIRED_FIELD);
		}
		
		try {
			if (LOGGER.isDebugEnabled()) {
				LOGGER.debug("Encoding string to Base64");
			}
			return Base64.getEncoder().encodeToString(input.getBytes());
		} catch (Exception e) {
			if (LOGGER.isErrorEnabled()) {
				LOGGER.error("Error encoding to Base64: {}", e.getMessage(), e);
			}
			throw new IllegalArgumentException(MessageConstraints.VALIDATION_INVALID_FORMAT, e);
		}
	}

	public static String decode(String base64Input) {
		if (base64Input == null) {
			if (LOGGER.isWarnEnabled()) {
				LOGGER.warn(MessageConstraints.VALIDATION_REQUIRED_FIELD, "base64Input");
			}
			throw new NullPointerException(MessageConstraints.VALIDATION_REQUIRED_FIELD);
		}
		
		try {
			if (LOGGER.isDebugEnabled()) {
				LOGGER.debug("Decoding string from Base64");
			}
			byte[] decodedBytes = Base64.getDecoder().decode(base64Input);
			return new String(decodedBytes);
		} catch (IllegalArgumentException e) {
			if (LOGGER.isErrorEnabled()) {
				LOGGER.error("Error decoding from Base64: {}", e.getMessage(), e);
			}
			throw new IllegalArgumentException(MessageConstraints.VALIDATION_INVALID_FORMAT, e);
		}
	}

	/**
	 * Cria um objeto Pageable para consultas paginadas com ordenação
	 * @param ordenarEntity Campo para ordenação
	 * @param direcao Direção de ordenação ('ASC' ou 'DESC')
	 * @param pagina Número da página (0-based)
	 * @param tamanho Tamanho da página
	 * @return Objeto Pageable configurado
	 */
	public static Pageable consultaPage(String ordenarEntity, @NonNull String direcao, Integer pagina, Integer tamanho) {
		if (ordenarEntity == null) {
			if (LOGGER.isWarnEnabled()) {
				LOGGER.warn(MessageConstraints.VALIDATION_REQUIRED_FIELD, "ordenarEntity");
			}
			throw new NullPointerException(MessageConstraints.VALIDATION_REQUIRED_FIELD);
		}
		
		if (pagina == null || tamanho == null) {
			if (LOGGER.isWarnEnabled()) {
				LOGGER.warn(MessageConstraints.VALIDATION_REQUIRED_FIELD, "pagina/tamanho");
			}
			throw new NullPointerException(MessageConstraints.VALIDATION_REQUIRED_FIELD);
		}
		
		try {
			if (LOGGER.isDebugEnabled()) {
				LOGGER.debug("Criando consulta paginada: campo={}, direção={}, página={}, tamanho={}", 
					ordenarEntity, direcao, pagina, tamanho);
			}
			Sort.Direction sortDirection = Sort.Direction.fromString(direcao);
			Sort sort = Sort.by(sortDirection, ordenarEntity);
			return PageRequest.of(pagina, tamanho, sort);
		} catch (IllegalArgumentException e) {
			if (LOGGER.isErrorEnabled()) {
				LOGGER.error("Erro ao criar consulta paginada: {}", e.getMessage(), e);
			}
			throw new IllegalArgumentException(MessageConstraints.VALIDATION_INVALID_FORMAT, e);
		}
	}

	/**
	 * Creates a response model for error messages
	 * @param message The error message
	 * @return ResponseGlobalModel with error flag set to true
	 * @deprecated Use {@link com.ocoelhogabriel.manager_user_security.utils.message.MessageResponse#responseGlobalModelError(String)} instead
	 */
	@Deprecated(since = "1.0.0", forRemoval = true)
	public static ResponseGlobalModel responseMessageError(String message) {
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("Creating error response model with message: {}", message);
		}
		return new ResponseGlobalModel(true, message, Utils.convertDateToString(new Date()));
	}

	/**
	 * Creates a response model for success messages
	 * @param message The success message
	 * @return ResponseGlobalModel with error flag set to false
	 * @deprecated Use {@link com.ocoelhogabriel.manager_user_security.utils.message.MessageResponse#responseGlobalModelSucess(String)} instead
	 */
	@Deprecated(since = "1.0.0", forRemoval = true)
	public static ResponseGlobalModel responseMessageSucess(String message) {
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("Creating success response model with message: {}", message);
		}
		return new ResponseGlobalModel(false, message, Utils.convertDateToString(new Date()));
	}

	public static String sdfBaseDateforString() {
		var date = new Date();
		return sdfbase.format(date);
	}

	public static String sdfDateforString(Date date) {
		if (date == null) {
			if (LOGGER.isWarnEnabled()) {
				LOGGER.warn(MessageConstraints.VALIDATION_REQUIRED_FIELD, "date");
			}
			return null;
		}
		
		SimpleDateFormat formatter = new SimpleDateFormat(DATE_FORMAT_PATTERN);
		return formatter.format(date);
	}

	public static Date sdfStringforDate(String date) throws ParseException {
		if (date == null || date.isEmpty()) {
			if (LOGGER.isWarnEnabled()) {
				LOGGER.warn(MessageConstraints.VALIDATION_REQUIRED_FIELD, "date");
			}
			throw new ParseException(MessageConstraints.VALIDATION_REQUIRED_FIELD, 0);
		}
		
		try {
			SimpleDateFormat formatter = new SimpleDateFormat(DATE_FORMAT_PATTERN);
			return formatter.parse(date);
		} catch (ParseException e) {
			if (LOGGER.isErrorEnabled()) {
				LOGGER.error(MessageConstraints.DATE_PARSING_ERROR, e.getMessage(), e);
			}
			throw new ParseException(MessageConstraints.DATE_PARSING_ERROR + e.getMessage(), 0);
		}
	}

	public static Date sdfDateTimeZone(String dateString) throws ParseException {
		if (dateString == null || dateString.isEmpty()) {
			if (LOGGER.isWarnEnabled()) {
				LOGGER.warn(MessageConstraints.VALIDATION_REQUIRED_FIELD, "dateString");
			}
			throw new ParseException(MessageConstraints.VALIDATION_REQUIRED_FIELD, 0);
		}
		
		try {
			SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ");
			formatter.setTimeZone(TimeZone.getTimeZone("UTC"));
			return formatter.parse(dateString);
		} catch (ParseException e) {
			if (LOGGER.isErrorEnabled()) {
				LOGGER.error(MessageConstraints.DATE_PARSING_ERROR + "{}", e.getMessage(), e);
			}
			throw new ParseException(MessageConstraints.DATE_PARSING_ERROR + e.getMessage(), 0);
		}
	}

	public static String convertTimestampToDateStr(int timestamp) {
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern(DATE_FORMAT_PATTERN);
		LocalDateTime dateTime = LocalDateTime.ofInstant(Instant.ofEpochSecond(timestamp), ZoneId.systemDefault());
		return dateTime.format(formatter);
	}

	public static Date convertTimestampToDate(int timestamp) throws ParseException {
		String timeStampStr = convertTimestampToDateStr(timestamp);
		return sdfStringforDate(timeStampStr);
	}

	/**
	 * Converte milímetros para metros
	 * @param mm Valor em milímetros
	 * @return Valor em metros
	 */
	public static double converterMmParaM(double mm) {
		if (LOGGER.isTraceEnabled()) {
			LOGGER.trace("Convertendo {} mm para metros", mm);
		}
		return mm / 1000.0;
	}

	/**
	 * Converte milímetros para metros (Long)
	 * @param mm Valor em milímetros
	 * @return Valor em metros
	 */
	public static Long converterMmParaMLong(Long mm) {
		if (mm == null) {
			if (LOGGER.isWarnEnabled()) {
				LOGGER.warn(MessageConstraints.VALIDATION_REQUIRED_FIELD, "mm");
			}
			throw new NullPointerException(MessageConstraints.VALIDATION_REQUIRED_FIELD);
		}
		if (LOGGER.isTraceEnabled()) {
			LOGGER.trace("Convertendo {} mm para metros (Long)", mm);
		}
		return mm / 1000;
	}

	/**
	 * Converte metros para milímetros
	 * @param m Valor em metros
	 * @return Valor em milímetros
	 */
	public static double converterMParaMm(double m) {
		if (LOGGER.isTraceEnabled()) {
			LOGGER.trace("Convertendo {} m para milímetros", m);
		}
		return m * 1000.0;
	}

	/**
	 * Converte milímetros cúbicos para metros cúbicos
	 * @param mm3 Valor em milímetros cúbicos
	 * @return Valor em metros cúbicos
	 */
	public static double converterMm3ParaM3(double mm3) {
		if (LOGGER.isTraceEnabled()) {
			LOGGER.trace("Convertendo {} mm³ para m³", mm3);
		}
		return mm3 / 1_000_000_000.0;
	}

	/**
	 * Calcula o volume de um silo vertical
	 * @param raioMilimetros Raio em milímetros
	 * @param alturaMilimetros Altura em milímetros
	 * @return Volume em milímetros cúbicos
	 */
	public static double calcularVolumeVertical(double raioMilimetros, double alturaMilimetros) {
		if (raioMilimetros < 0 || alturaMilimetros < 0) {
			if (LOGGER.isWarnEnabled()) {
				LOGGER.warn("Dimensões inválidas para cálculo de volume: raio={}, altura={}", raioMilimetros, alturaMilimetros);
			}
			throw new IllegalArgumentException(MessageConstraints.VALIDATION_INVALID_FORMAT);
		}
		
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("Calculando volume de silo vertical: raio={} mm, altura={} mm", raioMilimetros, alturaMilimetros);
		}
		return Math.PI * Math.pow(raioMilimetros, 2) * alturaMilimetros;
	}

	/**
	 * Calcula o volume de um silo horizontal
	 * @param comprimento Comprimento em milímetros
	 * @param largura Largura em milímetros
	 * @param altura Altura em milímetros
	 * @return Volume em milímetros cúbicos
	 */
	public static double calcularVolumeHorizontal(double comprimento, double largura, double altura) {
		if (comprimento < 0 || largura < 0 || altura < 0) {
			if (LOGGER.isWarnEnabled()) {
				LOGGER.warn("Dimensões inválidas para cálculo de volume: comprimento={}, largura={}, altura={}", 
					comprimento, largura, altura);
			}
			throw new IllegalArgumentException(MessageConstraints.VALIDATION_INVALID_FORMAT);
		}
		
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("Calculando volume horizontal: comprimento={} mm, largura={} mm, altura={} mm", 
				comprimento, largura, altura);
		}
		return comprimento * largura * altura;
	}

	/**
	 * Formata o volume em metros cúbicos com duas casas decimais
	 * @param volume Volume a ser formatado
	 * @return Volume formatado como string com 2 casas decimais
	 */
	public static String formatarVolume(double volume) {
		if (LOGGER.isTraceEnabled()) {
			LOGGER.trace("Formatando volume: {}", volume);
		}
		DecimalFormat decimalFormat = new DecimalFormat("0.00");
		return decimalFormat.format(volume);
	}

}
