package br.com.pvv.senai.model.dto;

import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;

import br.com.pvv.senai.entity.Paciente;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public class PacienteDto extends GenericDto<Paciente> {

	@Schema(description = "Identificador único da pessoa", example = "12345")
    private long id;
    @NotNull
    @Size(min = 8, max = 64)
    @Schema(description = "Nome completo da pessoa", example = "João da Silva")
    private String name;
    @NotNull
    @Schema(description = "Gênero da pessoa", example = "Masculino")
    private String gender;
    @NotNull
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE, pattern = "yyyy-MM-dd")
    @Schema(description = "Data de nascimento da pessoa", example = "1990-01-01")
    private Date birthDate;
    @NotNull
    @Pattern(regexp = "\\d{3}\\.\\d{3}\\.\\d{3}\\-\\d{2}")
    @Schema(description = "CPF da pessoa", example = "123.456.789-00")
    private String CPF;
    @NotNull
    @Size(max = 20)
    @Schema(description = "RG da pessoa", example = "MG-12.345.678")
    private String RG;
    @NotNull
    @Schema(description = "Estado civil da pessoa", example = "Solteiro")
    private String maritalStatus;
    @NotNull
    @Pattern(regexp = "\\(\\d{2}\\) \\d{1} \\d{4}-\\d{4}")
    @Schema(description = "Telefone da pessoa", example = "(31) 9 1234-5678")
    private String phone;
    @Email
    @Schema(description = "Email da pessoa", example = "joao.silva@example.com")
    private String email;
    @NotNull
    @Size(min = 8, max = 64)
    @Schema(description = "Cidade de nascimento da pessoa", example = "Belo Horizonte")
    private String birthCity;
    @NotNull
    @Pattern(regexp = "\\(\\d{2}\\) \\d{1} \\d{4}-\\d{4}")
    @Schema(description = "Contato de emergência da pessoa", example = "(31) 9 8765-4321")
    private String emergencyContact;
    @Schema(description = "Alergias da pessoa", example = "Nenhuma")
    private String allergies;
    @Schema(description = "Cuidados especiais da pessoa", example = "Nenhum")
    private String specialCare;
    @Schema(description = "Companhia de seguro da pessoa", example = "Seguro Saúde XYZ")
    private String insuranceCompany;
    @Schema(description = "Número do seguro da pessoa", example = "1234567890")
    private String insuranceNumber;
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE, pattern = "yyyy-MM-dd")
    @Schema(description = "Data de expiração do seguro da pessoa", example = "2025-12-31")
    private Date insuranceExpiration;

    @Schema(description = "Endereço da pessoa")
    private EnderecoDto address;

	@Override
	protected Class<Paciente> getType() {
		return Paciente.class;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getGender() {
		return gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}

	public Date getBirthDate() {
		return birthDate;
	}

	public void setBirthDate(Date birthDate) {
		this.birthDate = birthDate;
	}

	public String getCPF() {
		return CPF;
	}

	public void setCPF(String cPF) {
		CPF = cPF;
	}

	public String getRG() {
		return RG;
	}

	public void setRG(String rG) {
		RG = rG;
	}

	public String getMaritalStatus() {
		return maritalStatus;
	}

	public void setMaritalStatus(String maritalStatus) {
		this.maritalStatus = maritalStatus;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getBirthCity() {
		return birthCity;
	}

	public void setBirthCity(String birthCity) {
		this.birthCity = birthCity;
	}

	public String getEmergencyContact() {
		return emergencyContact;
	}

	public void setEmergencyContact(String emergencyContact) {
		this.emergencyContact = emergencyContact;
	}

	public String getAllergies() {
		return allergies;
	}

	public void setAllergies(String allergies) {
		this.allergies = allergies;
	}

	public String getSpecialCare() {
		return specialCare;
	}

	public void setSpecialCare(String specialCare) {
		this.specialCare = specialCare;
	}

	public String getInsuranceCompany() {
		return insuranceCompany;
	}

	public void setInsuranceCompany(String insuranceCompany) {
		this.insuranceCompany = insuranceCompany;
	}

	public String getInsuranceNumber() {
		return insuranceNumber;
	}

	public void setInsuranceNumber(String insuranceNumber) {
		this.insuranceNumber = insuranceNumber;
	}

	public Date getInsuranceExpiration() {
		return insuranceExpiration;
	}

	public void setInsuranceExpiration(Date insuranceExpiration) {
		this.insuranceExpiration = insuranceExpiration;
	}

	public EnderecoDto getAddress() {
		return address;
	}

	public void setAddress(EnderecoDto address) {
		this.address = address;
	}

}
