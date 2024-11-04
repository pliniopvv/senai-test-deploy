package br.com.pvv.senai.security;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

@Service
public class AuthenticationService {

	private final TokenService jwtService;

	public AuthenticationService(TokenService jwtService) {
		super();
		this.jwtService = jwtService;
	}

	public String authenticate(Authentication authentication) {
		return jwtService.generateToken(authentication);
	}

}
