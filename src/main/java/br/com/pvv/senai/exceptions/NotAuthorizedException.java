package br.com.pvv.senai.exceptions;

public class NotAuthorizedException extends RuntimeException {
	public NotAuthorizedException() {
		super("Não autorizado por requisições de projeto.");
	}
}
