package br.com.pvv.senai.exceptions;

public class ExameNotFoundException extends RuntimeException{
    public ExameNotFoundException() {
        super("Exame não encontrado.");
    }
}
