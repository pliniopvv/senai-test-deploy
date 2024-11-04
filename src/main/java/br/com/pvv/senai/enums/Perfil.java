package br.com.pvv.senai.enums;

import org.springframework.security.core.GrantedAuthority;

public enum Perfil implements GrantedAuthority {
	ADMIN, MEDICO, PACIENTE;

	@Override
	public String getAuthority() {
		return "ROLE_" + name();
	}

	public String scope() {
		return "SCOPE_ROLE_" + name();
	}
}
