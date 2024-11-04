package br.com.pvv.senai.security;

import br.com.pvv.senai.entity.Endereco;
import br.com.pvv.senai.entity.Paciente;
import br.com.pvv.senai.entity.Usuario;
import br.com.pvv.senai.enums.Perfil;
import br.com.pvv.senai.repository.PacienteRepository;
import br.com.pvv.senai.repository.UserRepository;
import br.com.pvv.senai.service.EnderecoService;
import br.com.pvv.senai.service.PacienteService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.*;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class PacienteServicetTest {

	@Mock
	PacienteRepository repository;

	@Mock
	UsuarioService userService;

	@Mock
	EnderecoService enderecoService;

	@Mock
	UserRepository userRepository;

	@InjectMocks
	PacienteService service;

	Paciente paciente;
	Usuario usuario;
	Usuario usuarioPaciente;

	@BeforeEach
	void setUp() {
		MockitoAnnotations.openMocks(this);
		paciente = new Paciente();
		paciente.setId(1L);
		paciente.setEmail("paciente@teste.com");
		paciente.setName("Maria Oliveira");
		paciente.setGender("Feminino");

		paciente.setCPF("123.456.789-00");
		paciente.setRG("MG-12.345.678");
		paciente.setMaritalStatus("Solteira");
		paciente.setPhone("(34) 9 8765-4321");
		paciente.setBirthCity("Uberlândia");
		paciente.setEmergencyContact("(34) 9 8765-4321");
		paciente.setAllergies("Nenhuma");
		paciente.setSpecialCare("Nenhum");
		paciente.setInsuranceCompany("Saúde Total");
		paciente.setInsuranceNumber("1234567890");

		usuario = new Usuario();
		usuario.setId(1L);
		usuario.setEmail("usuario@teste.com");
		usuario.setTelefone("123456789");
		usuario.setEmail("joao.silva@example.com");
		usuario.setCpf("123.456.789-00");
		usuario.setPassword("senha123");
		usuario.setSenhaMascarada("****");
		usuario.setPerfil(Perfil.ADMIN);

		usuarioPaciente = new Usuario();
		usuarioPaciente.setId(1L);
		usuarioPaciente.setEmail("usuario-paciente@teste.com");
		usuarioPaciente.setTelefone("123456789");
		usuarioPaciente.setEmail("joao.selva@example.com");
		usuarioPaciente.setCpf("123.456.000-00");
		usuarioPaciente.setPassword("senha123");
		usuarioPaciente.setSenhaMascarada("****");
		usuarioPaciente.setPerfil(Perfil.PACIENTE);

		paciente.setUsuario(usuarioPaciente);

		try {
			paciente.setInsuranceExpiration(new SimpleDateFormat("yyyy-MM-dd").parse("2025-12-31"));
			usuario.setDataNascimento(new SimpleDateFormat("yyyy-MM-dd").parse("1990-01-01"));
			paciente.setBirthDate(new SimpleDateFormat("yyyy-MM-dd").parse("1985-05-15"));
		} catch (ParseException e) {
			e.printStackTrace();
		}

	}

	@Test
	@DisplayName("Deve injetar o repositório")
	void getRepository() {
		assertNotNull(service.getRepository());
	}

	@Test
	@DisplayName("Deve retornar lista de pessoas pacientes")
	void list() {
		// Given
		when(repository.findAll()).thenReturn(List.of(paciente));
		// When
		var resultado = service.all();
		// Then
		assertNotNull(resultado);
		assertEquals(paciente.getId(), resultado.get(0).getId());
		verify(repository).findAll();
	}

	@Test
	@DisplayName("Deve retornar página de pessoas pacientes")
	void paged() {
		// Given
		Pageable pageable = PageRequest.of(0, 10);
		Page<Paciente> page = new PageImpl<>(List.of(paciente), pageable, 1);
		when(repository.findAll(any(Example.class), any(Pageable.class))).thenReturn(page);
		// When
		Page<Paciente> resultado = service.paged(Example.of(paciente), pageable);
		// Then
		assertNotNull(resultado);
		assertEquals(1, resultado.getTotalElements());
		assertEquals(paciente.getId(), resultado.getContent().get(0).getId());
		verify(repository).findAll(any(Example.class), any(Pageable.class));
	}

	@Test
	@DisplayName("Deve criar pessoa paciente")
	void create() {
		// Given
		when(repository.save(any(Paciente.class))).thenReturn(paciente);
		when(enderecoService.create(any())).thenReturn(new Endereco());
		when(userService.findByEmail(anyString())).thenReturn(Optional.of(usuario));
		// When
		Paciente pacienteSalvo = service.create(paciente);
		// Then
		assertNotNull(pacienteSalvo);
		assertEquals(paciente.getId(), pacienteSalvo.getId());
//		verify(repository).save(any(Paciente.class));
		verify(enderecoService).create(any());
		verify(userService).findByEmail(anyString());
	}

	@Test
	@DisplayName("Deve retornar pessoa paciente por id")
	void get() {
		// Given
		when(repository.findById(anyLong())).thenReturn(Optional.of(paciente));
		// When
		Paciente pacienteEncontrado = service.get(1L);
		// Then
		assertNotNull(pacienteEncontrado);
		assertEquals(paciente.getId(), pacienteEncontrado.getId());
		verify(repository).findById(1L);
	}

	@Test
	@DisplayName("Deve alterar pessoa paciente")
	void alter() {
		Endereco endereco = new Endereco();
		// Given
		when(userService.getRepository()).thenReturn(userRepository);
		when(repository.save(any(Paciente.class))).thenReturn(paciente);
		when(userService.findByEmail(anyString())).thenReturn(Optional.of(usuario));
		when(enderecoService.create(any(Endereco.class))).thenReturn(endereco);
		when(repository.findById(anyLong())).thenReturn(Optional.of(paciente));
		// When
		Paciente pacienteAlterado = service.alter(1L, paciente);
		// Then
		assertNotNull(pacienteAlterado);
		assertEquals(paciente.getId(), pacienteAlterado.getId());
		verify(repository).findById(anyLong());
		verify(repository).save(any(Paciente.class));
		verify(userService).findByEmail(anyString());
		verify(userService).alter(anyLong(), any(Usuario.class));
	}

	@Test
	@DisplayName("Deve retornar true ao deletar pessoa paciente")
	void delete() {
		// Given
		when(repository.findById(any())).thenReturn(Optional.of(paciente));
		// When
		boolean deletado = service.delete(1L);
		// Then
		assertTrue(deletado);
		verify(repository).delete(paciente);
	}

	@Test
	@DisplayName("Deve retornar quantidade de pessoas pacientes no repositório")
	void count() {
		// Given
		when(repository.count()).thenReturn(1L);
		// When
		Long quantidade = service.count();
		// Then
		assertEquals(1L, quantidade);
		verify(repository).count();
	}

}
