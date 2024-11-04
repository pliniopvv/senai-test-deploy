package br.com.pvv.senai.entity;

import java.util.Date;
import java.util.List;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.format.annotation.NumberFormat;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;

@Entity
public class Paciente implements IEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;
	@Column(length = 64, nullable = false)
	private String name; // 8-64
	@Column(nullable = false)
	private String gender;
	@Column(nullable = false)
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private Date birthDate;
	@Column(length = 14) // 000.000.000-00
	@NumberFormat(pattern = "###.###.###-##")
	private String CPF;
	@Column(length = 20)
	private String RG;
	@Column(nullable = false)
	private String maritalStatus;
	@Column(nullable = false)
	@NumberFormat(pattern = "(##) # ####-####")
	private String phone;
	@Column()
	private String email; // regex \w+@\w+
	@Column(nullable = false, length = 64) // 8-64
	private String birthCity;
	@Column(nullable = false)
	@NumberFormat(pattern = "(##) # ####-####")
	private String emergencyContact;
	@Column()
	private String allergies;
	@Column()
	private String specialCare;
	@Column()
	private String insuranceCompany;
	@Column()
	private String insuranceNumber;
	@Column()
	private Date insuranceExpiration;
	@OneToOne()
	private Endereco address;

	@JsonIgnore
	@OneToOne(cascade = CascadeType.DETACH, orphanRemoval = true)
	@JoinColumn(name = "id_usuario", nullable = false)
	private Usuario usuario;

	@OneToMany
	private List<Consulta> consultas;
	@OneToMany
	private List<Exame> exames;

	public List<Consulta> getConsultas() {
		return consultas;
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

	public Endereco getAddress() {
		return address;
	}

	public void setAddress(Endereco address) {
		this.address = address;
	}

	public Usuario getUsuario() {
		return usuario;
	}

	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}

	public List<Exame> getExames() {
		return exames;
	}

	public void setExames(List<Exame> exames) {
		this.exames = exames;
	}

	public void setConsultas(List<Consulta> consultas) {
		this.consultas = consultas;
	}

}
