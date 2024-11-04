package br.com.pvv.senai.model.dto;

import java.time.LocalDate;
import java.time.LocalTime;

import org.springframework.format.annotation.DateTimeFormat;

import br.com.pvv.senai.entity.Exame;
import br.com.pvv.senai.model.dto.annotations.SkipMakeEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class ExameDto extends GenericDto<Exame> {

	private long id;

	@Schema(description = "Nome do paciente", example = "João da Silva", minLength = 8, maxLength = 64)
	@Size(min = 8, max = 64)
	private String nome;
	@Schema(description = "Data do exame", example = "2024-10-31")
	@NotNull
	@DateTimeFormat(iso = DateTimeFormat.ISO.DATE, pattern = "yyyy-MM-dd")
	private LocalDate dataExame;
	@Schema(description = "Hora do exame", example = "14:30:00")
	@NotNull
	@DateTimeFormat(iso = DateTimeFormat.ISO.DATE, pattern = "hh:mm:ss")
	private LocalTime horaExame;
	@Schema(description = "Tipo de exame", example = "Sangue", minLength = 4, maxLength = 32)
	@NotNull
	@Size(min = 4, max = 32)
	private String tipo;
	@Schema(description = "Laboratório responsável pelo exame", example = "Laboratório XYZ", minLength = 4, maxLength = 32)
	@NotNull
	@Size(min = 4, max = 32)
	private String laboratorio;
	@Schema(description = "URL com mais informações sobre o exame", example = "http://www.exemplo.com/exame")
	private String URL;
	@Schema(description = "Resultados do exame", example = "Resultados detalhados do exame...", minLength = 16, maxLength = 1024)
	@Size(min = 16, max = 1024)
	private String resultados;
	
	private PacienteDto paciente;

	@NotNull
	@SkipMakeEntity
	private int patientId;

	public final int getPatientId() {
		return patientId;
	}

	public final void setPatientId(int patientId) {
		this.patientId = patientId;
	}

	@Override
	protected Class<Exame> getType() {
		return Exame.class;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public LocalDate getDataExame() {
		return dataExame;
	}

	public void setDataExame(LocalDate dataExame) {
		this.dataExame = dataExame;
	}

	public LocalTime getHoraExame() {
		return horaExame;
	}

	public void setHoraExame(LocalTime horaExame) {
		this.horaExame = horaExame;
	}

	public String getTipo() {
		return tipo;
	}

	public void setTipo(String tipo) {
		this.tipo = tipo;
	}

	public String getLaboratorio() {
		return laboratorio;
	}

	public void setLaboratorio(String laboratorio) {
		this.laboratorio = laboratorio;
	}

	public String getURL() {
		return URL != null ? URL : "";
	}

	public void setURL(String uRL) {
		URL = uRL;
	}

	public String getResultados() {
		return resultados;
	}

	public void setResultados(String resultados) {
		this.resultados = resultados;
	}

	public PacienteDto getPaciente() {
		return paciente;
	}

	public void setPaciente(PacienteDto paciente) {
		this.paciente = paciente;
	}

}
