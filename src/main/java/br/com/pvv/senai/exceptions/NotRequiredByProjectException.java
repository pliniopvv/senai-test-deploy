package br.com.pvv.senai.exceptions;

public class NotRequiredByProjectException extends Exception {

	public NotRequiredByProjectException() {
		super("Método não implementado por não ser requisitado pelo projeto.");
	}

}
