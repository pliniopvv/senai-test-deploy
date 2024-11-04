package br.com.pvv.senai.exceptions;

public class PacienteNotFoundException extends Exception {

	public PacienteNotFoundException(int id) {
		super("Paciente não localizado com id" + id);
	}
}
