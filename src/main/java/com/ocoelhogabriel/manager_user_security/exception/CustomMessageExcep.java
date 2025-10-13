package com.ocoelhogabriel.manager_user_security.exception;

import java.io.IOException;

import org.springframework.stereotype.Component;

import com.google.gson.Gson;
import com.ocoelhogabriel.manager_user_security.domain.constraints.MessageConstraints;
import com.ocoelhogabriel.manager_user_security.domain.constraints.MessageFormatterUtil;

import jakarta.persistence.EntityNotFoundException;

/**
 * Classe utilitária para geração de exceções personalizadas
 * Agora utilizando o padrão MessageConstraints para mensagens
 */
@Component
public class CustomMessageExcep {
    
    // Sem mais dependência estática para MessageUtil
    
    public CustomMessageExcep() {
        // Construtor vazio, não precisa mais de MessageUtil
    }

	public static IOException exceptionCodigoIOException(String acao, String local, Object codigo, Object object, Throwable throwable) throws IOException {
	    String message = MessageFormatterUtil.format(
            MessageConstraints.LOG_OPERATION_ERROR, 
            acao + " " + local, 
            "Código: " + codigo + ", objeto: " + new Gson().toJson(object)
        );
	    
		throw new IOException(message, throwable);
	}

	public static IOException exceptionIOException(String acao, String local, Object object, Throwable throwable) throws IOException {
	    String message = MessageFormatterUtil.format(
            MessageConstraints.LOG_OPERATION_ERROR, 
            acao + " " + local, 
            "objeto: " + new Gson().toJson(object)
        );
	        
		throw new IOException(message, throwable);
	}

	public static EntityNotFoundException exceptionEntityNotFoundException(Object codigo, String local, Throwable throwable) {
	    String entityType = determineEntityType(local);
	    String message = MessageFormatterUtil.formatTemplate(entityType, codigo.toString());
	        
		throw new EntityNotFoundException(message);
	}
	
	private static String determineEntityType(String local) {
	    if (local == null) {
	        return "entity.not.found.id";
	    }
	    
	    local = local.toLowerCase();
	    if (local.contains("planta")) {
	        return "plant.not.found.id";
	    } else if (local.contains("usuário") || local.contains("usuario")) {
	        return "user.not.found.id";
	    } else if (local.contains("empresa")) {
	        return "enterprise.not.found.id";
	    } else if (local.contains("recurso")) {
	        return "resource.not.found.id";
	    } else if (local.contains("perfil")) {
	        return "profile.not.found.id";
	    }
	    
	    return "entity.not.found.id";
	}

}
