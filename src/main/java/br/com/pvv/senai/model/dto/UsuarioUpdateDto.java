package br.com.pvv.senai.model.dto;

import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;

import br.com.pvv.senai.entity.Usuario;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public class UsuarioUpdateDto extends GenericDto<Usuario> {

	@Schema(description = "Identificador único", example = "12345", required = true, accessMode = Schema.AccessMode.READ_ONLY)
	private long Id;
	@NotNull
	@Size(max = 255)
	@Schema(description = "Nome completo", example = "João da Silva", required = true, minLength = 1, maxLength = 255)
	private String nome;
	@NotNull
	@Pattern(regexp = "\\(\\d{2}+\\) \\d{1}+ \\d{4}+-\\d{4}+")
	@Schema(description = "Número de telefone", example = "(12) 3 4567-8901", required = true, pattern = "\\(\\d{2}+\\) \\d{1}+ \\d{4}+-\\d{4}+")
	private String telefone;
	@NotNull
	@Email
	@Size(max = 255)
	@Schema(description = "Endereço de e-mail", example = "joao.silva@example.com", required = true, format = "email", maxLength = 255)
	private String email;
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	@Schema(description = "Data de nascimento", example = "1990-01-01", format = "date", nullable = true)
	private Date dataNascimento;
	@NotNull
	@Size(max = 14)
	@Schema(description = "CPF", example = "123.456.789-00", required = true, maxLength = 14)
	private String cpf;

	public String getTelefone() {
		return telefone;
	}

	public void setTelefone(String telefone) {
		this.telefone = telefone;
	}

	public long getId() {
		return Id;
	}

	public void setId(long id) {
		Id = id;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public Date getDataNascimento() {
		return dataNascimento;
	}

	public void setDataNascimento(Date dataNascimento) {
		this.dataNascimento = dataNascimento;
	}

	public String getCpf() {
		return cpf;
	}

	public void setCpf(String cpf) {
		this.cpf = cpf;
	}

	@Override
	protected Class<Usuario> getType() {
		return Usuario.class;
	}

}
