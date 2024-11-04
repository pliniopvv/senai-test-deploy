package br.com.pvv.senai.exceptions;

public class PacienteUserNotFoundException extends Exception {

	public PacienteUserNotFoundException(String email) {
		super("Pessoa usuária não localizada para paciente : " + email);
	}
}
