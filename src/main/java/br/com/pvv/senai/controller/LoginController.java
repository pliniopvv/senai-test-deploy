package br.com.pvv.senai.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.pvv.senai.entity.Usuario;
import br.com.pvv.senai.exceptions.UnauthorizationException;
import br.com.pvv.senai.model.dto.LoginRequestDTO;
import br.com.pvv.senai.repository.UserRepository;
import br.com.pvv.senai.security.TokenService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/login")
public class LoginController {

	@Autowired
	private AuthenticationManager manager;

	@Autowired
	private UserRepository repository;

	@Autowired
	private TokenService jwtService;

	@PostMapping
	@Operation(summary = "Login de usuário", description = "Autentica um usuário com base nas credenciais fornecidas.")
	public ResponseEntity login(
			@Parameter(description = "Credenciais de login do usuário", required = true) @RequestBody @Valid LoginRequestDTO login)
			throws MethodArgumentNotValidException, UnauthorizationException {
		var usernamePassword = new UsernamePasswordAuthenticationToken(login.getUsername(), login.getPassword());
		try {
			var auth = this.manager.authenticate(usernamePassword);
			Usuario user = repository.findByEmail(login.getUsername()).orElse(null);
			if (user == null)
				throw new UnauthorizationException();
			var token = jwtService.generateToken(user);

			return ResponseEntity.ok(token);
		} catch (Exception e) {
			throw new UnauthorizationException();
		}
	}

}
