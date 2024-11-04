package br.com.pvv.senai.exceptions;

public class UnauthorizationException extends Exception {
	
	public UnauthorizationException() {
		super("Login não autorizado.");
	}
}
