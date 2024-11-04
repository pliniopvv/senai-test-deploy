package br.com.pvv.senai.controller;

import br.com.pvv.senai.entity.Usuario;
import br.com.pvv.senai.enums.Perfil;
import br.com.pvv.senai.repository.UserRepository;
import br.com.pvv.senai.security.TokenService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class LoginControllerTest {

	@Autowired
	MockMvc mvc;

	@Autowired
	private UserRepository repository;

	@MockBean
	private TokenService jwtService;

	@MockBean
	private AuthenticationManager manager;

	Usuario usuarioCorrect;
	Usuario usuarioWrongMail;
	Usuario usuarioPaciente;

	@BeforeEach
	void setup() {
		usuarioCorrect = new Usuario();
		usuarioCorrect.setNome("Nome de usuário");
		usuarioCorrect.setEmail("usuario@teste.com");
		usuarioCorrect.setDataNascimento(java.sql.Date.valueOf(LocalDate.of(1980, 10, 10)));
		usuarioCorrect.setTelefone("(48) 9 9999-9999");
		usuarioCorrect.setCpf("000.000.000-00");
		usuarioCorrect.setPassword("12341234");
		usuarioCorrect.setPerfil(Perfil.MEDICO);

		usuarioWrongMail = new Usuario();
		usuarioWrongMail.setNome("Nome de usuário");
		usuarioWrongMail.setEmail("usuarioteste.com");
		usuarioWrongMail.setDataNascimento(java.sql.Date.valueOf(LocalDate.of(1980, 10, 10)));
		usuarioWrongMail.setTelefone("(48) 9 9999-9999");
		usuarioWrongMail.setCpf("000.000.000-00");
		usuarioWrongMail.setPassword("12341234");
		usuarioWrongMail.setPerfil(Perfil.MEDICO);

		usuarioPaciente = new Usuario();
		usuarioPaciente.setNome("Nome de usuário");
		usuarioPaciente.setEmail("usuarioteste.com");
		usuarioPaciente.setDataNascimento(java.sql.Date.valueOf(LocalDate.of(1980, 10, 10)));
		usuarioPaciente.setTelefone("(48) 9 9999-9999");
		usuarioPaciente.setCpf("000.000.000-00");
		usuarioPaciente.setPassword("12341234");
		usuarioPaciente.setPerfil(Perfil.PACIENTE);
		
		repository.save(usuarioCorrect);
	};
	
	@AfterEach
	void clear() {
		repository.deleteAll();
	}

	@Test
	@DisplayName("")
	@WithAnonymousUser
	void login() throws Exception {

		mvc.perform(post("/login").contentType(MediaType.APPLICATION_JSON).content(
				"""
				{
				  "username": "usuario@teste.com",
				  "password": "12341234"
				}
				""")).andExpect(status().isOk());
	}

}
