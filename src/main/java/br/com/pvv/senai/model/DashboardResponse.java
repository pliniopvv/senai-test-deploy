package br.com.pvv.senai.model;

public class DashboardResponse {

	private Long qtdPacientes;
	private Long qtdConsultas;
	private Long qtdExames;
	public Long qtdUsuarios;

	public Long getQtdPacientes() {
		return qtdPacientes;
	}

	public void setQtdPacientes(Long qtdPacientes) {
		this.qtdPacientes = qtdPacientes;
	}

	public Long getQtdConsultas() {
		return qtdConsultas;
	}

	public void setQtdConsultas(Long qtdConsultas) {
		this.qtdConsultas = qtdConsultas;
	}

	public Long getQtdExames() {
		return qtdExames;
	}

	public void setQtdExames(Long qtdExames) {
		this.qtdExames = qtdExames;
	}

	public Long getQtdUsuarios() { return qtdUsuarios; }

	public void setQtdUsuarios(Long qtdUsuarios) { this.qtdUsuarios = qtdUsuarios; }
}
