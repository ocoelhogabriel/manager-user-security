package com.ocoelhogabriel.manager_user_security.utils.message;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import com.ocoelhogabriel.manager_user_security.domain.constraints.MessageConstraints;
import com.ocoelhogabriel.manager_user_security.exception.ResponseGlobalModel;
import com.ocoelhogabriel.manager_user_security.utils.Utils;

/**
 * Classe utilitária para padronizar as respostas HTTP da API
 * Implementa padrões consistentes para diferentes tipos de resposta
 */
@Component
public class MessageResponse {
	private static final Logger LOGGER = LoggerFactory.getLogger(MessageResponse.class);
	
	private MessageResponse() {
		throw new IllegalStateException(MessageConstraints.ERROR_GENERIC);
	}

	/**
	 * Retorna uma resposta paginada com status 200 OK
	 * @param <T> O tipo de objeto na página
	 * @param page A página a ser retornada
	 * @return ResponseEntity com status 200 e o conteúdo da página
	 */
	public static <T> ResponseEntity<Page<T>> page(Page<T> page) {
		return new ResponseEntity<>(page, HttpStatus.OK);
	}

	/**
	 * Retorna uma resposta de sucesso com status 200 OK
	 * @param <T> O tipo de objeto na resposta
	 * @param object O objeto a ser retornado
	 * @return ResponseEntity com status 200 e o objeto
	 */
	public static <T> ResponseEntity<T> success(T object) {
		return new ResponseEntity<>(object, HttpStatus.OK);
	}

	/**
	 * Retorna uma resposta de criação com status 201 CREATED
	 * @param <T> O tipo de objeto na resposta
	 * @param object O objeto criado
	 * @return ResponseEntity com status 201 e o objeto criado
	 */
	public static <T> ResponseEntity<T> create(T object) {
		return new ResponseEntity<>(object, HttpStatus.CREATED);
	}
	
	/**
	 * Retorna uma resposta sem conteúdo com status 204 NO_CONTENT
	 * @return ResponseEntity com status 204
	 */
	public static ResponseEntity<Void> noContent() {
		return new ResponseEntity<>(HttpStatus.NO_CONTENT);
	}

	/**
	 * Retorna uma resposta de erro de requisição com status 400 BAD_REQUEST
	 * @param <T> O tipo de objeto na resposta
	 * @param object O objeto de erro a ser retornado
	 * @return ResponseEntity com status 400 e o objeto de erro
	 */
	public static <T> ResponseEntity<T> badRequest(T object) {
		return new ResponseEntity<>(object, HttpStatus.BAD_REQUEST);
	}

	/**
	 * Retorna uma resposta de erro de autorização com status 401 UNAUTHORIZED
	 * @param <T> O tipo de objeto na resposta
	 * @param object O objeto de erro a ser retornado
	 * @return ResponseEntity com status 401 e o objeto de erro
	 */
	public static <T> ResponseEntity<T> notAuthorize(T object) {
		return new ResponseEntity<>(object, HttpStatus.UNAUTHORIZED);
	}

	/**
	 * Retorna uma resposta de recurso não encontrado com status 404 NOT_FOUND
	 * @param <T> O tipo de objeto na resposta
	 * @param object O objeto de erro a ser retornado
	 * @return ResponseEntity com status 404 e o objeto de erro
	 */
	public static <T> ResponseEntity<T> notFound(T object) {
		return new ResponseEntity<>(object, HttpStatus.NOT_FOUND);
	}
	
	/**
	 * Retorna uma resposta de erro interno com status 500 INTERNAL_SERVER_ERROR
	 * @param <T> O tipo de objeto na resposta
	 * @param object O objeto de erro a ser retornado
	 * @return ResponseEntity com status 500 e o objeto de erro
	 */
	public static <T> ResponseEntity<T> internalError(T object) {
		return new ResponseEntity<>(object, HttpStatus.INTERNAL_SERVER_ERROR);
	}

	/**
	 * Cria um modelo de resposta global para operações bem-sucedidas
	 * @param message A mensagem de sucesso
	 * @return Um modelo de resposta global com indicador de sucesso
	 */
	public static ResponseGlobalModel responseGlobalModelSucess(String message) {
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("Gerando resposta de sucesso: {}", message);
		}
		
		// Registrar mensagem de sucesso para deduplicação se necessário
		UniqueMessageUtil.isUniqueMessage(message, "success_message");
		return new ResponseGlobalModel(true, message, Utils.newDateString());
	}

	/**
	 * Cria um modelo de resposta global para operações com erro
	 * @param message A mensagem de erro
	 * @return Um modelo de resposta global com indicador de erro
	 */
	public static ResponseGlobalModel responseGlobalModelError(String message) {
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("Gerando resposta de erro: {}", message);
		}
		
		// Registrar mensagem de erro para deduplicação se necessário
		UniqueMessageUtil.isUniqueMessage(message, "error_message");
		return new ResponseGlobalModel(false, message, Utils.newDateString());
	}

}
