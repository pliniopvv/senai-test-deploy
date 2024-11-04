package br.com.pvv.senai.utils;

public class SenhaUtils {

    public static String gerarSenhaMascarada(String senha) {
        if (senha.length()>4){
            String senhaParteVisivel = senha.substring(0,4);
            String senhaParteOculta = "*".repeat(senha.length()-4);
            return senhaParteVisivel+ senhaParteOculta;
        } else {
            return "*".repeat(senha.length());
        }
    }

}
