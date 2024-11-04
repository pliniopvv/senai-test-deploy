package br.com.pvv.senai.controller;

import br.com.pvv.senai.entity.Usuario;
import br.com.pvv.senai.enums.Perfil;
import br.com.pvv.senai.repository.UserRepository;
import br.com.pvv.senai.security.TokenService;
import br.com.pvv.senai.security.UsuarioService;
import br.com.pvv.senai.service.PacienteService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.CoreMatchers.hasItem;
import static org.hamcrest.CoreMatchers.not;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.jwt;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@WebMvcTest(UsuarioController.class)
class UsuarioControllerTest {

	@Autowired
	MockMvc mvc;

	@MockBean
	UsuarioService usuarioService;
	
	@MockBean
	PacienteService pacienteService;

	@MockBean
	TokenService tokenService;

	@MockBean
	UserRepository repository;

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
	};

	@Test
	@DisplayName("GET USUÁRIO - 200 - PERFIL USUÁRIO LOGADO")
	@WithMockUser(username = "teste@teste.com", roles = { "MEDICO", "PACIENTE" })
	void me_200() throws Exception {
		when(usuarioService.findByEmail(any(String.class))).thenReturn(Optional.of(usuarioCorrect));

		mvc.perform(get("/usuarios/me").with(jwt())).andExpect(jsonPath("$.nome").value("Nome de usuário"))
				.andDo(print());

		verify(usuarioService).findByEmail(any(String.class));
	}

	@Test
	@DisplayName("POST USUÁRIO - 201 - REALIZAR CADASTRO")
	@WithMockUser(username = "admin", roles = { "ADMIN", "MEDICO", "PACIENTE" })
	void post_201() throws Exception {
		when(usuarioService.create(any(Usuario.class))).thenReturn(usuarioCorrect);

		mvc.perform(post("/usuarios").with(jwt()).contentType(MediaType.APPLICATION_JSON).content("""
						{
						  "nome": "Nome de usuário",
						  "email": "usuario@teste.com",
						  "dataNascimento": "1980-10-10",
						  "telefone": "(48) 9 9999-9999",
						  "cpf": "000.000.000-00",
						  "password": "12341234",
						  "perfil": 1
						}
				""")).andExpect(status().isCreated()).andExpect(jsonPath("$.nome").value("Nome de usuário"));

		verify(usuarioService).create(any(Usuario.class));
	}

	@Test
	@DisplayName("POST USUÁRIO - 400 - EMAIL INVÁLIDO")
	@WithMockUser(username = "admin", roles = { "ADMIN", "MEDICO", "PACIENTE" })
	void post_400() throws Exception {
		mvc.perform(post("/usuarios").with(jwt()).contentType(MediaType.APPLICATION_JSON).content("""
						{
						  "nome": "Nome de usuário",
						  "email": "usuarioteste.com",
						  "dataNascimento": "1980-10-10",
						  "telefone": "(48) 9 9999-9999",
						  "cpf": "000.000.000-00",
						  "password": "12341234",
						  "perfil": 1
						}
				""")).andExpect(status().isBadRequest());
	}

	@Test
	@DisplayName("POST USUÁRIO - 401 - CAD. PERFIL PACIENTE")
	@WithMockUser(username = "admin", roles = { "ADMIN", "MEDICO", "PACIENTE" })
	void post_401() throws Exception {

		when(repository.findByEmail(any(String.class))).thenReturn(Optional.of(usuarioWrongMail));

		mvc.perform(post("/usuarios").with(jwt()).contentType(MediaType.APPLICATION_JSON).content("""
						{
						  "nome": "Nome de usuário",
						  "email": "usuario@teste.com",
						  "dataNascimento": "1980-10-10",
						  "telefone": "(48) 9 9999-9999",
						  "cpf": "000.000.000-00",
						  "password": "12341234",
						  "perfil": 2
						}
				""")).andExpect(status().isUnauthorized());
	}

	@Test
	@DisplayName("ALTERAR USUÁRIO - 204 - ALTERAR SENHA USUÁRIO")
	void put_200() throws Exception {
		when(usuarioService.findByEmail(any(String.class))).thenReturn(Optional.ofNullable(usuarioCorrect));

		mvc.perform(put("/usuarios/email/usuario@mail.com/redefinir-senha").with(jwt())
				.contentType(MediaType.APPLICATION_JSON).content("12341234")).andExpect(status().isNoContent());

		verify(usuarioService).findByEmail(any(String.class));
	}

	@Test
	@DisplayName("GET USUÁRIOS - 200 - LISTAR USUÁRIOS E REMOVE PACIENTE")
	@WithMockUser(username = "admin", roles = { "ADMIN", "MEDICO", "PACIENTE" })
	void get_200() throws Exception {
		lenient().when(usuarioService.all()).thenReturn(List.of(usuarioCorrect, usuarioPaciente));

		mvc.perform(get("/usuarios").with(jwt())).andExpect(status().isOk())
				.andExpect(jsonPath("$.content[0].nome").value("Nome de usuário")).andDo(print());

		verify(usuarioService).all();
	}

	@Test
	@DisplayName("GET USUÁRIOS - 200 - LISTAR USUÁRIOS | CONSULTA NOME")
	@WithMockUser(username = "admin", roles = { "ADMIN", "MEDICO", "PACIENTE" })
	void get_200_withConsulta() throws Exception {
		lenient().when(usuarioService.paged(any(), any())).thenReturn(new PageImpl<Usuario>(List.of(usuarioCorrect)));

		mvc.perform(get("/usuarios?nome=Usuário").with(jwt())).andExpect(status().isOk())
				.andExpect(jsonPath("$.content[*].perfil", not(hasItem(Perfil.PACIENTE))))
				.andExpect(jsonPath("$.content[0].nome").value("Nome de usuário"));

		verify(usuarioService).paged(any(), any());
	}

}