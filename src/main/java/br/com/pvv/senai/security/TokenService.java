package br.com.pvv.senai.security;

import java.time.Instant;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.stereotype.Service;

import br.com.pvv.senai.entity.Usuario;

@Service
public class TokenService {

	private JwtEncoder encoder;
	private JwtDecoder decoder;

	public TokenService(JwtEncoder encoder, JwtDecoder decoder) {
		super();
		this.encoder = encoder;
		this.decoder = decoder;
	}

	public String generateToken(Usuario authentication) {
		Instant now = Instant.now();
		long expiry = 3600L;

		String scope = authentication.getAuthorities().stream().map(GrantedAuthority::getAuthority)
				.collect(Collectors.joining(" "));
		var claims = JwtClaimsSet.builder().issuer("senai-labmedical").issuedAt(now).expiresAt(now.plusSeconds(expiry))
				.subject(authentication.getEmail()).claim("scope", scope).build();
		return encoder.encode(JwtEncoderParameters.from(claims)).getTokenValue();
	}

	public String generateToken(Authentication authentication) {
		Instant now = Instant.now();
		long expiry = 3600L;

		String scope = authentication.getAuthorities().stream().map(GrantedAuthority::getAuthority)
				.collect(Collectors.joining(" "));
		var claims = JwtClaimsSet.builder().issuer("senai-labmedical").issuedAt(now).expiresAt(now.plusSeconds(expiry))
				.subject(authentication.getName()).claim("scope", scope).build();
		return encoder.encode(JwtEncoderParameters.from(claims)).getTokenValue();
	}

	public String validateToken(String token) {
		return decoder.decode(token).getSubject();
	}

}
