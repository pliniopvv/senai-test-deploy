package br.com.pvv.senai.exceptions;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DtoToEntityException extends RuntimeException {

	Logger log = LoggerFactory.getLogger(DtoToEntityException.class);

	public DtoToEntityException(String message) {
		super(message);
		log.error("Erro de conversão : " + message);
	}

}
