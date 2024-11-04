package br.com.pvv.senai.exceptions;

public class ConsultaNotFoundException extends RuntimeException{
    public ConsultaNotFoundException() {
        super("Consulta não encontrada.");
    }
}
