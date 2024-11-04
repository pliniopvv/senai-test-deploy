package br.com.pvv.senai.exceptions;

public class EmailExistenteException extends Exception {

	public EmailExistenteException() {
		super("E-mail já cadastrado.");
	}
	
}
