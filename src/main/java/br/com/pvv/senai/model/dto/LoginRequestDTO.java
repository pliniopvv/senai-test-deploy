package br.com.pvv.senai.model.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;

public class LoginRequestDTO {

	@NotNull
    @Email
    @Schema(description = "Nome do usuário, deve ser um e-mail válido", 
            example = "user@example.com")
    private String username;

    @NotNull
    @Schema(description = "Senha do usuário, deve ser não nula", 
            example = "P@ssw0rd!")
    private String password;

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

}
