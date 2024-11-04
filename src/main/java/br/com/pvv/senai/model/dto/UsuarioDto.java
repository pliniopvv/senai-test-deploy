package br.com.pvv.senai.model.dto;

import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;

import br.com.pvv.senai.entity.Usuario;
import br.com.pvv.senai.enums.Perfil;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public class UsuarioDto extends GenericDto<Usuario> {

	@Schema(description = "ID do usuário", example = "12345", requiredMode = Schema.RequiredMode.REQUIRED)
    private long Id;
    @NotNull
    @Size(max = 255)
    @Schema(description = "Nome do usuário", example = "João da Silva", requiredMode = Schema.RequiredMode.REQUIRED)
    private String nome;
    @NotNull
    @Pattern(regexp = "\\(\\d{2}\\) \\d{1} \\d{4}-\\d{4}")
    @Schema(description = "Telefone do usuário", example = "(12) 3 4567-8901", requiredMode = Schema.RequiredMode.REQUIRED)
    private String telefone;
    @NotNull
    @Email
    @Size(max = 255)
    @Schema(description = "Email do usuário", example = "joao.silva@example.com", requiredMode = Schema.RequiredMode.REQUIRED)
    private String email;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @Schema(description = "Data de nascimento do usuário", example = "1990-01-01")
    private Date dataNascimento;
    @NotNull
    @Size(max = 14)
    @Schema(description = "CPF do usuário", example = "123.456.789-00", requiredMode = Schema.RequiredMode.REQUIRED)
    private String cpf;
    @NotNull
    @Size(max = 255)
    @Schema(description = "Senha do usuário", example = "senhaSegura123", requiredMode = Schema.RequiredMode.REQUIRED)
    private String password;
    @NotNull
    @Schema(description = "Perfil do usuário", example = "ADMIN", requiredMode = Schema.RequiredMode.REQUIRED)
    private Perfil perfil;

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
