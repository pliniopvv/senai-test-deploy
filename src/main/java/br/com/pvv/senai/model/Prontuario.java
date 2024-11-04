package br.com.pvv.senai.model;

public class Prontuario {

	private long numeroRegistro;
	private String nome;
	private String convenio;

	public Prontuario(long numeroRegistro, String nome, String convenio) {
		super();
		this.numeroRegistro = numeroRegistro;
		this.nome = nome;
		this.convenio = convenio;
	}

	public long getNumeroRegistro() {
		return numeroRegistro;
	}

	public void setNumeroRegistro(long numeroRegistro) {
		this.numeroRegistro = numeroRegistro;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public String getConvenio() {
		return convenio;
	}

	public void setConvenio(String convenio) {
		this.convenio = convenio;
	}

}
