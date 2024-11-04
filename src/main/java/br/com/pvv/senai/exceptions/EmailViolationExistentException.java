package br.com.pvv.senai.exceptions;

public class EmailViolationExistentException extends RuntimeException {

	public EmailViolationExistentException() {
		super("E-mail já existente");
		
	}
	
}
