package br.com.pvv.senai.controller;

import br.com.pvv.senai.entity.*;
import br.com.pvv.senai.enums.Perfil;
import br.com.pvv.senai.model.dto.ConsultaDto;
import br.com.pvv.senai.model.dto.ExameDto;
import br.com.pvv.senai.model.dto.PacienteDto;
import br.com.pvv.senai.security.SecurityFilter;
import br.com.pvv.senai.security.UsuarioService;
import br.com.pvv.senai.service.ConsultaService;
import br.com.pvv.senai.service.ExameService;
import br.com.pvv.senai.service.PacienteService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageImpl;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.mock;

@AutoConfigureMockMvc
@ExtendWith(MockitoExtension.class)
public class ExameControllerTest {

	@InjectMocks
	ExameController controller;

	@Mock
	ExameService service;

	@Mock
	PacienteService patientService;

	@Mock
	UsuarioService usuarioService;

	@MockBean
	ExameService exameService;

	@MockBean
	ConsultaService consultaService;

	@MockBean
	SecurityFilter securityFilter;

	Usuario usuarioCorrect;
	Usuario usuarioWrongMail;
	Usuario usuarioPaciente;
	PacienteDto putPaciente;
	Paciente paciente;

	ConsultaDto consultaDto;
	ConsultaDto putConsulta;
	Consulta consulta;
	ExameDto putExame;
	Exame exame;

	String token;

	void clearEntitys() {
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
		usuarioPaciente.setEmail("carlos.souza@example.com");
		usuarioPaciente.setDataNascimento(java.sql.Date.valueOf(LocalDate.of(1980, 10, 10)));
		usuarioPaciente.setTelefone("(48) 9 9999-9999");
		usuarioPaciente.setCpf("000.000.000-00");
		usuarioPaciente.setPassword("12341234");
		usuarioPaciente.setPerfil(Perfil.PACIENTE);

		consulta = new Consulta();

		consulta.setReason("Check-up anual");
		consulta.setDate(LocalDate.now());
		consulta.setTime(LocalTime.now());
		consulta.setIssueDescription("Paciente relatou dores de cabeça frequentes e cansaço.");
		consulta.setPrescribedMedication("Paracetamol 500mg");
		consulta.setObservation("Recomendada hidratação e repouso.");

		putConsulta = new ConsultaDto();

		putConsulta.setReason("Check-up anual");
		putConsulta.setDate(LocalDate.now());
		putConsulta.setTime(LocalTime.now());
		putConsulta.setIssueDescription("Paciente relatou dores de cabeça frequentes e cansaço.");
		putConsulta.setPrescribedMedication("Paracetamol 500mg");
		putConsulta.setObservation("Recomendada hidratação e repouso.");

		consultaDto = new ConsultaDto();

		consultaDto.setReason("Consulta de rotina");
		consultaDto.setDate(LocalDate.of(2024, 10, 31));
		consultaDto.setTime(LocalTime.of(14, 30, 0));
		consultaDto.setIssueDescription("Paciente relata dores intensas na região lombar.");
		consultaDto.setPrescribedMedication("Ibuprofeno 400mg");
		consultaDto.setObservation("Paciente deve retornar em 2 semanas para acompanhamento.");

		PacienteDto pacienteDto = new PacienteDto();
		pacienteDto.setId(67890);
		pacienteDto.setName("Ana Pereira");
		consultaDto.setPatient(pacienteDto);

		consultaDto.setPatientId(67890);

		paciente = new Paciente();

		paciente.setName("Carlos Souza");
		paciente.setGender("Masculino");
		paciente.setBirthDate(new Date(85, 3, 25));
		paciente.setCPF("987.654.321-00");
		paciente.setRG("SP-43.210.987");
		paciente.setMaritalStatus("Casado");
		paciente.setPhone("(11) 9 8765-4321");
		paciente.setEmail("carlos.souza@example.com");
		paciente.setBirthCity("São Paulo");
		paciente.setEmergencyContact("(11) 9 8765-4321");
		paciente.setAllergies("Nenhuma");
		paciente.setSpecialCare("Nenhum");
		paciente.setInsuranceCompany("Bradesco Saúde");
		paciente.setInsuranceNumber("9876543210");
		paciente.setInsuranceExpiration(new Date(126, 5, 30));

		putPaciente = new PacienteDto();

		putPaciente.setName("Carlos Souza");
		putPaciente.setGender("Masculino");
		putPaciente.setBirthDate(new Date(85, 3, 25));
		putPaciente.setCPF("987.654.321-00");
		putPaciente.setRG("SP-43.210.987");
		putPaciente.setMaritalStatus("Casado");
		putPaciente.setPhone("(11) 9 8765-4321");
		putPaciente.setEmail("carlos.souza@example.com");
		putPaciente.setBirthCity("São Paulo");
		putPaciente.setEmergencyContact("(11) 9 8765-4321");
		putPaciente.setAllergies("Nenhuma");
		putPaciente.setSpecialCare("Nenhum");
		putPaciente.setInsuranceCompany("Bradesco Saúde");
		putPaciente.setInsuranceNumber("9876543210");
		putPaciente.setInsuranceExpiration(new Date(126, 5, 30));

		Endereco endereco = new Endereco();

		endereco.setCEP("38400-000");
		endereco.setCidade("Uberlândia");
		endereco.setLogradouro("Rua Exemplo");
		endereco.setNumero(123);
		endereco.setComplemento("Apto 101");
		endereco.setBairro("Centro");
		endereco.setPontoDeReferencia("Próximo ao supermercado");

		exame = new Exame();

		exame.setNome("Hemograma Completo");
		exame.setDataExame(LocalDate.of(2024, 11, 2));
		exame.setHoraExame(LocalTime.of(10, 30, 0));
		exame.setTipo("Sangue");
		exame.setLaboratorio("Laboratório XYZ");
		exame.setURL("http://example.com/resultados/12345");
		exame.setResultados("Resultados normais, sem alterações significativas.");
		
		exame.setPaciente(paciente);
		
		putExame = new ExameDto();

		putExame.setNome("Hemograma Completo");
		putExame.setDataExame(LocalDate.of(2024, 11, 2));
		putExame.setHoraExame(LocalTime.of(10, 30, 0));
		putExame.setTipo("Sangue");
		putExame.setLaboratorio("Laboratório XYZ");
		putExame.setURL("http://example.com/resultados/12345");
		putExame.setResultados("Resultados normais, sem alterações significativas.");
		
		putExame.setPaciente(putPaciente);

		paciente.setUsuario(usuarioPaciente);
		paciente.setAddress(endereco);

		consulta.setPatient(paciente);
	}

	@BeforeEach
	void setup() {
		clearEntitys();
	};

	void authenticationMock(){
		Authentication authentication = mock(Authentication.class);
		when(authentication.getName()).thenReturn("mockUser");
		SecurityContext securityContext = mock(SecurityContext.class);
		when(securityContext.getAuthentication()).thenReturn(authentication);
		SecurityContextHolder.setContext(securityContext);

		Usuario usuarioAutenticado = mock(Usuario.class);
		when(usuarioAutenticado.getPerfil()).thenReturn(Perfil.ADMIN);
		when(usuarioService.findByEmail("mockUser")).thenReturn(Optional.of(usuarioAutenticado));
	}

	@Test
	@DisplayName("LISTAR EXAME - 200 - OBTÉM LISTA DOS EXAMES")
	void list_200() throws Exception {

		this.authenticationMock();

		when(service.all()).thenReturn(List.of(exame));

		var resp = controller.list(Map.of());
		var body = resp.getBody().getContent().get(0);

		verify(service).all();

		assertNotNull(resp);
		assertEquals(exame.getNome(), body.getNome());
	}

	@Test
	@DisplayName("LISTAR EXAMES - 200 - OBTÉM LISTA DOS EXAMES COM FILTRO")
	void list_200_withFilter() throws Exception {

		this.authenticationMock();

		when(service.paged(any(), any())).thenReturn(new PageImpl(List.of(exame)));

		var resp = controller.list(Map.of("name", exame.getNome()));
		var body = resp.getBody().getContent().get(0);

		verify(service).paged(any(), any());

		assertNotNull(resp);
		assertEquals(exame.getNome(), body.getNome());
	}

	@Test
	@DisplayName("CONSULTA EXAME - 200 - OBTÉM EXAMES DETERMINADO")
	void get_200() throws Exception {

		this.authenticationMock();

		when(service.get(anyLong())).thenReturn(exame);

		var resp = controller.get(1L);
		var body = (Exame) resp.getBody();

		verify(service).get(anyLong());

		assertNotNull(resp);
		assertEquals(exame.getNome(), body.getNome());
	}

	@Test
	@DisplayName("ATUALIZA EXAME - 200 - ATUALIZA EXAME DETERMINADO")
	void put_200() throws Exception {
		when(patientService.get(anyLong())).thenReturn(paciente);
		when(service.get(anyLong())).thenReturn(exame);
		when(service.alter(anyLong(), any())).thenReturn(exame);

		var resp = controller.put(1L, putExame);
		var body = (Exame) resp.getBody();

		verify(patientService).get(anyLong());
		verify(service).get(anyLong());
		verify(service).alter(anyLong(), any());

		assertNotNull(resp);
		assertEquals(exame.getNome(), body.getNome());
	}

	@Test
	@DisplayName("EXCLUI EXAME - 200 - REMOVE EXAME DETERMINADO")
	void delete_200() throws Exception {
		when(service.delete(anyLong())).thenReturn(true);

		controller.delete(1L);

		verify(service).delete(anyLong());
	}

}
