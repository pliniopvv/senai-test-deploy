package br.com.pvv.senai.security;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.Instant;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;

import br.com.pvv.senai.entity.Usuario;
import br.com.pvv.senai.enums.Perfil;

@ExtendWith(MockitoExtension.class)
class TokenServiceTest {

	@InjectMocks
	TokenService service;

	@Mock
	JwtEncoder encoder;

	@Mock
	JwtDecoder decoder;

	Usuario usuario;

	@BeforeEach
	void setup() {
		MockitoAnnotations.openMocks(this);
		usuario = new Usuario();
		usuario.setEmail("usuario@teste.com");
		usuario.setPerfil(Perfil.ADMIN);
	}

	@Test
	@DisplayName("Deve gerar um token para Usuario do tipo Admin")
	void generateTokenUsuario() {
		// Given
		List<GrantedAuthority> authorities = List.of(() -> "ROLE_ADMIN");
		Usuario usuario = mock(Usuario.class);
		when(usuario.getEmail()).thenReturn("usuario@teste.com");
		when(usuario.getAuthorities()).thenReturn((Collection) authorities);

		JwtClaimsSet claims = JwtClaimsSet.builder().issuer("senai-labmedical").issuedAt(Instant.now())
				.expiresAt(Instant.now().plusSeconds(3600L)).subject(usuario.getEmail()).claim("scope", "ROLE_ADMIN")
				.build();

		Map<String, Object> headers = Map.of("alg", "HS256");

		Jwt jwt = new Jwt("mocked-token", Instant.now(), Instant.now().plusSeconds(3600L), headers, claims.getClaims());

		System.out.println(jwt.getTokenValue());

		when(encoder.encode(any(JwtEncoderParameters.class))).thenReturn(jwt);

		// When
		String token = service.generateToken(usuario);
		// Then
		assertNotNull(token);
		assertEquals("mocked-token", token);
		verify(encoder).encode(any(JwtEncoderParameters.class));

	}

	@Test
	@DisplayName("Deve gerar um token recebendo um objeto Authentication")
	void GenerateTokenAuthentication() {
		// Given
		Authentication authentication = mock(Authentication.class);
		when(authentication.getName()).thenReturn("authUser");
		when(authentication.getAuthorities())
				.thenReturn((Collection) List.of(new SimpleGrantedAuthority("ROLE_ADMIN")));

		JwtClaimsSet claims = JwtClaimsSet.builder().issuer("senai-labmedical").issuedAt(Instant.now())
				.expiresAt(Instant.now().plusSeconds(3600L)).subject(authentication.getName())
				.claim("scope", "ROLE_ADMIN").build();
		
		Map<String, Object> headers = Map.of("alg", "HS256");

		Jwt jwt = new Jwt("mocked-token", Instant.now(), Instant.now().plusSeconds(3600L), headers, claims.getClaims());

		when(encoder.encode(any(JwtEncoderParameters.class))).thenReturn(jwt);

		// When
		String token = service.generateToken(authentication);

		// Then
		assertNotNull(token);
		assertEquals("mocked-token", token);
		verify(encoder).encode(any(JwtEncoderParameters.class));
	}

	@Test
	@DisplayName("Deve validar um token")
	void validateToken() {
		// Given
		String token = "token-válido";
		Jwt jwt = mock(Jwt.class);
		when(jwt.getSubject()).thenReturn("subject");
		when(decoder.decode(token)).thenReturn(jwt);

		// When
		String subject = service.validateToken(token);

		// Then
		assertNotNull(subject);
		assertEquals("subject", subject);
		verify(decoder).decode(token);
	}
}