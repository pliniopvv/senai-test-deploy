package br.com.pvv.senai.exceptions;

public class UsuarioNotFoundException extends RuntimeException {

	public UsuarioNotFoundException() {
		super("Pessoa usuária não localizada.");
	}
	
}
