package com.ocoelhogabriel.manager_user_security.utils;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ocoelhogabriel.manager_user_security.domain.constraints.MessageConstraints;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter(autoApply = true)
public class JsonNodeConverter implements AttributeConverter<JsonNode, String> {

	private static final Logger LOGGER = LoggerFactory.getLogger(JsonNodeConverter.class);
	private static final ObjectMapper objectMapper = new ObjectMapper();

	@Override
	public String convertToDatabaseColumn(JsonNode attribute) {
		if (attribute == null) {
			return null;
		}
		
		try {
			if (LOGGER.isDebugEnabled()) {
				LOGGER.debug("Converting JsonNode to database column string");
			}
			return objectMapper.writeValueAsString(attribute);
		} catch (JsonProcessingException e) {
			if (LOGGER.isErrorEnabled()) {
				LOGGER.error(MessageConstraints.VALIDATION_INVALID_FORMAT, e);
			}
			throw new IllegalArgumentException(MessageConstraints.VALIDATION_INVALID_FORMAT, e);
		}
	}

	@Override
	public JsonNode convertToEntityAttribute(String dbData) {
		if (dbData == null || dbData.isEmpty()) {
			return null;
		}
		
		try {
			if (LOGGER.isDebugEnabled()) {
				LOGGER.debug("Converting database column string to JsonNode");
			}
			return objectMapper.readTree(dbData);
		} catch (IOException e) {
			if (LOGGER.isErrorEnabled()) {
				LOGGER.error(MessageConstraints.VALIDATION_INVALID_FORMAT, e);
			}
			throw new IllegalArgumentException(MessageConstraints.VALIDATION_INVALID_FORMAT, e);
		}
	}
}
