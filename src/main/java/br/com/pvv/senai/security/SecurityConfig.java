package br.com.pvv.senai.security;

import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.util.Arrays;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jose.jwk.RSAKey;
import com.nimbusds.jose.jwk.source.ImmutableJWKSet;

import br.com.pvv.senai.enums.Perfil;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

	@Value("${jwt.public.key}")
	private RSAPublicKey key;
	@Value("${jwt.private.key}")
	private RSAPrivateKey priv;

	@Autowired
	private SecurityFilter secFilter;

	UrlBasedCorsConfigurationSource apiConfigurationSource() {
		CorsConfiguration configuration = new CorsConfiguration();
		configuration.setAllowedOrigins(Arrays.asList("https://**"));
		configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE"));
		UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
		source.registerCorsConfiguration("/**", configuration);
		return source;
	}

	@Bean
	public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

		http.cors(Customizer.withDefaults());

		http.csrf(csrf -> csrf.disable())
				.sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
				.authorizeHttpRequests(auth -> auth
						// GUEST
						.requestMatchers( //
								"/login", //
								"/usuarios/pre-registro", //
								"/usuarios/email/{email}/redefinir-senha", //
								"/swagger-ui" //
						).permitAll()
						// pacientes
						.requestMatchers(HttpMethod.GET, //
								"/pacientes/{id}", //
								"/consultas/{id}", //
								"/consultas", //
								"/exames", //
								"/exames/{id}", //
								"/usuarios/me" //
						).hasAuthority(Perfil.PACIENTE.scope())
						// MEDICO
						.requestMatchers(HttpMethod.POST, //
								"/exames", //
								"/consultas", //
								"/pacientes")
						.hasAuthority(Perfil.MEDICO.scope())//
						.requestMatchers(HttpMethod.PUT, //
								"/exames/{id}", //
								"/consultas/{id}", //
								"/pacientes/{id}")
						.hasAuthority(Perfil.MEDICO.scope())//
						.requestMatchers(HttpMethod.DELETE, //
								"/exames/{id}", //
								"/consultas/{id}", //
								"/pacientes/{id}")
						.hasAuthority(Perfil.MEDICO.scope())//
						.requestMatchers(HttpMethod.GET, //
								"/dashboard**", //
								"/pacientes/{id}/prontuarios", //
								"/pacientes/prontuarios", //
//								"/exames/{id}", //
//								"/consultas/{id}", //
								"/pacientes", //
								"/pacientes/{id}", //
								"/consultas", //
								"/exames" //
						).hasAuthority(Perfil.MEDICO.scope())

						// ADMIN
						.requestMatchers(HttpMethod.POST, "/exames", //
								"/consultas", //
								"/pacientes", //
								"/usuarios")
						.hasAuthority(Perfil.ADMIN.scope())//
						.requestMatchers(HttpMethod.PUT, //
								"/exames/{id}", //
								"/consultas/{id}", //
								"/pacientes/{id}", //
								"/usuarios/{id}")
						.hasAuthority(Perfil.ADMIN.scope())//
						.requestMatchers(HttpMethod.DELETE, //
								"/exames/{id}", //
								"/consultas/{id}", //
								"/pacientes/{id}", //
								"/usuarios/{id}")
						.hasAuthority(Perfil.ADMIN.scope()).requestMatchers(HttpMethod.GET, //
								"/usuarios/{id}", //
								"/usuarios" //
//								"/dashboard**", //
//								"/pacientes/{id}/prontuarios", //
//								"/pacientes/prontuarios", //
////								"/exames/{id}", //
////								"/consultas/{id}", //
//								"/pacientes"// , //
////								"/pacientes/{id}" //
						).hasAuthority(Perfil.ADMIN.scope()) //
						.requestMatchers(HttpMethod.GET, "/**") //
						.permitAll()
				// OUTRAS ROTAS
//						.anyRequest().denyAll()//
				).oauth2ResourceServer(oauth2 -> oauth2.jwt(Customizer.withDefaults()))
				.addFilterBefore(secFilter, UsernamePasswordAuthenticationFilter.class);

		return http.build();
	}

	@Bean
	JwtDecoder jwtDecoder() {
		return NimbusJwtDecoder.withPublicKey(key).build();
	}

	@Bean
	AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration)
			throws Exception {
		return authenticationConfiguration.getAuthenticationManager();
	}

	@Bean
	JwtEncoder jwtEncoder() {
		var jwk = new RSAKey.Builder(key).privateKey(priv).build();
		var jwks = new ImmutableJWKSet<>(new JWKSet(jwk));
		return new NimbusJwtEncoder(jwks);
	}

	@Bean
	PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

}
