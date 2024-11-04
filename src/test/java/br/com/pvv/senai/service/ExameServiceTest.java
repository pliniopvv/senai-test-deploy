package br.com.pvv.senai.service;

import br.com.pvv.senai.entity.Exame;
import br.com.pvv.senai.entity.Paciente;
import br.com.pvv.senai.repository.ExameRepository;
import br.com.pvv.senai.repository.PacienteRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.*;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class ExameServiceTest {

    @Mock
    ExameRepository repository;

    @Mock
    PacienteRepository pacienteRepository;

    @InjectMocks
    ExameService service;

    @Mock
    PacienteService pacienteService;

    Exame exame;
    Paciente paciente;
    List<Exame> exames;
    
    @BeforeEach
    void setup(){
        MockitoAnnotations.openMocks(this);

        paciente = new Paciente();
        paciente.setId(1L);
        paciente.setEmail("paciente@teste.com");
        paciente.setName("João Silva");
        paciente.setGender("Masculino");
        paciente.setCPF("123.456.789-00");
        paciente.setRG("SC-12.345.678");
        paciente.setMaritalStatus("Solteiro");
        paciente.setPhone("(34) 9 8765-4321");
        paciente.setBirthCity("Florianópolis");
        paciente.setEmergencyContact("(34) 9 8765-4321");
        paciente.setAllergies("Nenhuma");
        paciente.setSpecialCare("Nenhum");
        paciente.setInsuranceCompany("Saúde Qcode");
        paciente.setInsuranceNumber("1234567890");

        exame = new Exame();
        exame.setId(1L);
        exame.setNome("Nome do exame");
        exame.setTipo("Tipo do exame");
        exame.setLaboratorio("Laboratório do exame");
        exame.setResultados("Resultados do exame");
        exame.setPaciente(paciente);
        exame.setDataExame(LocalDate.parse("2021-12-31"));
        exame.setHoraExame(LocalTime.parse("10:00:00"));
    }
    
    @Test
    @DisplayName("Deve injetar o repositório")
    void getRepository() {
        assertNotNull(service.getRepository());
    }

    @Test
    @DisplayName("Deve criar um exame")
    void create() {
        // Given
        when(repository.save(any(Exame.class))).thenReturn(exame);
        // When
        Exame exameSalvo = service.create(exame);
        // Then
        assertNotNull(exameSalvo);
        assertEquals(exameSalvo.getId(), exame.getId());
        verify(repository).save(any(Exame.class));
    }

    @Test
    @DisplayName("Deve alterar um exame")
    void alter() {
        // Given
        when(repository.save(any(Exame.class))).thenReturn(exame);
        // When
        Exame exameSalvo = service.alter(1L, exame);
        // Then
        assertNotNull(exameSalvo);
        assertEquals(exameSalvo.getId(), exame.getId());
        verify(repository).save(any(Exame.class));
    }

    @Test
    @DisplayName("Deve buscar exames por id do paciente")
    void findByPacienteId() {
        // Given
        when(repository.findByPacienteId(1L)).thenReturn(List.of(exame));
        // When
        exames = service.findByPacienteId(1L);
        // Then
        assertEquals(exames.get(0).getId(), exame.getId());
        verify(repository).findByPacienteId(1L);
    }

    @Test
    @DisplayName("Deve retornar quantidade de exames no repositório")
    void count() {
        // Given
        when(repository.count()).thenReturn(1L);
        // When
        Long quantidade = service.count();
        // Then
        assertEquals(1L, quantidade);
        verify(repository).count();
    }

    @Test
    @DisplayName("Deve retornar exame pelo id")
    void get(){
        // Given
        when(repository.findById(1L)).thenReturn(Optional.of(exame));
        // When
        Exame exameEncontrado = service.get(1L);
        // Then
        assertNotNull(exameEncontrado);
        assertEquals(exame.getId(), exameEncontrado.getId());
        verify(repository).findById(1L);
    }

    @Test
    @DisplayName("Deve deletar exame pelo id")
    void delete(){
        // Given
        when(repository.findById(1L)).thenReturn(Optional.of(exame));
        // When
        boolean deletado = service.delete(1L);
        // Then
        assertTrue(deletado);
        verify(repository).delete(exame);
    }

    @Test
    @DisplayName("Deve retornar página de exames")
    void paged(){
        // Given
        Pageable pageable = PageRequest.of(0, 10);
        Page<Exame> page = new PageImpl<>(List.of(exame), pageable, 1);
        when(repository.findAll(any(Example.class), any(Pageable.class))).thenReturn(page);
        // When
        Page<Exame> resultado = service.paged(Example.of(exame), pageable);
        // Then
        assertNotNull(resultado);
        assertEquals(1, resultado.getTotalElements());
        assertEquals(exame.getId(), resultado.getContent().get(0).getId());
        verify(repository).findAll(any(Example.class), any(Pageable.class));
    }

    @Test
    @DisplayName("Deve retornar lista com todos os exames")
    void all(){
        // Given
        when(repository.findAll()).thenReturn(List.of(exame));
        // When
        var resultado = service.all();
        // Then
        assertNotNull(resultado);
        assertEquals(paciente.getId(), resultado.get(0).getId());
        verify(repository).findAll();
    }
    
}