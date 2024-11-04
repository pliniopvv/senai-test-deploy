package br.com.pvv.senai.model.dto;

import java.util.Date;

import br.com.pvv.senai.entity.Usuario;
import br.com.pvv.senai.enums.Perfil;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class UsuarioDtoMinimal extends GenericDto<Usuario> {

	@Schema(description = "Identificador único do usuário", example = "1")
	private long Id;
	@Schema(description = "Nome completo do usuário", example = "João Silva")
	private String nome;
	@NotNull
	@Size(max = 255)
	@Schema(description = "Endereço de e-mail do usuário", example = "joao.silva@example.com")
	private String email;
	@Schema(description = "Data de nascimento do usuário", example = "1990-01-01")
	private Date dataNascimento;
	@Schema(description = "CPF do usuário", example = "123.456.789-00")
	private String cpf;
	@NotNull
	@Size(max = 255)
	@Schema(description = "Senha do usuário", example = "senhaSegura123")
	private String password;
	@NotNull
	@Schema(description = "Perfil do usuário")
	private Perfil perfil;

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

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public Perfil getPerfil() {
		return perfil;
	}

	public void setPerfil(Perfil perfil) {
		this.perfil = perfil;
	}

	@Override
	protected Class<Usuario> getType() {
		return Usuario.class;
	}

}
