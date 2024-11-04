package br.com.pvv.senai.model.dto;

import java.time.LocalDate;
import java.time.LocalTime;

import org.springframework.format.annotation.DateTimeFormat;

import br.com.pvv.senai.entity.Consulta;
import br.com.pvv.senai.model.dto.annotations.SkipMakeEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class ConsultaDto extends GenericDto<Consulta> {

	@Schema(description = "Identificador único", example = "12345")
	private long Id;

	@NotNull
	@Size(min = 8, max = 64)
	@Schema(description = "Motivo da consulta", example = "Consulta de rotina")
	private String reason;
	@NotNull
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	@Schema(description = "Data da consulta", example = "2024-10-31")
	private LocalDate date;
	@NotNull
	@DateTimeFormat(pattern = "hh:mm:ss")
	@Schema(description = "Hora da consulta", example = "14:30:00")
	private LocalTime time;
	@NotNull
	@Size(min = 16, max = 1024)
	@Schema(description = "Descrição do problema", example = "Paciente relata dores intensas na região lombar.")
	private String issueDescription;
	@Schema(description = "Medicação prescrita", example = "Ibuprofeno 400mg")
	private String prescribedMedication;
	@Size(min = 16, max = 256)
	@Schema(description = "Observações adicionais", example = "Paciente deve retornar em 2 semanas para acompanhamento.")
	private String observation;
	@SkipMakeEntity
	@Schema(description = "Dados do paciente")
	private PacienteDto patient;

	@NotNull
	@SkipMakeEntity
	@Schema(description = "Identificador do paciente", example = "67890")
	private int patientId;

	@Override
	protected Class<Consulta> getType() {
		return Consulta.class;
	}

	public long getId() {
		return Id;
	}

	public void setId(long id) {
		Id = id;
	}

	public String getReason() {
		return reason;
	}

	public void setReason(String reason) {
		this.reason = reason;
	}

	public LocalDate getDate() {
		return date;
	}

	public void setDate(LocalDate date) {
		this.date = date;
	}

	public LocalTime getTime() {
		return time;
	}

	public void setTime(LocalTime time) {
		this.time = time;
	}

	public String getIssueDescription() {
		return issueDescription;
	}

	public void setIssueDescription(String issueDescription) {
		this.issueDescription = issueDescription;
	}

	public String getPrescribedMedication() {
		return prescribedMedication;
	}

	public void setPrescribedMedication(String prescribedMedication) {
		this.prescribedMedication = prescribedMedication;
	}

	public String getObservation() {
		return observation;
	}

	public void setObservation(String observation) {
		this.observation = observation;
	}

	public PacienteDto getPatient() {
		return patient;
	}

	public void setPatient(PacienteDto patient) {
		this.patient = patient;
	}

	public int getPatientId() {
		return patientId;
	}

	public void setPatientId(int patientId) {
		this.patientId = patientId;
	}

}
